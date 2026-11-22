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
        script.sh "git push origin HEAD"
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
