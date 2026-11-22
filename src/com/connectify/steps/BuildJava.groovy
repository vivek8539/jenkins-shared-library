package com.connectify.steps

class BuildJava extends BaseStep {

    BuildJava() {
        super("Build")
    }

    @Override
    public void execute() {
        echo 'Executing build tasks'
        container('maven') {
            sh 'mvn clean install -DskipTests=true'
        }
    }
}
