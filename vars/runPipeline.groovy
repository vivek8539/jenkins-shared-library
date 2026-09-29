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
                        stagesList = ['Build', 'Test', 'Deploy', 'Pause']
                        input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'
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
