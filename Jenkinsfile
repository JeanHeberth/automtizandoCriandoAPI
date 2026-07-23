pipeline {
    agent any

    environment {
        API_USER = credentials('login-pipeline')
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Validar Java') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'java -version'
                    } else {
                        bat 'java -version'
                    }
                }
            }
        }

        stage('Executar Testes') {
            steps {
                script {
                    if (isUnix()) {
                        sh """
                            echo "Usuário: $API_USER_USR"
                            echo "Senha: $API_USER_PSW"
                            ./gradlew clean test
                        """
                    } else {
                        bat """
                            echo Usuário: %API_USER_USR%
                            echo Senha: %API_USER_PSW%
                            gradlew.bat clean test
                        """
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
        }
    }
}
