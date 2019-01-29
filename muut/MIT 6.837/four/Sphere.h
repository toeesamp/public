#ifndef SPHERE_H
#define SPHERE_H

#include "Object3D.h"
#include <vecmath.h>
#include <cmath>
#include <algorithm>

#include <iostream>
using namespace std;

class Sphere: public Object3D
{
public:
	Sphere(){ 
		//unit ball at the center
		this->center = Vector3f(0.0f);
		this->radius = 1;
	}


	Sphere( Vector3f p_center , float p_radius , Material* p_material ):Object3D(p_material)
	{
		this->center = p_center;
		this->radius = p_radius;
		this->material = p_material;
	}
	

	~Sphere(){}


	virtual bool intersect( const Ray& r , Hit& h , float tmin)
	{
		float closestIntersect = FLT_MAX;

		Vector3f rayOrigin = r.getOrigin();
		Vector3f rayDirection = r.getDirection();


		//Muodostetaan s�teen ja pallon leikkauksen yht�l�
		float a = Vector3f::dot(rayDirection, rayDirection);
		float b = Vector3f::dot(2.0f * rayDirection, rayOrigin);
		float c = Vector3f::dot(rayOrigin, rayOrigin) - pow(this->radius, 2);

		//Selvitet��n osuuko s�de palloon ratkaisemalla toisen asteen yht�l�

		//Yht�l�n diskriminantti
		//float discriminant = pow(b, 2) - 4.0f * a * c;
		float discriminant = b*b - 4.0f * a * c;

		//Jos diskriminantti on pienempi kuin nolla niin yht�l�ll� ei ole ratkaisuja
		if (discriminant < 0.0f)
		{
			return false;
		}

		//Jos diskriminantti on tasan nolla niin s�de sivuaa palloa ja yht�l�ll� on yksi ratkaisu
		if (discriminant == 0.0f)
		{
			closestIntersect = -b / (2.0f * a);
		}

		//Jos diskriminantti on suurempi kuin nolla niin s�de menee pallon l�pi ja
		//yht�l�ll� on kaksi ratkaisua
		else
		{ 
			float solution1 = (-b + sqrt(discriminant)) / (2.0f * a);
			float solution2 = (-b - sqrt(discriminant)) / (2.0f * a);

			//Tarkistetaan kumpi ratkaisuista on pienempi
			closestIntersect = min(solution1, solution2);
			//Tarkistetaan onko l�ydetty osuma l�hemp�n�, kuin muut l�ydetyt osumat
			if (closestIntersect < h.getT() && closestIntersect > tmin)
			{
				//Lasketaan pallon normaali osumakohdassa
				Vector3f intersectPoint = r.pointAtParameter(closestIntersect);
				Vector3f intersectPointNormal = intersectPoint.normalized();

				//Asetetaan hitin uudet arvot
				h.set(closestIntersect, material, intersectPointNormal);
			}
			return true;
		}
		return false;
	}

protected:
	Vector3f center;
	float radius;
	Material* material;
};

#endif
