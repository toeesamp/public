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

	//Luodaan group halutulla määrällä objekteja
	Group(int num_objects) 
	{
		objects.resize(num_objects);
	}

	~Group() 
	{

	}


	//Käydään läpi kaikki grooupin sisältämät objektit ja kutsutaan niiden
	//omaa intersectiä. Palautetaan true jos yksikin leikkaus löytyy
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


	//Lisätään grouppiin objekti haluttuun indeksiin
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
	
