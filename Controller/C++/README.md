# Controlador PID em C++

Esta biblioteca possui métodos que permitem controlar sistemas com plataformas que aceitam a linguagem de programação C++. Por exemplo a [Arduino IDE](https://www.arduino.cc/en/Main/Software).

Caso necessite programar em linguagem C, seu caminho é [*este*](https://github.com/AsafeSilva/PID-Tuner-Controller/tree/master/Controller/C).


## "PID.h"

- Criando sua variável `PID`

É possível criar uma variável `PID` de três formas distintas:

- A 1ª é utilizando o construtor *default*:

```c++
// Com este construtor os ganhos kP, kI, e kD são iniciados com 0. E os limites do PID são 0 e 255;
PID myPID;
```

*O nome `myPID` é um exemplo*

- Na 2ª forma você pode setar alguns parâmetros:

```c++
// Inicia os ganhos kP, kI, kD, respectivamente, com valores desejados
PID myPID(1.0, 1.0, 1.0);
```

- E eis a 3ª forma:

```c++
// Mesmo que o anterior, com o acréscimo de dois parâmetros: outMin e outMax (saída mínima e saída máxima).
PID myPID(1.0, 1.0, 1.0, -100, 100); 
```


Após ter sua varíavel declarada, você pode usá-la para acessar um conjunto de métodos (ou funções).


- Primeiramente, vamos listar os métodos que alteram as constantes do PID (KP, KI e KD). Estas são as variáveis que, se ajustadas corretamente, farão o PID trabalhar de forma eficiente. Elas são 4:

```c++
// Seta os ganhos PID separadamente
void setKP(float _kP);
void setKI(float _kI);
void setKD(float _kD);  

// Seta todos os ganhos de uma só vez
void setGains(float _kP, float _kI, float _kD);
```


**Para acessar qualquer um dos métodos citados, basta usar a seguinte sintaxe:**
```c++
PID pidVelocity;

pidVelocity.setGains(1.0, 0.05, 0.45);
pidVelocity.compute();
...

OU

PID *pidVelocity;

pidVelocity->setGains(1.0, 0.05, 0.45);
pidVelocity->compute();
...
```

- Existem mais 2 métodos que setam alguns parâmetros importantes. São eles:

```c++
// Seta limites da saída
void setLimits(int _outMin, int _outMax);

// Seta a frequência do cálculo PID. Ou seja, quantas vezes por segundo ele será executado 
void setFrequency(float _frequency);
```

- Um método que pode ser muito importante em alguns projetos é o de *resetar* as variáveis:

```c++
// Zera as váriveis P, I e D, além da váriavel que guarda o último erro lido
void reset();
```

- O PID trabalha com base em uma variável chamada de `Erro`, que expressa o quanto falta para se alcançar o objetivo. Essa variável é a diferença entre o *SetPoint* e a *Entrada* do sistema. Desta forma, existem dois métodos que devem ser chamados antes de realizar o cálculo PID:

```c++
// Atualiza o SetPoint (valor desejado para o sistema)
void setSetPoint(float _setPoint);

// Atualiza valor de entrada do sistema
void addInput(float _newInput);
```

- Antes de ir para a função principal, existem 4 métodos *Getters*:

```c++
// Métodos que retornam parâmetros do PID
float getKP();
float getKI();
float getKD();
float getSetPoint();
```

- Por fim, a função que faz a mágica! É possível utilizá-la de duas formas:

Vamos supor que você está colocando o cálculo PID dentro de um *Timer* (Temporizador), cuja frequência de execução é conhecida. Neste caso, você utiliza o método `setFrequency(float _frequency)`, informando a frequência. E finalmente, para calcular o PID, chama a seguinte função:

```c++
// Método que realiza cálculos e retorna a saída do PID (P + I + D)
float compute();
```

O outro caso é se você estiver calculando o tempo de loop no própio código, de forma dinâmica. Nesta situação, você necesita informar como parâmetro o período de execução. Um exemplo usando a plataforma *Arduino*, seria assim:

```c++
unsigned long time = (millis() - lastComputeTime);	// Calcula tempo que passou
lastComputeTime = millis();				// Tempo passado = tempo atual
float dt = time/1000.0f;				// Calcula tempo em segundos

float output = myPID.compute(dt);
```


*E se eu precisar controlar mais de um PID no mesmo programa?*
Simples!

```c++
PID pidVelocity(2.0, 0.01, 0.5, 0, 100);
PID pidTemperature(3.0, 0.1, 1.0, -50, 50);
...

pidVelocity.compute();
pidTemperature.compute();
...
```

Você também pode criar um *array* de PIDs:

```c++
PID pids[3];

for(int i = 0; i < 3; i++)
	pids[i].compute();
```


# Lista de funções

```c++

  PID(float _kP = 0.0, float _kI = 0.0, float _kD = 0.0);

  PID(float _kP, float _kI, float _kD, float _outMin, float _outMax);

  void setSetPoint(float _setPoint);

  void setKP(float _kP);
  
  void setKI(float _kI);
  
  void setKD(float _kD);  

  void setGains(float _kP, float _kI, float _kD);
  
  void setLimits(int _outMin, int _outMax);
  
  void setFrequency(float _frequency);

  void addInput(float _newInput);

  float compute();
  
  float compute(float dTime);

  void reset();

  float getKP();
  
  float getKI();
  
  float getKD();
  
  float getSetPoint();
```


**Espero ter ajudado!**

Caso encontre algum erro, ou queira fazer alguma contribuição, por favor, contate-me!
