#include <iostream>
#include <cstdlib>
#include <ctime>

#define MAXN 30000000
#define A (-1.0)
#define B (1.0)

//säieryhmän koko
#define LOCAL_SIZE 1024

//säieryhmien määrä
//#define WG_COUNT (MAXN/LOCAL_SIZE+1)


//ydin, jolle annetaan kaksi n kokoista vektoria x ja y, sekä
//liukuluku m, joille suoritetaan axpy-operaatio
__global__ void axpy(double *x, double *y, double m, int n) {

	const int global_id = blockIdx.x * blockDim.x + threadIdx.x;


	if(global_id < n)
		y[global_id] = m * x[global_id] + y[global_id];

}


//tarkistetaan onko doublet samoja
bool double_equals( double a, double b, double epsilon = 0.0001) {

	return std::abs(a-b) < epsilon;
}

int main() {

	cudaError err;

	// Alustetaan satunnaislukugeneraattori kellonajalla
	srand(time(NULL));

	// Satunnainen kokonaisluku väliltä [1,MAXN]
	int n = rand() % MAXN + 1;

	// Varataan tilaa vektoreille ja luodaan satunnainen liukuluku
	double *x = new double[n];
	double *y = new double[n];
	double m = (B-A)*((double)rand()/RAND_MAX)+A;

	//ja vektorit testaamista varten
	double *x_test = new double[n];
	double *y_test = new double[n];


	// Generoidaan kaksi satunnaista vektoria
	for(int i = 0; i < n; i++) {
		x[i] = (B-A)*((double)rand()/RAND_MAX)+A;
		y[i] = (B-A)*((double)rand()/RAND_MAX)+A;
	}

	//kopioidaan vektorit talteen testaamista varten
	for(int i = 0; i < n; i++) {
		x_test[i] = x[i];
		y_test[i] = y[i];
	}

	//Varataan muisti Cuda-laitteelta

	double *deviceBuffer1, *deviceBuffer2;

	//Varataan deviceBuffer1
	err = cudaMalloc((void **)&deviceBuffer1, n*sizeof(double));

	if(err != cudaSuccess) {
		std::cout << "Muistin varaaminen epäonnistui." << std::endl;

		// cudaError on union-tietotyyppi. Virhekoodia vastaava virheviestijono
		// saadaan selville cudaGetErrorString-aliohjelman avulla.
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;
		delete [] x;
		delete [] y;
		delete [] x_test;
		delete [] y_test;
		return 1;
	}

	//Varataan deviceBuffer2
	err = cudaMalloc((void **)&deviceBuffer2, n*sizeof(double));

	if(err != cudaSuccess) {
		std::cout << "Muistin varaaminen epäonnistui." << std::endl;

		// cudaError on union-tietotyyppi. Virhekoodia vastaava virheviestijono
		// saadaan selville cudaGetErrorString-aliohjelman avulla.
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;
			delete [] x;
			delete [] y;
			delete [] x_test;
			delete [] y_test;
		return 1;
	}

	//Siirretään data cuda-laitteelle

  	//Siirretään x -> deviceBuffer1
	err = cudaMemcpy(
		deviceBuffer1, x, n*sizeof(double), cudaMemcpyHostToDevice);

	if(err != cudaSuccess) {
		std::cout << "Isäntälaite -> GPU -siirtokäskyn asettaminen " \
			"komentojonoon epäonnistui." << std::endl;
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;

		cudaFree(deviceBuffer1);
		cudaFree(deviceBuffer2);
		delete [] x;
		delete [] y;
		delete [] x_test;
		delete [] y_test;
		return 1;
	}

	//Siirretään y -> deviceBuffer2
	err = cudaMemcpy(
		deviceBuffer2, y, n*sizeof(double), cudaMemcpyHostToDevice);

	if(err != cudaSuccess) {
		std::cout << "Isäntälaite -> GPU -siirtokäskyn asettaminen " \
			"komentojonoon epäonnistui." << std::endl;
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;

		cudaFree(deviceBuffer1);
		cudaFree(deviceBuffer2);
		delete [] x;
		delete [] y;
		delete [] x_test;
		delete [] y_test;
		return 1;
	}

	//ytimen käynnistäminen

	// Säieryhmien määrä
	dim3 WGCount(n/LOCAL_SIZE+1, 1, 1);

	// Lokaalin indeksiavaruuden koko
	dim3 localDim(LOCAL_SIZE, 1, 1);

	axpy<<<WGCount, localDim>>>(deviceBuffer1, deviceBuffer2, m, n);

	//kysytään tuliko ytimen käynnistämisessä virheitä
	err = cudaGetLastError();

	if(err != cudaSuccess) {
		std::cout << "Ytimen käynnistyskäskyn asettaminen komentojonoon " \
			"epäonnistui." << std::endl;
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;

		cudaFree(deviceBuffer1);
		cudaFree(deviceBuffer2);
		delete [] x;
		delete [] y;
		delete [] x_test;
		delete [] y_test;
		return 1;
	}

	//
	// Siirretään data takaisin isäntälaiteen muistiin
	//

	//x ja deviceBuffer1
	cudaMemcpy(x, deviceBuffer1, n*sizeof(double), cudaMemcpyDeviceToHost);

	if(err != cudaSuccess) {
		std::cout << "GPU -> Isäntälaite -siirtokäskyn asettaminen " \
			"komentojonoon epäonnistui." << std::endl;
		std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
			std::endl;

			cudaFree(deviceBuffer1);
			cudaFree(deviceBuffer2);
			delete [] x;
			delete [] y;
			delete [] x_test;
			delete [] y_test;
		return 1;
	}

	//y ja deviceBuffer2
	cudaMemcpy(y, deviceBuffer2, n*sizeof(double), cudaMemcpyDeviceToHost);

		if(err != cudaSuccess) {
			std::cout << "GPU -> Isäntälaite -siirtokäskyn asettaminen " \
				"komentojonoon epäonnistui." << std::endl;
			std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
				std::endl;

			cudaFree(deviceBuffer1);
			cudaFree(deviceBuffer2);
			delete [] x;
			delete [] y;
			delete [] x_test;
			delete [] y_test;
			return 1;
		}

		//
		// Tarkistetaan tulos
		//


		//lasketaan axpy cpu:lla
		for(int i = 0; i < n; i++) {
			y_test[i] = m * x_test[i] + y_test[i];
		}

		//tarkistetaan ovatko kaikki y:n arvot samoja
		bool correct = true;
		for(int i = 0; i < n; i++) {
			if (double_equals(y_test[i], y[i]) != true) {
				correct = false;
				//tulostetaan eriävät arvot
				std::cout << y_test[i] << std::endl;
				std::cout << y[i] << std::endl;
				break;
			}
		}

		std::cout << "The result was " ;
		if (!correct) {
			std::cout << "incorrect." << std::endl;
		}
		else std::cout << "correct." << std::endl;


		//
		// Vapautetaan CUDA-laitteen puolelta varattu muisti
		//
		err = cudaFree(deviceBuffer1);

		if(err != cudaSuccess) {
			std::cout << "Muistin vapauttaminen epäonnistui epäonnistui." <<
				std::endl;
			std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
				std::endl;

			delete [] x;
			delete [] y;
			delete [] x_test;
			delete [] y_test;
			return 1;
		}

		err = cudaFree(deviceBuffer2);

		if(err != cudaSuccess) {
			std::cout << "Muistin vapauttaminen epäonnistui epäonnistui." <<
				std::endl;
			std::cerr << "CUDA-virhekoodi: " << cudaGetErrorString(err) <<
				std::endl;

			delete [] x;
			delete [] y;
			delete [] x_test;
			delete [] y_test;
			return 1;
		}

		delete [] x;
		delete [] y;
		delete [] x_test;
		delete [] y_test;

		return 0;
}
