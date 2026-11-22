package com.connectify.steps

class BuildJava extends BaseStep {

    BuildJava() {
        super("Build")
    }

    @Override
    def run() {
        echo 'Executing build tasks'
        container('maven') {
            sh 'mvn clean install -DskipTests=true'
        }
    }
}
