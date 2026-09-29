def call() {
    pipeline {
        agent {
            kubernetes {
                label 'stageExecutor-pod'
                yaml env.BUILDPOD_YAML
            }
        }

        stages {
            stage('Generate Pipeline') {
                steps {
                    echo 'Preparing Build, Test, Deploy, and Pause stages'
                }
            }

            stage('Build') {
                steps {
                    echo 'Executing Build stage'
                    container('maven') {
                        sh 'mvn clean install -DskipTests=true'
                    }
                }
            }

            stage('Test') {
                steps {
                    echo 'Executing Test stage'
                }
            }

            stage('Deploy') {
                steps {
                    echo 'Executing Deploy stage'
                }
            }

            stage('Pause') {
                steps {
                    input message: 'Do you want to proceed with Pause stage?', ok: 'Yes'
                }
            }
        }
    }
}
