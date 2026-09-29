def call() {
    def stagesList = []

    pipeline {
        agent any

        stages {
            stage('Generate Pipeline') {
                steps {
                    script {
                        stagesList = ['Build', 'Test', 'Deploy']
                    }
                }
            }

            stage('Setup Environment') {
                agent {
                    kubernetes {
                        label 'build-pod'
                        yaml env.BUILDPOD_YAML
                    }
                }
                steps {
                    script {
                        stagesList.each { stageName ->
                            stage(stageName) {
                                echo "Executing ${stageName} stage"
                            }
                        }
                    }
                }
            }
        }
    }
}
