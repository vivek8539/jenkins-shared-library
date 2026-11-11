import com.connectify.PipelineUtils

def call() {
    pipeline {
        agent {
            label 'built-in'
        }

        stages {
            stage('Generate Pipeline') {
                steps {
                    script {
                        def stagesList = PipelineUtils.getStages(this)
                        echo "current work dir: ${pwd()}"
//                        def stagesList = ['Build', 'Test', 'Deploy', 'Pause']
                        stagesList.each { stageName ->
                            stage(stageName.toString()) {
                                stageName.run(stageName.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}
