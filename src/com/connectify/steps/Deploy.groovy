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
            script.sh """
                helm version
            """
        }

    }
}
