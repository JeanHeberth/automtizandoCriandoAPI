pipeline {
    agent any

    environment {
        API_URL_WINDOWS = 'http://100.83.72.100:9999/criandoAPI/v1/actuator/health'
        API_URL_UNIX    = 'http://100.83.72.100:9999/criandoAPI/v1/actuator/health'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
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
                                echo "Senha carregada: SIM"
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
                                Write-Host "Senha carregada: SIM"
                            '''
                        }
                    }
                }
            }
        }

        stage('Preparar Projeto') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            chmod +x gradlew

                            ./gradlew clean testClasses \
                                --stacktrace \
                                --no-daemon
                        '''
                    } else {
                        bat '''
                            @echo off

                            call gradlew.bat clean testClasses ^
                                --stacktrace ^
                                --no-daemon

                            exit /b %ERRORLEVEL%
                        '''
                    }
                }
            }
        }

        stage('Testes de Autenticação') {
            steps {
                catchError(
                    buildResult: 'FAILURE',
                    stageResult: 'FAILURE'
                ) {
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
                                    echo "Executando testes de autenticação..."

                                    ./gradlew testAuth \
                                        --stacktrace \
                                        --info \
                                        --no-daemon
                                '''
                            } else {
                                bat '''
                                    @echo off

                                    echo Executando testes de autenticacao...

                                    call gradlew.bat testAuth ^
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
        }

        stage('Testes de Health Check') {
            steps {
                catchError(
                    buildResult: 'FAILURE',
                    stageResult: 'FAILURE'
                ) {
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
                                    echo "Executando testes de health check..."

                                    ./gradlew testHealth \
                                        --stacktrace \
                                        --info \
                                        --no-daemon
                                '''
                            } else {
                                bat '''
                                    @echo off

                                    echo Executando testes de health check...

                                    call gradlew.bat testHealth ^
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
        }

        stage('Testes de Pedido') {
            steps {
                catchError(
                    buildResult: 'FAILURE',
                    stageResult: 'FAILURE'
                ) {
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
                                    echo "Executando testes de pedido..."

                                    ./gradlew testPedido \
                                        --stacktrace \
                                        --info \
                                        --no-daemon
                                '''
                            } else {
                                bat '''
                                    @echo off

                                    echo Executando testes de pedido...

                                    call gradlew.bat testPedido ^
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
        }

        stage('Testes de Produto') {
            steps {
                catchError(
                    buildResult: 'FAILURE',
                    stageResult: 'FAILURE'
                ) {
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
                                    echo "Executando testes de produto..."

                                    ./gradlew testProduto \
                                        --stacktrace \
                                        --info \
                                        --no-daemon
                                '''
                            } else {
                                bat '''
                                    @echo off

                                    echo Executando testes de produto...

                                    call gradlew.bat testProduto ^
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
        }

        stage('Testes de Usuário') {
            steps {
                catchError(
                    buildResult: 'FAILURE',
                    stageResult: 'FAILURE'
                ) {
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
                                    echo "Executando testes de usuário..."

                                    ./gradlew testUsuario \
                                        --stacktrace \
                                        --info \
                                        --no-daemon
                                '''
                            } else {
                                bat '''
                                    @echo off

                                    echo Executando testes de usuario...

                                    call gradlew.bat testUsuario ^
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
        }

        stage('Publicar Resultados JUnit') {
            steps {
                junit(
                    allowEmptyResults: true,
                    testResults: '**/build/test-results/**/*.xml'
                )
            }
        }

        stage('Publicar Allure Report') {
            steps {
                allure(
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'build/allure-results']]
                )
            }
        }

    } // fecha o bloco stages

    post {
        success {
            echo 'Todos os grupos de testes foram aprovados.'
        }

        unstable {
            echo 'A pipeline foi concluída com testes instáveis.'
        }

        failure {
            echo 'Um ou mais grupos de testes falharam.'
        }

        always {
            archiveArtifacts(
                artifacts: '**/build/reports/tests/**',
                allowEmptyArchive: true
            )

            archiveArtifacts(
                artifacts: '**/build/allure-results/**',
                allowEmptyArchive: true
            )

            echo 'Pipeline de testes concluída.'
        }
    }
}