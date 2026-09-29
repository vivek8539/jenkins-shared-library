import com.connectify.PipelineUtils

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
                    script {
                        def stagesList = PipelineUtils.getStages()
                        echo "current work dir: ${pwd()}"
//                        def stagesList = ['Build', 'Test', 'Deploy', 'Pause']
                        stagesList.each { stageName ->
                            stage(stageName) {
                                echo "Executing ${stageName} stage"
                                if (stageName == 'Pause') {
                                    input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'
                                } else if (stageName == 'Build') {
                                    echo 'Executing build tasks'
                                    container('maven') {
                                        sh 'mvn clean install -DskipTests=true'
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
