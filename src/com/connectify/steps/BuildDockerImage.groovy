package com.connectify.steps

class BuildDockerImage extends BaseStep {

    def script

    BuildDockerImage(script) {
        super("Build Docker Image")
        this.script = script
    }

    @Override
    void execute() {
        def version = readMavenPom(file: 'pom.xml').version
        script.echo "Application: ${pom.artifactId}"
        script.echo "Version: ${version}"
        def ImageName = "${env.DOCKER_USERNAME}/${pom.artifactId}:${version}"
        script.echo "Image Name: ${ImageName}"

        script.container('kaniko') {
            script.sh """
                /kaniko/executor --context . --dockerfile ${script.pwd()}/Dockerfile --destination ${ImageName} --insecure --skip-tls-verify
            """
        }
    }
}
