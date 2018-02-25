# Controlador PID em linguagem C

Esta biblioteca possui métodos que permitem controlar sistemas com plataformas que aceitam a linguagem de programação C. Por exemplo a [Arduino IDE](https://www.arduino.cc/en/Main/Software), a [MPLAB IDE](http://www.microchip.com/mplab/mplab-x-ide), entre outros.

Caso necessite programar em linguagem C++, seu caminho é [*este*](https://github.com/AsafeSilva/PID-Tuner-Controller/Controller/C++).


## "PID.h"

- Criando sua variável `PID`

Declarar sua variável `PID` é muito simples. Basta dizer o tipo da variável, neste caso `PID`, e colocar o nome da sua variável. Por exemplo:

```java
PID myPID;

// ou como um ponteiro:

PID *myPID;
```


Após ter sua varíavel declarada, você pode usá-la com um conjunto de funções disponíves nesta biblioteca.


- Primeiramente, vamos listar as funções que alteram as constantes do PID (KP, KI e KD). Estas são as variáveis que, se ajustadas corretamente, farão o PID trabalhar de forma eficiente. Elas são 4:

```c
// Seta os ganhos PID separadamente
void PID_setKP(PID *pid, float kp);
void PID_setKI(PID *pid, float ki);
void PID_setKD(PID *pid, float kd); 

// Seta todos os ganhos de uma só vez
void PID_setGains(PID *pid, float kp, float ki, float kd);
```


**Para usar qualquer uma da funções citadas, basta usar a seguinte sintaxe:**
```c
PID pidVelocity;

PID_setGains(&pidVelocity, 1.0, 0.05, 0.45);
PID_compute(&pidVelocity, error);
...

// Se a variável não for declarada como ponteiro, não esqueça do '&' antes da mesma! 
```

- Existem mais 2 métodos que setam alguns parâmetros importantes. São eles:

```c
// Seta limites da saída
void PID_setLimits(PID *pid, float min, float max);

// Seta a frequência do cálculo PID. Ou seja, quantas vezes por segundo ele será executado 
void PID_setFrequency(PID *pid, float freq);
```

- Um método que pode ser muito importante em alguns projetos é o de *resetar* as variáveis:

```c
// Zera as váriveis P, I e D, além da váriavel que guarda o último erro lido
void PID_reset(PID *pid);
```

- Antes de ir para a função principal, existem 3 métodos *Getters*:

```c
// Métodos que retornam parâmetros do PID
float PID_getKP(PID *pid);
float PID_getKI(PID *pid);
float PID_getKD(PID *pid);
```

- Por fim, a função que faz a mágica!

O PID trabalha com base em uma variável chamada de `Erro`, que expressa o quanto falta para se alcançar o objetivo. Essa variável é a diferença entre o *SetPoint* e a *Entrada* do sistema, que é o parâmetro da função `compute()`:

```c
// Método que realiza cálculos e retorna a saída do PID (P + I + D)
float PID_compute(PID *pid, float error);
```


*E se eu precisar controlar mais de um PID no mesmo programa?*
Simples!

```c
PID pidVelocity;
PID pidTemperature;
...

PID_compute(&pidVelocity, velocityError);
PID_compute(&pidTemperature, temperatureError);
...
```

Você também pode criar um *array* de PIDs:

```c
PID pids[3];

PID_compute(&pids[0], error0);
PID_compute(&pids[1], error1);
PID_compute(&pids[3], error2);
```


# Lista de funções

```c
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
```

**Espero ter ajudado!**

Caso encontre algum erro, ou queira fazer alguma contribuição, por favor, contate-me!