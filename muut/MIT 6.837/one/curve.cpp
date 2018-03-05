#include "curve.h"
#include "extra.h"
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
using namespace std;

namespace
{
    // Approximately equal to.  We don't want to use == because of
    // precision issues with floating point.
    inline bool approx( const Vector3f& lhs, const Vector3f& rhs )
    {
        const float eps = 1e-8f;
        return ( lhs - rhs ).absSquared() < eps;
    }

	// bernstein spline
	Matrix4f B(
		1.0f, -3.0f, 3.0f, -1.0f,
		0.0f, 3.0f, -6.0f, 3.0f,
		0.0f, 0.0f, 3.0f, -3.0f,
		0.0f, 0.0f, 0.0f, 1.0f
	);
    
	// b-spline
	Matrix4f B_bs(
		1.0f / 6.0f, -3.0f / 6.0f, 3.0f / 6.0f, -1.0f / 6.0f,
		4.0f / 6.0f, 0.0f / 6.0f, -6.0f / 6.0f, 3.0f / 6.0f,
		1.0f / 6.0f, 3.0f / 6.0f, 3.0f / 6.0f, -3.0f / 6.0f,
		0.0f / 6.0f, 0.0f / 6.0f, 0.0f / 6.0f, 1.0f / 6.0f
	);
}
    

Curve evalBezier( const vector< Vector3f >& P, unsigned steps )
{
    // Check
    if( P.size() < 4 || P.size() % 3 != 1 )
    {
        cerr << "evalBezier must be called with 3n+1 control points." << endl;
        exit( 0 );
    }

	Curve curve = Curve();
	CurvePoint cp;
	
	Vector3f prevB(0, 0, 1);

	// silmukka yleiselle käyrälle
	// otetaan 4 ekaa pistettä ja sitten siirrytään käyrän seuraavaan osaan
	// +3, koska ekan käyrän viimeinen ja seuraavan ensimmäinen pitää olla sama piste
	for (size_t j = 0; j < P.size()-1; j=j+3)
	{
		Matrix4f G(Vector4f(P[j], 0.0f), Vector4f(P[j+1], 0.0f), Vector4f(P[j+2], 0.0f), Vector4f(P[j+3], 0.0f));

		float t = 0.0f;
		float step = 1.0f / steps;

		for (size_t i = 0; i <= steps; i++)
		{
			Vector4f Tt(1, t, t*t, t*t*t);
			Vector4f dTt(0, 1, 2*t, 3*t*t);
			cp.V = (G*B*Tt).xyz();
			cp.T = (G*B*dTt).xyz().normalized();

			cp.N = Vector3f::cross(prevB, cp.T).normalized();
			cp.B = Vector3f::cross(cp.T, cp.N).normalized();

			//Tällä saadaan cp.N ristituloon Bi-1
			prevB = cp.B;
			curve.push_back(cp);
			t = t + step;
		}
	}

    cerr << "\t>>> evalBezier has been called with the following input:" << endl;

    cerr << "\t>>> Control points (type vector< Vector3f >): "<< endl;
    for( unsigned i = 0; i < P.size(); ++i )
    {
        cerr << "\t>>> " << P[i] << endl;
    }

    cerr << "\t>>> Steps (type steps): " << steps << endl;

    return curve;
}

Curve evalBspline( const vector< Vector3f >& P, unsigned steps )
{
    // Check
    if( P.size() < 4 )
    {
        cerr << "evalBspline must be called with 4 or more control points." << endl;
        exit( 0 );
    }

	cerr << "\t>>> evalBSpline has been called with the following input:" << endl;

	cerr << "\t>>> Control points (type vector< Vector3f >): " << endl;
	for (unsigned i = 0; i < P.size(); ++i)
	{
		cerr << "\t>>> " << P[i] << endl;
	}

	cerr << "\t>>> Steps (type steps): " << steps << endl;

	Curve curve = Curve();
	CurvePoint cp;

	Vector3f prevB(0, 0, 1);

	for (size_t j = 0; j + 3 < P.size(); j++)
	{
		Matrix4f G(Vector4f(P[j], 0.0f), Vector4f(P[j + 1], 0.0f), Vector4f(P[j + 2], 0.0f), Vector4f(P[j + 3], 0.0f));

		float t = 0.0f;
		float step = 1.0f / steps;

		for (size_t i = 0; i <= steps; i++)
		{
			Vector4f Tt(1, t, t*t, t*t*t);
			Vector4f dTt(0, 1, 2 * t, 3 * t*t);
			cp.V = (G*B_bs*Tt).xyz();
			cp.T = (G*B_bs*dTt).xyz().normalized();

			cp.N = Vector3f::cross(prevB, cp.T).normalized();
			cp.B = Vector3f::cross(cp.T, cp.N).normalized();

			//Tällä saadaan cp.N ristituloon Bi-1
			prevB = cp.B;

			curve.push_back(cp);
			t = t + step;
		}
	}
    return curve;
}

Curve evalCircle( float radius, unsigned steps )
{
    // This is a sample function on how to properly initialize a Curve
    // (which is a vector< CurvePoint >).
    
    // Preallocate a curve with steps+1 CurvePoints
    Curve R( steps+1 );

    // Fill it in counterclockwise
    for( unsigned i = 0; i <= steps; ++i )
    {
        // step from 0 to 2pi
        float t = 2.0f * M_PI * float( i ) / steps;

        // Initialize position
        // We're pivoting counterclockwise around the y-axis
        R[i].V = radius * Vector3f( cos(t), sin(t), 0 );
        
        // Tangent vector is first derivative
        R[i].T = Vector3f( -sin(t), cos(t), 0 );
        
        // Normal vector is second derivative
        R[i].N = Vector3f( -cos(t), -sin(t), 0 );

        // Finally, binormal is facing up.
        R[i].B = Vector3f( 0, 0, 1 );
    }

    return R;
}

void drawCurve( const Curve& curve, float framesize )
{
    // Save current state of OpenGL
    glPushAttrib( GL_ALL_ATTRIB_BITS );

    // Setup for line drawing
    glDisable( GL_LIGHTING ); 
    glColor4f( 1, 1, 1, 1 );
    glLineWidth( 1 );
    
    // Draw curve
    glBegin( GL_LINE_STRIP );
    for( unsigned i = 0; i < curve.size(); ++i )
    {
        glVertex( curve[ i ].V );
    }
    glEnd();

    glLineWidth( 1 );

    // Draw coordinate frames if framesize nonzero
    if( framesize != 0.0f )
    {
        Matrix4f M;

        for( unsigned i = 0; i < curve.size(); ++i )
        {
            M.setCol( 0, Vector4f( curve[i].N, 0 ) );
            M.setCol( 1, Vector4f( curve[i].B, 0 ) );
            M.setCol( 2, Vector4f( curve[i].T, 0 ) );
            M.setCol( 3, Vector4f( curve[i].V, 1 ) );

            glPushMatrix();
            glMultMatrixf( M );
            glScaled( framesize, framesize, framesize );
            glBegin( GL_LINES );
            glColor3f( 1, 0, 0 ); glVertex3d( 0, 0, 0 ); glVertex3d( 1, 0, 0 );
            glColor3f( 0, 1, 0 ); glVertex3d( 0, 0, 0 ); glVertex3d( 0, 1, 0 );
            glColor3f( 0, 0, 1 ); glVertex3d( 0, 0, 0 ); glVertex3d( 0, 0, 1 );
            glEnd();
            glPopMatrix();
        }
    }
    
    // Pop state
    glPopAttrib();
}

