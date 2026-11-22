package com.connectify.steps

class BuildDockerImage extends BaseStep {

    def script

    BuildDockerImage(script) {
        super("Build Docker Image")
        this.script = script
    }

    @Override
    void execute() {
        def pom = script.readMavenPom(file: 'pom.xml')
        String version = pom.version
        script.echo "Application: ${pom.artifactId}"
        script.echo "Version: ${version}"

        String ImageName = "${script.env.DOCKER_USERNAME}/${pom.artifactId}:${version}"
        script.echo "Image Name: ${ImageName}"

        script.container('kaniko') {
            script.sh """
                /kaniko/executor --context . --dockerfile ${script.pwd()}/Dockerfile --destination ${ImageName} --insecure --skip-tls-verify
            """
        }

                String newVersion = getNewVersion(version)
        script.container('maven') {

            script.sh "mvn versions:set -DnewVersion=${newVersion}"
            script.sh "mvn versions:commit"
        }
        script.sh """git config user.name 'jenkins bot' """
        script.sh """git config user.email 'jenkins@connectify.com' """
        script.sh "git add pom.xml"
        script.sh "git commit -m \"Bump version to ${newVersion}\""
        script.sh "git remote -v"
        script.withCredentials([
                script.usernamePassword(
                        credentialsId: 'github',
                        usernameVariable: 'GIT_USERNAME',
                        passwordVariable: 'GIT_TOKEN'
                )
        ]) {

            script.sh '''
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

    private static String getNewVersion(String currentVersion) {
          def splittedVersion= currentVersion.split('-')
        def parts = splittedVersion[0].tokenize('.')
        parts[-1] = (parts[-1].toInteger() + 1).toString()

        def newVersion
        if (splittedVersion.length > 1) {

             newVersion = parts.join('.') + '-' + splittedVersion[1]
        } else {
             newVersion = parts.join('.')
        }
        return newVersion
    }
}
