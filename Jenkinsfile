pipeline {
    agent any

    environment {
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

                                powershell.exe -NoProfile -Command ^
                                    "Start-Sleep -Seconds 5"
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
                withCredentials([
                    usernamePassword(
                        credentialsId: 'login-pipeline',
                        usernameVariable: 'API_EMAIL',
                        passwordVariable: 'API_SENHA'
                    )
                ]) {
                    script {
                        if (isUnix()) {
                            sh '''
                                echo "Verificando credenciais..."

                                if [ -z "$API_EMAIL" ]; then
                                    echo "ERRO: API_EMAIL não foi carregada."
                                    exit 1
                                fi

                                if [ -z "$API_SENHA" ]; then
                                    echo "ERRO: API_SENHA não foi carregada."
                                    exit 1
                                fi

                                echo "Email carregado: SIM"
                                echo "Tamanho do email: ${#API_EMAIL}"
                                echo "Email possui arroba: $(echo "$API_EMAIL" | grep -q "@" && echo SIM || echo NAO)"

                                echo "Senha carregada: SIM"
                                echo "Tamanho da senha: ${#API_SENHA}"
                            '''
                        } else {
                            powershell '''
                                Write-Host "Verificando credenciais..."

                                if ([string]::IsNullOrWhiteSpace($env:API_EMAIL)) {
                                    Write-Host "ERRO: API_EMAIL nao foi carregada."
                                    exit 1
                                }

                                if ([string]::IsNullOrWhiteSpace($env:API_SENHA)) {
                                    Write-Host "ERRO: API_SENHA nao foi carregada."
                                    exit 1
                                }

                                Write-Host "Email carregado: SIM"
                                Write-Host "Tamanho do email: $($env:API_EMAIL.Length)"
                                Write-Host "Email possui arroba: $($env:API_EMAIL.Contains('@'))"

                                Write-Host "Senha carregada: SIM"
                                Write-Host "Tamanho da senha: $($env:API_SENHA.Length)"
                            '''
                        }
                    }
                }
            }
        }

        stage('Executar Testes') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'login-pipeline',
                        usernameVariable: 'API_EMAIL',
                        passwordVariable: 'API_SENHA'
                    )
                ]) {
                    script {
                        if (isUnix()) {
                            sh '''
                                echo "Executando testes com credenciais..."

                                chmod +x gradlew

                                ./gradlew clean test \
                                    --stacktrace \
                                    --info \
                                    --no-daemon
                            '''
                        } else {
                            bat '''
                                @echo off

                                echo Executando testes com credenciais...

                                call gradlew.bat clean test ^
                                    --stacktrace ^
                                    --info ^
                                    --no-daemon

                                exit /b %ERRORLEVEL%
                            '''
                        }
                    }
                }
            }
        }

        stage('Publicar Resultado') {
            steps {
                junit(
                    allowEmptyResults: true,
                    testResults: '**/build/test-results/test/*.xml'
                )
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
            archiveArtifacts(
                artifacts: '**/build/reports/tests/**',
                allowEmptyArchive: true
            )

            echo 'Pipeline de testes concluída.'
        }
    }
}