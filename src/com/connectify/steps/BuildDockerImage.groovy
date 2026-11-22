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

        script.input message: 'Do you want to continue?', ok: 'Yes, Continue'

        script.container('kaniko') {
            script.sh """
                /kaniko/executor --context . --dockerfile ${script.pwd()}/Dockerfile --destination ${ImageName} --insecure --skip-tls-verify
            """
        }

        script.container('maven') {
            script.sh "mvn versions:set -DnewVersion=${getNewVersion(version)}"
            script.sh "mvn versions:commit"
        }
    }

    private static String getNewVersion(String currentVersion) {
          def splittedVersion= currentVersion.split('-')
        def parts = splittedVersion[0].tokenize('.')
        parts[-1] = (parts[-1].toInteger() + 1).toString()
        def newVersion = parts.join('.') + splittedVersion[-1]
        return newVersion
    }
}
