#include "GL/freeglut.h"
#include <cmath>
#include <iostream>
#include <sstream>
#include <vector>
#include "vecmath.h"
using namespace std;

// @author Tommi Sampo

// Itsearvio 5

// Toteutetut lisäominaisuudet:
// -Kappaleen pyörittäminen r-näppäimellä
// -Kappaleen esittäminen display listillä
// -Sulava värinvaihto
// -Kameran kontrollointi hiirellä (kääntäminen ja zoomaus)

//Jos tekisin uudestaan niin ensinäkin tekisin vektorien erotuksen
//sille tarkoitetulla valmiilla funktiolla, normalisoisin sen //erotuksen ja käyttäisin vaihtamiseen lerppiä.

// Globals

// Maximum buffer size for lines in the .obj file
CONST int MAX_BUFFER_SIZE = 256;

// This is the list of points (3D vectors)
vector<Vector3f> vecv;

// This is the list of normals (also 3D vectors)
vector<Vector3f> vecn;

// This is the list of faces (indices into vecv and vecn)
//     a      /      b     /     c             d      /     e      /     f              g     /    h       /    i
// vecf[i][0] / vecf[i][1] / vecf[i][2]    vecf[i][3] / vecf[i][4] / vecf[i][5]    vecf[i][6] / vecf[i][7] / vecf[i][8]                         
vector<vector<unsigned> > vecf;


// You will need more global variables to implement color and position changes

// Offset for the light source from original position
GLfloat Lt0posOffset[] = { 0.0f, 0.0f };

int timerInterval = 20;

// Globals for color transition

// Index for the diffColors table
int colorIndex = 0;
// Amount of different colors (rows in diffColors table)
int colorsAmount = 0;

// Components for single step of color transition
GLfloat colorTransitionStep[3] = { 0, 0, 0 };

int firstRun = true;
int colorTransition = false;
int colorTransitionStarted = false;

float colorTransitionSteps = 30;
int colorTransitionStepsTaken = 0;

// Next color to be displayed
GLfloat nextColor[4] = { 0, 0, 0 , 1 };

// Globals for rotation
int rotating = false;

float rotateAngle = 0;
float rotateAngleIncrement = 1;

// Globals for camera position
float cameraDistanceOffset = 0.0f;

// The previous x-coordinate of the mouse (used for mouse rotation)
int previousX = 0;


// Draws triangles from vecv, vecn and vecf (not used in final version)
void drawInput()
{
	for (size_t i = 0; i < vecf.size(); i++)
	{
		glBegin(GL_TRIANGLES);
		glNormal3d(vecn[(vecf[i][2]) - 1][0], vecn[(vecf[i][2]) - 1][1], vecn[(vecf[i][2]) - 1][2]);
		glVertex3d(vecv[(vecf[i][0]) - 1][0], vecv[(vecf[i][0]) - 1][1], vecv[(vecf[i][0]) - 1][2]);
		glNormal3d(vecn[(vecf[i][5]) - 1][0], vecn[(vecf[i][5]) - 1][1], vecn[(vecf[i][5]) - 1][2]);
		glVertex3d(vecv[(vecf[i][3]) - 1][0], vecv[(vecf[i][3]) - 1][1], vecv[(vecf[i][3]) - 1][2]);
		glNormal3d(vecn[(vecf[i][8]) - 1][0], vecn[(vecf[i][8]) - 1][1], vecn[(vecf[i][8]) - 1][2]);
		glVertex3d(vecv[(vecf[i][6]) - 1][0], vecv[(vecf[i][6]) - 1][1], vecv[(vecf[i][6]) - 1][2]);
		glEnd();
	}
}


// Generates a display list from vecv, vecn and vecf
void generateDisplayList()
{
	GLuint index = glGenLists(1);

	glNewList(index, GL_COMPILE);
	for (size_t i = 0; i < vecf.size(); i++)
	{
		glBegin(GL_TRIANGLES);
		glNormal3d(vecn[(vecf[i][2]) - 1][0], vecn[(vecf[i][2]) - 1][1], vecn[(vecf[i][2]) - 1][2]);
		glVertex3d(vecv[(vecf[i][0]) - 1][0], vecv[(vecf[i][0]) - 1][1], vecv[(vecf[i][0]) - 1][2]);
		glNormal3d(vecn[(vecf[i][5]) - 1][0], vecn[(vecf[i][5]) - 1][1], vecn[(vecf[i][5]) - 1][2]);
		glVertex3d(vecv[(vecf[i][3]) - 1][0], vecv[(vecf[i][3]) - 1][1], vecv[(vecf[i][3]) - 1][2]);
		glNormal3d(vecn[(vecf[i][8]) - 1][0], vecn[(vecf[i][8]) - 1][1], vecn[(vecf[i][8]) - 1][2]);
		glVertex3d(vecv[(vecf[i][6]) - 1][0], vecv[(vecf[i][6]) - 1][1], vecv[(vecf[i][6]) - 1][2]);
		glEnd();
	}
	glEndList();

}


// Timer function for smooth color transition
void changeColor(int t)
{
	glutPostRedisplay();
}


// Rotates the model every timerInterval by rotateAngleIncrement
void rotateModel(int t)
{

	if (rotating)
	{
		if (rotateAngle < 360)
		{
			rotateAngle = rotateAngle + rotateAngleIncrement;
		}
		else
		{
			rotateAngle = 0;
		}
		glutTimerFunc(timerInterval, rotateModel, 0);

	}
	glutPostRedisplay();
}


// These are convenience functions which allow us to call OpenGL 
// methods on Vec3d objects
inline void glVertex(const Vector3f &a)
{
	glVertex3fv(a);
}

inline void glNormal(const Vector3f &a)
{
	glNormal3fv(a);
}


// This function is called whenever a "Normal" key press is received.
void keyboardFunc(unsigned char key, int x, int y)
{
	switch (key)
	{
	case 27: // Escape key
		exit(0);
		break;
	case'r':
		rotating = !rotating;
		rotateModel(0);
		break;
	case 'c':
		if (!colorTransition)
		{
			colorTransition = true;
			colorTransitionStarted = false;
		}
		break;
	default:
		cout << "Unhandled key press " << key << "." << endl;
	}

	// this will refresh the screen so that the user sees the color change
	glutPostRedisplay();
}


// This function is called whenever a "Special" key press is received.
// Right now, it's handling the arrow keys.
void specialFunc(int key, int x, int y)
{
	switch (key)
	{
	case GLUT_KEY_UP:
		Lt0posOffset[1] = Lt0posOffset[1] - 0.5;
		break;
	case GLUT_KEY_DOWN:
		Lt0posOffset[1] = Lt0posOffset[1] + 0.5;
		break;
	case GLUT_KEY_LEFT:
		Lt0posOffset[0] = Lt0posOffset[0] + 0.5;
		break;
	case GLUT_KEY_RIGHT:
		Lt0posOffset[0] = Lt0posOffset[0] - 0.5;
		break;
	}

	// this will refresh the screen so that the user sees the light position
	glutPostRedisplay();
}



// Allows model rotation using mouse
void motionFunc(int x, int y)
{
	rotateAngle = rotateAngle - ((previousX - x) * 0.2f);
	previousX = x;
	glutPostRedisplay();
}



// Saves the x-coordinate where the left mouse button is first pressed
void mouseFunc(int button, int state, int x, int y)
{

	if (state == GLUT_DOWN)
	{
		previousX = x;
	}

}



// Changes the distance of the camera from the object depending
// on mouse scroll wheel direction
void mouseWheelFunc(int wheel, int direction, int x, int y)
{
	if (wheel == 0)
	{
		switch (direction)
		{
		case 1:
			if (cameraDistanceOffset > -3.0f)
			{
				cameraDistanceOffset = cameraDistanceOffset - 0.5f;
			}
			glutPostRedisplay();
			break;
		case -1:
			if (cameraDistanceOffset < 10.0f)
			{
				cameraDistanceOffset = cameraDistanceOffset + 0.5f;
			}
			glutPostRedisplay();
			break;
		}
	}
}


// This function is responsible for displaying the object.
void drawScene(void)
{
	int i;

	// Clear the rendering window
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Rotate the image
	glMatrixMode(GL_MODELVIEW);  // Current matrix affects objects positions
	glLoadIdentity();              // Initialize to the identity


	// Position the camera at [0,0,5], looking at [0,0,0],
	// with [0,1,0] as the up direction.
	gluLookAt(0.0, 0.0, 5.0 + cameraDistanceOffset,
		0.0, 0.0, 0.0,
		0.0, 1.0, 0.0);

	// Set material properties of object

	// Here are some colors you might use - feel free to add more
	GLfloat diffColors[4][4] = { {0.5, 0.5, 0.9, 1.0},
								 {0.9, 0.5, 0.5, 1.0},
								 {0.5, 0.9, 0.3, 1.0},
								 {0.3, 0.8, 0.9, 1.0} };

	// Things that only need to be set on the first run
	if (firstRun)
	{
		for (size_t i = 0; i <= 2; i++)
		{
			nextColor[i] = diffColors[0][i];
		}
		// Number of rows in diffColors
		colorsAmount = sizeof diffColors / sizeof diffColors[0];

		firstRun = false;
	}


	if (colorTransition)
	{
		if (!colorTransitionStarted)
		{
			int nextColorIndex = 0;

			if (colorIndex < colorsAmount - 1)
			{
				nextColorIndex = colorIndex + 1;
			}
			else
			{
				nextColorIndex = 0;
			}

			// Divide transition into steps
			for (size_t i = 0; i <= 2; i++)
			{
				colorTransitionStep[i] = (diffColors[nextColorIndex][i] - diffColors[colorIndex][i]) / colorTransitionSteps;
			}

			colorIndex = nextColorIndex;
			colorTransitionStarted = true;
		}

		if (colorTransitionStepsTaken < colorTransitionSteps)
		{
			// The color change itself
			for (size_t i = 0; i <= 2; i++)
			{
				nextColor[i] = nextColor[i] + colorTransitionStep[i];
			}
			colorTransitionStepsTaken++;
		}
		else
		{
			// Ensuring that the final color is the one defined in diffColors (eliminating floating point errors)
			for (size_t i = 0; i <= 2; i++)
			{
				nextColor[i] = diffColors[colorIndex][i];
			}
			colorTransitionStepsTaken = 0;
			colorTransition = false;
		}

		glutTimerFunc(timerInterval, changeColor, 0);
	}

	// Here we use the first color entry as the diffuse color
	//glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, diffColors[colorIndex]);
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, nextColor);

	// Define specular color and shininess
	GLfloat specColor[] = { 1.0, 1.0, 1.0, 1.0 };
	GLfloat shininess[] = { 100.0 };

	// Note that the specular color and shininess can stay constant
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specColor);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, shininess);

	// Set light properties

	// Light color (RGBA)
	GLfloat Lt0diff[] = { 1.0,1.0,1.0,1.0 };
	// Light position
	GLfloat Lt0pos[] = { 1.0f - Lt0posOffset[0], 1.0f - Lt0posOffset[1], 5.0f, 1.0f };

	glLightfv(GL_LIGHT0, GL_DIFFUSE, Lt0diff);
	glLightfv(GL_LIGHT0, GL_POSITION, Lt0pos);

	// Rotate object
	glRotated(rotateAngle, 0, 1, 0);

	// This GLUT method draws a teapot.  You should replace
	// it with code which draws the object you loaded.
	//glutSolidTeapot(1.0);
	//drawInput();

	// Draw from the display list
	glCallList(1);

	// Dump the image to the screen.
	glutSwapBuffers();
}


// Initialize OpenGL's rendering modes
void initRendering()
{
	glEnable(GL_DEPTH_TEST);   // Depth testing must be turned on
	glEnable(GL_LIGHTING);     // Enable lighting calculations
	glEnable(GL_LIGHT0);       // Turn on light #0.
}


// Called when the window is resized
// w, h - width and height of the window in pixels.
void reshapeFunc(int w, int h)
{
	// Always use the largest square viewport possible
	if (w > h) {
		glViewport((w - h) / 2, 0, h, h);
	}
	else {
		glViewport(0, (h - w) / 2, w, w);
	}

	// Set up a perspective view, with square aspect ratio
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	// 50 degree fov, uniform aspect ratio, near = 1, far = 100
	gluPerspective(50.0, 1.0, 1.0, 100.0);
}


// Reads vertices, normals and faces from an obj-file and places them in
// vecv, vecn and vecf, respectively.
void loadInput()
{
	char buffer[MAX_BUFFER_SIZE];
	int i = 1;
	while (cin.getline(buffer, MAX_BUFFER_SIZE)) {

		stringstream ss(buffer);

		Vector3f v;
		string s;

		ss >> s;

		if (s == "v")
		{
			ss >> v[0] >> v[1] >> v[2];
			vecv.push_back(v);
		}
		else if (s == "vn")
		{
			ss >> v[0] >> v[1] >> v[2];
			vecn.push_back(v);
		}
		else if (s == "f")
		{
			vector<unsigned> vf(9);
			//												a    /   b   /   c      d    /  e    /  f        g   /   h   /  i   
			sscanf(buffer, "f %i/%i/%i %i/%i/%i %i/%i/%i", &vf[0], &vf[1], &vf[2], &vf[3], &vf[4], &vf[5], &vf[6], &vf[7], &vf[8]);
			vecf.push_back(vf);
		}
	}
}



// Main routine.
// Set up OpenGL, define the callbacks and start the main loop
int main(int argc, char** argv)
{
	loadInput();

	glutInit(&argc, argv);

	// We're going to animate it, so double buffer 
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);

	// Initial parameters for window position and size
	glutInitWindowPosition(200, 200);
	glutInitWindowSize(720, 720);
	glutCreateWindow("Assignment 0");

	// Initialize OpenGL parameters.
	initRendering();

	// Create display list
	generateDisplayList();

	// Set up callback functions for key presses
	glutKeyboardFunc(keyboardFunc);		// Handles "normal" ascii symbols
	glutSpecialFunc(specialFunc);		// Handles "special" keyboard keys
	glutMouseFunc(mouseFunc);			// Handles mouse buttons
	glutMouseWheelFunc(mouseWheelFunc); // Handles mouse wheel scrolling
	glutMotionFunc(motionFunc);			// Handles mouse movement

	 // Set up the callback function for resizing windows
	glutReshapeFunc(reshapeFunc);

	// Call this whenever window needs redrawing
	glutDisplayFunc(drawScene);

	// Start the main loop.  glutMainLoop never returns.
	glutMainLoop();

	return 0;	// This line is never reached.
}
