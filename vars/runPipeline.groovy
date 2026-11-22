import com.connectify.steps.PipelineUtils

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
                        def stagesList = new PipelineUtils().getStages(this)
                        echo "current work dir: ${pwd()}"
                        stagesList.each { currentStage ->
                            stage(currentStage.getStep()) {
                                currentStage.execute()
                            }
                        }
                    }
                }
            }
        }
    }
}
