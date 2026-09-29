def call() {
    def stagesList = []

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
                    script {
                        stagesList = ['Build', 'Test', 'Deploy']
                    }
                }
            }

            stage('Generated Pipeline') {
                steps {
                    script {
                        def stageExecutor = load 'pipeline/stageExecutor.groovy'

                        stagesList.each { stageName ->
                            stage(stageName) {
                                stageExecutor.executeStage(stageName)
                            }
                        }
                    }
                }
            }
        }
    }
}
