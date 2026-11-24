package com.connectify.steps

class Deploy extends BaseStep{

    def script


    Deploy(script) {
        super("Deploy")
        this.script = script
    }

    @Override
    void execute() {
        script.echo 'Executing deploy tasks'
        script.container('deploy') {
            script.withCredentials([
                    script.file(credentialsId: 'k8s-kubeconfig', variable: 'KUBECONFIG')
            ]) {
                script.input message: 'Do you want to continue?', ok: 'Yes, Continue'

                script.sh """
                helm version
                cat /var/run/secrets/kubernetes.io/serviceaccount/namespace
                helm upgrade --install messaging-service ./.devops/helm -n dk2359-b --set image.tag=vivek8539/messaging-service:0.0.2-SNAPSHOT
            """
            }
        }

    }
}
