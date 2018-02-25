#include "PID.h"


void PID_setKP(PID *pid, float kp){
	pid->KP = kp;
}

void PID_setKI(PID *pid, float ki){
	pid->KI = ki;
}

void PID_setKD(PID *pid, float kd){
	pid->KD = kd;
}

float PID_getKP(PID *pid){
	return pid->KP;
}

float PID_getKI(PID *pid){
	return pid->KI;
}

float PID_getKD(PID *pid){
	return pid->KD;
}

void PID_setGains(PID *pid, float kp, float ki, float kd){
	PID_setKP(pid, kp);
	PID_setKI(pid, ki);
	PID_setKD(pid, kd);
}

void PID_setLimits(PID *pid, float min, float max){
	pid->outMin = min;
	pid->outMax = max;
}

void PID_setFrequency(PID *pid, float freq){
	pid->frequency = freq;
}

void PID_reset(PID *pid){
	pid->P = 0;
	pid->I = 0;
	pid->D = 0;
	pid->lastError = 0;
}

void PID_begin(PID *pid){
	PID_setGains(pid, 0.0, 0.0, 0.0);

	PID_setLimits(pid, 0, 255);
	PID_setFrequency(pid, 1.0);	
}

float PID_compute(PID *pid, float error){

	// Implementation of P
	pid->P = pid->KP * error;

	// Implementation of I
	pid->I += (pid->KI * error / pid->frequency);
	if (pid->I > pid->outMax) pid->I = pid->outMax;
	else if (pid->I < pid->outMin) pid->I = pid->outMin;

	// Implementation of D
	pid->D = pid->KD * (error - pid->lastError) * pid->frequency;

	// Calculate output PID
	float output = pid->P + pid->I + pid->D;
	if (output > pid->outMax) output = pid->outMax;
	else if (output < pid->outMin) output = pid->outMin;

	pid->lastError = error;

	return output;
}