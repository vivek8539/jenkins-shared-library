package com.connectify.steps

class BuildJava extends BaseStep {

    def script

    BuildJava() {
        super("Build")
        this.script = script
    }

    @Override
    public void execute() {
        script.echo 'Executing build tasks'
        script.container('maven') {
            script.sh 'mvn clean install -DskipTests=true'
        }
    }
}
