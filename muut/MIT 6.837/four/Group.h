#ifndef GROUP_H
#define GROUP_H


#include "Object3D.h"
#include "Ray.h"
#include "Hit.h"
#include <iostream>

using  namespace std;

class Group:public Object3D
{
public:

	Group() {

	}

	//Luodaan group halutulla m��r�ll� objekteja
	Group(int num_objects) 
	{
		objects.resize(num_objects);
	}

	~Group() 
	{

	}


	//K�yd��n l�pi kaikki grooupin sis�lt�m�t objektit ja kutsutaan niiden
	//omaa intersecti�. Palautetaan true jos yksikin leikkaus l�ytyy
	virtual bool intersect(const Ray& r, Hit& h, float tmin) 
	{
		bool intersectFound = false;

		for (Object3D* object : objects)
		{
			if (object->intersect(r, h, tmin))
			{
				intersectFound = true;
			}
		}
		return intersectFound;
	}


	//Lis�t��n grouppiin objekti haluttuun indeksiin
	void addObject(int index, Object3D* obj)
	{
		this->objects[index] = obj;
	}


	//Palauttaa groupin koon
	int getGroupSize() 
	{
		return objects.size();
	}

private:
	std::vector< Object3D* > objects;
};

#endif
	
