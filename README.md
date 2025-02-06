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
   git clone https://github.com/VicRyan007/StepCounterApp

   # StepCounterApp

## Instruções para Configuração e Execução

### Abra o Projeto no Android Studio

1. Selecione **"Open an existing project"** e navegue até a pasta clonada.

### Sincronize as Dependências

1. Aguarde a sincronização do Gradle e a importação dos arquivos.

### Compile e Execute

1. Conecte um dispositivo Android ou inicie um emulador.
2. Clique em **"Run"** para compilar e instalar o app.
3. Conceda as permissões solicitadas (**ACTIVITY_RECOGNITION** e **POST_NOTIFICATIONS**).

## Observações

- A contagem é relativa ao primeiro registro (baseline).
- A atualização ocorre a cada dois passos.
- A notificação persistente é exibida mesmo quando o app está em segundo plano.

   
