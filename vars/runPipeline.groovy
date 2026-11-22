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

                                    sh "mvn versions:set -DnewVersion=0.0.2"
                                    sh "mvn versions:commit"
                                }
                                sh """git config user.name 'jenkins bot' """
                                sh """git config user.email 'jenkins@connectify.com' """
                                sh "git add pom.xml"
                                sh "git commit -m \"Bump version to 0.0.2\""
                                sh "git remote -v"
                                withCredentials([
                                        usernamePassword(
                                                credentialsId: 'github',
                                                usernameVariable: 'GIT_USERNAME',
                                                passwordVariable: 'GIT_TOKEN'
                                        )
                                ]) {

                                    sh '''
                                        RAW_URL=$(git remote get-url origin)
                                        CLEAN_URL=$(echo "$RAW_URL" | sed 's|https://||')
                                
                                        CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
                                        if [ "$CURRENT_BRANCH" = "HEAD" ]; then
                                            # If detached, try to get the branch name from Jenkins environment variables
                                            CURRENT_BRANCH="${BRANCH_NAME:-main}"
                                        fi
                                        
                                        echo "Pushing to branch: ${CURRENT_BRANCH}"
                                        git push https://${GIT_USERNAME}:${GIT_TOKEN}@${CLEAN_URL} HEAD:refs/heads/${CURRENT_BRANCH}
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
