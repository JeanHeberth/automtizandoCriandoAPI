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
                                powershell.exe -NoProfile -Command "Start-Sleep -Seconds 5"
                            )

                            echo ERRO: API nao iniciou no tempo esperado.
                            exit /b 1
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
                            chmod +x gradlew
                            ./gradlew clean test
                        '''
                    } else {
                        bat '''
                            @echo off
                            echo Executando testes...
                            gradlew.bat clean test
                        '''
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
