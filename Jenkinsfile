pipeline {
    agent any

    environment {
        API_USER = credentials('login-pipeline')

        API_URL_WINDOWS = 'http://100.83.72.100:9999/criandoAPI/v1/actuator/health'
        API_URL_UNIX    = 'http://100.83.72.100:9999/criandoAPI/v1/actuator/health'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositório de testes...'
                checkout scm
            }
        }

        stage('Aguardar API iniciar') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            echo "Aguardando API iniciar..."

                            for i in $(seq 1 12); do
                                echo "Tentativa $i de 12..."

                                if curl --silent --fail "$API_URL_UNIX" > /dev/null; then
                                    echo "API disponível."
                                    exit 0
                                fi

                                echo "API ainda não está disponível."
                                sleep 5
                            done

                            echo "ERRO: API não iniciou no tempo esperado."
                            exit 1
                        '''
                    } else {
                        bat '''
                            @echo off
                            echo Aguardando API iniciar...

                            for /L %%i in (1,1,12) do (
                                echo Tentativa %%i de 12...

                                curl.exe --silent --fail "%API_URL_WINDOWS%" >nul 2>&1

                                if not errorlevel 1 (
                                    echo API disponivel.
                                    exit /b 0
                                )

                                echo API ainda nao esta disponivel.
                                powershell.exe -NoProfile -Command "Start-Sleep -Seconds 5"
                            )

                            echo ERRO: API nao iniciou no tempo esperado.
                            exit /b 1
                        '''
                    }
                }
            }
        }
        stage('Validar Credenciais') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            echo "Verificando credenciais..."

                            if [ -z "$API_USER" ]; then
                                echo "ERRO: API_USER nao foi carregado."
                                exit 1
                            fi

                            if [ -z "$API_USER_PSW" ]; then
                                echo "ERRO: API_USER_PSW nao foi carregado."
                                exit 1
                            fi

                            echo "Usuario carregado: $API_USER"
                            echo "Senha carregada: SIM"
                            echo "Quantidade de caracteres da senha: ${#API_USER_PSW}"
                        '''
                    } else {
                        powershell '''
                            Write-Host "Verificando credenciais..."

                            if ([string]::IsNullOrWhiteSpace($env:API_USER)) {
                                Write-Host "ERRO: API_USER nao foi carregado."
                                exit 1
                            }

                            if ([string]::IsNullOrWhiteSpace($env:API_USER_PSW)) {
                                Write-Host "ERRO: API_USER_PSW nao foi carregado."
                                exit 1
                            }

                            Write-Host "Usuario carregado: $env:API_USER"
                            Write-Host "Senha carregada: SIM"
                            Write-Host "Quantidade de caracteres da senha: $($env:API_USER_PSW.Length)"
                        '''
                    }
                }
            }
        }
        stage('Executar Testes') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            export EMAIL="$API_USER_USR"
                            export PASSWORD="$API_USER_PSW"

                            chmod +x gradlew
                            ./gradlew clean test
                        '''
                    } else {
                        bat '''
                            @echo off

                            set EMAIL=%API_USER_USR%
                            set PASSWORD=%API_USER_PSW%

                            echo Executando testes com credenciais...
                            gradlew.bat clean test --stacktrace --info --no-daemon                        '''
                    }
                }
            }
        }

        stage('Publicar Resultado') {
            steps {
                junit allowEmptyResults: true,
                      testResults: '**/build/test-results/test/*.xml'
            }
        }
    }

    post {
        success {
            echo 'Todos os testes foram aprovados.'
        }

        failure {
            echo 'Um ou mais testes falharam.'
        }

        always {
            archiveArtifacts artifacts: '**/build/reports/tests/**',
                             allowEmptyArchive: true
            echo 'Pipeline de testes concluída.'
        }
    }
}
