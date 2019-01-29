#ifndef CAMERA_H
#define CAMERA_H

#include "Ray.h"
#include <vecmath.h>
#include <float.h>
#include <cmath>


class Camera
{
public:
	//generate rays for each screen-space coordinate
	virtual Ray generateRay( const Vector2f& point , float aspectRatio) = 0 ; 
	
	virtual float getTMin() const = 0 ; 
	virtual ~Camera(){}
protected:
	Vector3f center; 
	Vector3f direction;
	Vector3f up;
	Vector3f horizontal;

};

class PerspectiveCamera: public Camera
{
public:

	//Luodaan perspektiivikamera haluttuun pisteeseen ja määritetään sille katsomissuunta, yläsuunta ja
	//katselukulma
	PerspectiveCamera(const Vector3f& center, const Vector3f& direction,const Vector3f& up , float angle)
	{
		this->center = center;
		this->direction = direction;
		this->up = up;
		this->angle = angle;
	}


	//Luodaan normalisoitu säde, joka lähtee kameran keskipisteestä parametrinä annettua pistettä kohti
	virtual Ray generateRay( const Vector2f& point, float aspectRatio){
		
		//Kameran uvw-kanta
		Vector3f w = this->direction;
		Vector3f u = Vector3f::cross(w, this->up);
		Vector3f v = Vector3f::cross(u, w);

		//Etäisyys näyttötasoon
		float distance = 1.0f / tan(angle / 2.0f);

		//Määritetään säteen lähtöpiste ja suunta
		Vector3f rayOrigin = this->center;
		Vector3f rayDirection = (point.x()*u + aspectRatio*point.y()*v + distance * w).normalized();

		return Ray(rayOrigin, rayDirection);
	}

	virtual float getTMin() const { 
		return 0.0f;
	}

private:
	Vector3f center;
	Vector3f direction;
	Vector3f up;
	float angle;
};

#endif //CAMERA_H
