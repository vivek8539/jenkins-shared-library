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
                    }
                }
            }

            stage('Generated Pipeline') {
                steps {
                    script {
//                        def stageExecutor = load 'pipeline/stageExecutor.groovy'

                        stagesList.each { stageName ->
                            stage(stageName) {
                                echo "Executing ${stageName} stage"
                                if (stageName =='Pause') {
                                    input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'
                                } else if (stageName == 'Build') {
                                    echo 'Executing build tasks'
                                    sh 'mvn clean install'
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
