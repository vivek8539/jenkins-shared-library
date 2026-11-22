import com.connectify.steps.PipelineUtils

def call() {
    pipeline {
        agent {
            kubernetes {
                label 'stageExecutor-pod'
                yaml env.BUILDPOD_YAML
            }
        }
        options {
            buildDiscarder(logRotator(numToKeepStr: '5'))
            disableConcurrentBuilds()
        }
        stages {
            stage('Generate Pipeline') {
                steps {
                    script {
                        def stagesList = new PipelineUtils().getStages(this)
                        echo "current work dir: ${pwd()}"
                        stagesList.each { currentStage ->
                            stage(currentStage.getStep()) {
//                                currentStage.execute()
                                container('maven') {

                                    sh "mvn versions:set -DnewVersion=0.0.1"
                                    sh "mvn versions:commit"
                                }
                                sh """git config user.name 'jenkins bot' """
                                sh """git config user.email 'jenkins@connectify.com' """
                                sh "git add pom.xml"
                                sh "git commit -m \"Bump version to 0.0.1\""
                                sh "git remote -v"
                                RAW_URL=$(sh "git remote get-url origin")

                                CLEAN_URL=$(echo "$RAW_URL" | sed 's|https://||')
                                withCredentials([
                                        usernamePassword(
                                                credentialsId: 'github',
                                                usernameVariable: 'GIT_USERNAME',
                                                passwordVariable: 'GIT_TOKEN'
                                        )
                                ]) {

                                    sh "git push https://${GIT_USERNAME}:${GIT_TOKEN}@${CLEAN_URL} HEAD"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
