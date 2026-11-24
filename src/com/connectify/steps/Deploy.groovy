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

                script.sh '''
                helm version
                helm upgrade --install $APPLICATION ./.devops/helm -n dk2359-b --set image.tag=$IMAGE_TAG
            '''
            }
        }

    }
}
