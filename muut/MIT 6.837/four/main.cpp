#include <cassert>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <iostream>
#include <sstream>

#include "SceneParser.h"
#include "Image.h"
#include "Camera.h"
#include "VecUtils.h"
#include <string.h>

using namespace std;

float clamp(float input, float min, float max);
static const char* getArgument(int argc, char* argv[], const char* option);

#include "bitmap_image.hpp"


//K‰yd‰‰n argumenttilista l‰pi ja katsotaan lˆytyykˆ sielt‰ haluttua argumenttia (option).
//Jos lˆytyy niin palautetaan seuraava alkio eli argumentin arvo
static const char* getArgument(int argc, char* argv[], const char* option)
{
	if (option == NULL)
	{
		return 0;
	}
	for (int i = 1; i <= argc; i++)
	{
		if (argv[i] != NULL && strncmp(argv[i], option, 256) == 0)
		{
			return argv[i + 1];
		}
	}
	return 0;
}


//Clamppaa luvun minimin ja maksimin v‰lille
float clamp(float input, float min, float max)
{
	if (input < min)
	{
		return min;
	}
	else if (input > max)
	{
		return max;
	}
	return input;
}


int main(int argc, char* argv[])
{
	//parsetaan argumentit

	const char* parsedInput = getArgument(argc, argv, "-input");
	//std::cout << parsedInput << std::endl;

	const char* parsedWidth = getArgument(argc, argv, "-size");
	//std::cout << parsedWidth << std::endl;

	const char* parsedHeight = getArgument(argc, argv, parsedWidth);
	//std::cout << parsedHeight << std::endl;

	const char* parsedOutput = getArgument(argc, argv, "-output");
	//std::cout << parsedOutput << std::endl;

	const char* parsedDepthMin = getArgument(argc, argv, "-depth");
	//std::cout << parsedDepthMin << std::endl;


	const char* parsedDepthMax = NULL;
	const char* parsedDepthOutput = NULL;

	if (parsedDepthMin != NULL)
	{
		parsedDepthMax = getArgument(argc, argv, parsedDepthMin);
		//std::cout << parsedDepthMax << std::endl;

		parsedDepthOutput = getArgument(argc, argv, parsedDepthMax);
		//std::cout << parsedDepthOutput << std::endl;
	}
	
	const char* parsedNormalsOutput = getArgument(argc, argv, "-normals");
	//std::cout << parsedNormalsOutput << std::endl;



	string arguments;
	arguments.append(parsedWidth);
	arguments.append(" ");
	arguments.append(parsedHeight);
	arguments.append(" ");
	arguments.append(parsedDepthMin);
	arguments.append(" ");
	arguments.append(parsedDepthMax);


	stringstream ss(arguments);
	int width, height, depthMin, depthMax;
	char* input = (char*)parsedInput;

	ss >> width;
	ss >> height;
	ss >> depthMin;
	ss >> depthMax;


	/*Ensin parsetaan scene inputin m‰‰rittelem‰st‰ tiedostosta
	 *Seuraavaksi loopataan jokaisen pikselin yli ja l‰hetet‰‰n ray sen suuntaan
	 *Peruskuva (image, pixelColor) v‰ritet‰‰n "normaalisti" input-tiedoston m‰‰ritelmien mukaan
	 *
	 *Syvyyskuva (depthImage, depthPixelColor) v‰ritet‰‰n s‰teen osumaet‰isyyden mukaan siten, 
	 *ett‰ l‰hin osuma on valkoinen ja v‰ri tummenee osuman menness‰ kauemmas.
	 *
	 *Normaaleiden kuva (normalsImage, normalsPixelColor) v‰ritet‰‰n osumakohtien normaaleiden mukaan
	 *(osumapinnan suhteen) siten ett‰, x on punainen, y vihre‰ ja z sininen
	 *
	 *Pikseli, jota vastaava s‰de ei osu mihink‰‰n v‰rj‰t‰‰n peruskuvassa backgroundColorin mukaan 
	 *ja muissa kuvissa mustaksi.
	 */
	SceneParser scene(input);
	Camera* camera = scene.getCamera();
	Group* group = scene.getGroup();

	Image image(width, height);
	Image depthImage(width, height);
	Image normalsImage(width, height);

	Vector3f pixelColor(0.0f, 0.0f, 0.0f);
	Vector3f depthPixelColor(0.0f, 0.0f, 0.0f);
	Vector3f normalsPixelColor(0.0f, 0.0f, 0.0f);

	Vector3f backgroundColor = scene.getBackgroundColor();

	//Selvitet‰‰n kuvasuhde
	float aspectRatio = (float)image.Height() / (float)image.Width();
	
	for (size_t i = 0; i < image.Width(); i++)
	{
		for (size_t j = 0; j < image.Height(); j++)
		{
			//Skaalataan annetut kuvan mitat v‰lille [-1, 1]
			float scaledX = -1.0f + 2.0f * (float)i / (float)image.Width();
			float scaledY = -1.0f + 2.0f * (float)j / (float)image.Height();

			//Generoidaan s‰de skaalattuja koordinaatteja kohti
			Ray ray = camera->generateRay(Vector2f(scaledX, scaledY), aspectRatio);
			Hit hit = Hit();


			//Selvitet‰‰n osuuko s‰de mihink‰‰n groupin sis‰lt‰m‰‰n objektiin 
			//ja v‰ritet‰‰n pikseli sen mukaan
			if (group->intersect(ray, hit, 0.0f))
			{
				//Peruskuvan v‰ri

				if (scene.getNumLights() != 1)
				{
					std::cout << "WARNING: the current implementation is only for 1 light source " << std::endl;
				}

				Material* material = hit.getMaterial();
				Light* light = scene.getLight(0);
				Vector3f ambientLight = scene.getAmbientLight();
				Vector3f diffuseColor = material->getDiffuseColor();

				Vector3f lightDirection;
				Vector3f lightColor;
				float distance;

				Vector3f intersectPoint = ray.getOrigin() + ray.getDirection() * hit.getT();

				//Selvitet‰‰n osumakohdan valaistusolosuhteet
				light->getIllumination(intersectPoint, lightDirection, lightColor, distance);

				//Varjostetaan pikseli valaistuksen mukaan
				Vector3f shadedColor = material->Shade(ray, hit, lightDirection, lightColor );


				//T‰ss‰ myˆs summattaisiin kaikkien valojen shaded v‰rit, mutta toistaiseksi niit‰ on vain yksi
				pixelColor = ambientLight * diffuseColor + shadedColor;


				//Syvyyskuvan v‰ri
				float clampedHit = clamp(hit.getT(), depthMin, depthMax);

				//Syvyyden arvojen v‰lin pituus
				float depthScaleRange = (float)depthMax - (float)depthMin;

				//Siirret‰‰n syvyyden arvo l‰htem‰‰n nollasta
				float depthColorValue = clampedHit - (float)depthMin;

				//Skaalataan syvyyden arvo v‰lille [0,1] pikseleiden v‰rityst‰ varten
				float scaledDepthColorValue = depthColorValue / depthScaleRange;

				//V‰ritet‰‰n syvyyskuvan pikselit siten, ett‰ l‰hin kohta on valkoinen 
				//ja v‰ri muuttuu sit‰ tummemmaksi, mit‰ kauemmas menn‰‰n
				depthPixelColor = Vector3f(1.0f - scaledDepthColorValue, 1.0f - scaledDepthColorValue, 1.0f - scaledDepthColorValue);


				//Normaalikuvan v‰ri
				// x -> red
				// y -> green
				// z -> blue

				//Skaalataan normaaleiden arvot v‰lilt‰ [-1,1] v‰lille [0,1]
				
				//Siirret‰‰n arvot v‰lille [0,2] ja jaetaan kahdella
				// -> arvot skaalautuvat v‰lille [0,1]
				normalsPixelColor.x() = (hit.getNormal().x() + 1.0f) / 2.0f;
				normalsPixelColor.y() = (hit.getNormal().y() + 1.0f) / 2.0f;
				normalsPixelColor.z() = (hit.getNormal().z() + 1.0f) / 2.0f;
			}
			else
			{
				pixelColor = backgroundColor;
				depthPixelColor = Vector3f(0.0f, 0.0f, 0.0f);
				normalsPixelColor = Vector3f(0.0f, 0.0f, 0.0f);
			}	

			//Asetetaan kullekin kuvalle kuuluvan pikselin v‰ri
			image.SetPixel(i, j, pixelColor);
			depthImage.SetPixel(i, j, depthPixelColor);
			normalsImage.SetPixel(i, j, normalsPixelColor);
		}
	}

	//Tallennetaan kuvat
	image.SaveImage(parsedOutput);
	if (parsedDepthOutput != NULL)
	{
		depthImage.SaveImage(parsedDepthOutput);
	}
	if (parsedNormalsOutput != NULL)
	{
		normalsImage.SaveImage(parsedNormalsOutput);
	}
	return 0;
}

