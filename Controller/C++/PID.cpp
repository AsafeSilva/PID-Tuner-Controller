#include "PID.h"

PID::PID(float _kP, float _kI, float _kD){
    setParameters(_kP, _kI, _kD);
    setSetPoint(0);
    setFrequency(1.0);

    outMax = 255;
    outMin = 0;
}

PID::PID(float _kP, float _kI, float _kD, float _outMin, float _outMax){
    setParameters(_kP, _kI, _kD);
    setFrequency(1.0);

    outMax = _outMax;
    outMin = _outMin;
}

void PID::setSetPoint(float _setPoint){
	setPoint = _setPoint;
}

void PID::setKP(float _kP){  kP = _kP; }
void PID::setKI(float _kI){  kI = _kI; }
void PID::setKD(float _kD){  kD = _kD; }  

void PID::setGains(float _kP, float _kI, float _kD){
	setKP(_kP);
	setKI(_kI);
	setKD(_kD);
}

void PID::setLimits(int _outMin, int _outMax){
	outMin = _outMin;
	outMax = _outMax;
}

void PID::setFrequency(float _frequency){
	frequency = _frequency;
}

void PID::addInput(float _newInput){
	newInput = _newInput;
}

float PID::compute(){
	compute(1.0/frequency);
}

float PID::compute(float dTime){

	// Calculates error
	error = (setPoint - newInput);

	// Implementation of the Proportional part
	P = kP * error;

	// Implementation of the Integrative part
	I += kI * error * dTime;
	if(I > outMax) I = outMax;
	else if(I < outMax) I = outMin;

	// Implementation of the Derivative part
	D = kD * (error - lastError) / dTime;

	// Calculates the PID output
	output = P + I + D;
	if(output > outMax) output = outMax;
	else if(output < outMax) output = outMin;

	lastError = error;

	return output;
}

void PID::reset(){
	P = I = D = lastError = 0;
}

float PID::getKP(){return kP;}
float PID::getKI(){return kI;}
float PID::getKD(){return kD;}
float PID::getSetPoint(){return setPoint;}
