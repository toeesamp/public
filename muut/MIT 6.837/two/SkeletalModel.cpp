#include "SkeletalModel.h"

#include <FL/Fl.H>

#ifndef M_PI
#define M_PI 3.14159265358979f
#endif

using namespace std;

void SkeletalModel::load(const char *skeletonFile, const char *meshFile, const char *attachmentsFile)
{
	loadSkeleton(skeletonFile);

	m_mesh.load(meshFile);
	m_mesh.loadAttachments(attachmentsFile, m_joints.size());

	computeBindWorldToJointTransforms();
	updateCurrentJointToWorldTransforms();
}

void SkeletalModel::draw(Matrix4f cameraMatrix, bool skeletonVisible)
{
	// draw() gets called whenever a redraw is required
	// (after an update() occurs, when the camera moves, the window is resized, etc)

	m_matrixStack.clear();
	m_matrixStack.push(cameraMatrix);

	if( skeletonVisible )
	{
		drawJoints();

		drawSkeleton();
	}
	else
	{
		// Clear out any weird matrix we may have been using for drawing the bones and revert to the camera matrix.
		glLoadMatrixf(m_matrixStack.top());

		// Tell the mesh to draw itself.
		m_mesh.draw();
	}
}

void SkeletalModel::loadSkeleton(const char* filename)
{
	// Load the skeleton from file here.

	string line;
	ifstream file (filename, ifstream::in);

	if (file.is_open())
	{
		while (getline(file, line))
		{
			stringstream ss(line);

			Joint *joint = new Joint;

			Vector3f transVector;

			int index;

			ss >> transVector[0];
			ss >> transVector[1];
			ss >> transVector[2];

			ss >> index;

			joint->transform = Matrix4f::translation(transVector);

			m_joints.push_back(joint);


			if (index >= 0)
			{
				m_joints[index]->children.push_back(joint);
			}

			// Tiedostomuodossa on m‰‰ritelty rootjointin indeksiksi -1
			if (index == -1)
			{
				m_rootJoint = joint;
			}
		}
	}
}

void SkeletalModel::drawJoint(Joint *currentJoint)
{
	m_matrixStack.push(currentJoint->transform);
	glLoadMatrixf(m_matrixStack.top());
	glutSolidSphere(0.025f, 12, 12);

	// Piirret‰‰n kaikki nivelet rekursiivisesti
	for (size_t i = 0; i < currentJoint->children.size(); i++)
	{
		drawJoint(currentJoint->children[i]);
	}
	m_matrixStack.pop();
	glLoadMatrixf(m_matrixStack.top());
}

 
void SkeletalModel::drawJoints( )
{
	// Draw a sphere at each joint. You will need to add a recursive helper function to traverse the joint hierarchy.
	//
	// We recommend using glutSolidSphere( 0.025f, 12, 12 )
	// to draw a sphere of reasonable size.
	//
	// You are *not* permitted to use the OpenGL matrix stack commands
	// (glPushMatrix, glPopMatrix, glMultMatrix).
	// You should use your MatrixStack class
	// and use glLoadMatrix() before your drawing call.

	drawJoint(m_rootJoint);
}


void SkeletalModel::drawBone(Joint *currentJoint)
{
	m_matrixStack.push(currentJoint->transform);
	glLoadMatrixf(m_matrixStack.top());

	// Luuta ei piirret‰ jos ollaan root jointissa
	if (currentJoint != this->m_rootJoint)
	{
		// Selvitet‰‰n nykyisen nivelen ja sen parentin v‰linen siirto transformaatiomatriisista
		// ja otetaan siit‰ negaatio -> saadaan vektori, jonka suuntaiseksi luu pit‰‰ k‰‰nt‰‰
		Vector3f parentOffsetVector = currentJoint->transform.getCol(3).xyz();
		parentOffsetVector.negate();

		// Lasketaan et‰isyys nykyisen nivelen ja sen parentin v‰lill‰
		float distanceToParent = parentOffsetVector.abs();

		// Z-akselin suuntainen vektori, jonka avulla selvitet‰‰n luun k‰‰ntˆakseli ja -kulma
		Vector3f zAxis(0.0f, 0.0f, distanceToParent);

		// Apuvektorin ja parentOffsetin ristitulona saadaan akseli, jonka ymp‰ri luuta pit‰‰ pyˆritt‰‰
		Vector3f rotationAxis = Vector3f::cross(zAxis, parentOffsetVector);
		
		// Ottamalla arkustangentti pisteest‰, joka muodostuu pyˆr‰ytysakselin pituudesta ja apuvektorin, sek‰ parentOffsetin pistetulosta
		// saadaan tarvittava pyˆr‰ytyskulma radiaaneina (jonka j‰lkeen se muutetaan asteiksi)
		float rotationAngleDegrees = atan2(rotationAxis.abs(), Vector3f::dot(zAxis, parentOffsetVector))*(180.0 / M_PI);

		// Tehd‰‰n kaikki tarvittavat transformaatiot (pyˆritys, siirto ja skaalaus) oikeassa j‰rjestyksess‰, jonka j‰lkeen piirret‰‰n luu
		glRotatef(rotationAngleDegrees, rotationAxis.x(), rotationAxis.y(), rotationAxis.z());

		// Siirret‰‰n luu l‰htem‰‰n jointin keskipisteest‰ z-akselin suuntaan
		glTranslatef(0.0f, 0.0f, distanceToParent / 2.0f);

		// Skaalataan luu j‰rkev‰n levyiseksi ja sen pituiseksi, ett‰ se p‰‰ttyy parentJointin keskelle
		glScalef(0.05f, 0.05f, distanceToParent);

		glutSolidCube(1.0f);
	}

	// Piirret‰‰n kaikki luut rekursiivisesti
	for (size_t i = 0; i < currentJoint->children.size(); i++)
	{
		drawBone(currentJoint->children[i]);
	}

	m_matrixStack.pop();
	glLoadMatrixf(m_matrixStack.top());
}

void SkeletalModel::drawSkeleton( )
{
	// Draw boxes between the joints. You will need to add a recursive helper function to traverse the joint hierarchy.
	drawBone(m_rootJoint);
}

void SkeletalModel::setJointTransform(int jointIndex, float rX, float rY, float rZ)
{
	// Set the rotation part of the joint's transformation matrix based on the passed in Euler angles.
}


void SkeletalModel::computeBindWorldToJointTransforms()
{
	// 2.3.1. Implement this method to compute a per-joint transform from
	// world-space to joint space in the BIND POSE.
	//
	// Note that this needs to be computed only once since there is only
	// a single bind pose.
	//
	// This method should update each joint's bindWorldToJointTransform.
	// You will need to add a recursive helper function to traverse the joint hierarchy.
}

void SkeletalModel::updateCurrentJointToWorldTransforms()
{
	// 2.3.2. Implement this method to compute a per-joint transform from
	// joint space to world space in the CURRENT POSE.
	//
	// The current pose is defined by the rotations you've applied to the
	// joints and hence needs to be *updated* every time the joint angles change.
	//
	// This method should update each joint's bindWorldToJointTransform.
	// You will need to add a recursive helper function to traverse the joint hierarchy.
}

void SkeletalModel::updateMesh()
{
	// 2.3.2. This is the core of SSD.
	// Implement this method to update the vertices of the mesh
	// given the current state of the skeleton.
	// You will need both the bind pose world --> joint transforms.
	// and the current joint --> world transforms.
}

