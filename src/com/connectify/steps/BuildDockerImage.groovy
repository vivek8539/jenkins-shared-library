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
        script.echo "Application: ${pom.artifactId}"
        script.echo "Version: ${pom.version}"
        def ImageName = "${script.env.DOCKER_USERNAME}/${pom.artifactId}:${pom.version}"
        script.echo "Image Name: ${ImageName}"

        script.container('kaniko') {
            script.sh """
                /kaniko/executor --context . --dockerfile ${script.pwd()}/Dockerfile --destination ${ImageName} --insecure --skip-tls-verify
            """
        }
    }
}
