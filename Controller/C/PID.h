#ifndef PID_H
#define PID_H

typedef struct{
	float KP, KI, KD;
	float P, I, D;
	float outMin, outMax;
	float lastError;

	float frequency;
}PID;


void PID_setKP(PID *pid, float kp);
void PID_setKI(PID *pid, float ki);
void PID_setKD(PID *pid, float kd);
float PID_getKP(PID *pid);
float PID_getKI(PID *pid);
float PID_getKD(PID *pid);

void PID_setGains(PID *pid, float kp, float ki, float kd);
void PID_setLimits(PID *pid, float min, float max);
void PID_setFrequency(PID *pid, float freq);
void PID_reset(PID *pid);

void PID_begin(PID *pid);

float PID_compute(PID *pid, float error);

#endif