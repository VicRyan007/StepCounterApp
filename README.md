# StepCounterApp

StepCounterApp é um aplicativo Android desenvolvido em Kotlin que conta os passos dados pelo usuário a partir de um valor base, atualizando a contagem a cada dois passos. O aplicativo utiliza o sensor de contagem de passos do dispositivo para calcular os passos relativos (a partir do início da contagem) e exibe essa informação tanto na interface do usuário quanto em uma notificação persistente na barra de notificações, mesmo quando o aplicativo está em segundo plano.

## Funcionalidades

- **Contagem de Passos em Tempo Real:**  
  Utiliza o sensor `TYPE_STEP_COUNTER` para contar os passos dados pelo usuário. A contagem é calculada de forma relativa, usando a primeira leitura como base e atualizando somente a cada dois passos.

- **Serviço em Primeiro Plano:**  
  Um serviço é executado em primeiro plano para que a contagem de passos continue mesmo quando o aplicativo é fechado ou está rodando em segundo plano.

- **Notificações Persistentes:**  
  Uma notificação é atualizada dinamicamente a cada dois passos, exibindo a contagem atual. Em dispositivos com Android 13 (Tiramisu) ou superior, é solicitada a permissão `POST_NOTIFICATIONS` para garantir a exibição correta.

- **Solicitação de Permissões em Tempo de Execução:**  
  - `ACTIVITY_RECOGNITION`: Necessária para acessar o sensor de passos (a partir do Android 10).
  - `POST_NOTIFICATIONS`: Necessária para exibir notificações em dispositivos Android 13+.

## Estrutura do Projeto


## Requisitos

- **Android SDK:**  
  - Compile SDK: 33  
  - Min SDK: 23  
  - Target SDK: 33

- **Kotlin:**  
  Versão 1.7.10

- **Dependências Principais:**  
  - `androidx.core:core-ktx`
  - `androidx.appcompat:appcompat`
  - `com.google.android.material:material`
  - `androidx.constraintlayout:constraintlayout`
  - `androidx.lifecycle:lifecycle-runtime-ktx`
  - `androidx.activity:activity-ktx`
  - `androidx.health:health-services-client`
  - `org.jetbrains.kotlinx:kotlinx-coroutines-android`

## Instalação e Uso

1. **Clone o repositório:**

   ```bash
   git clone <URL_DO_REPOSITORIO>
