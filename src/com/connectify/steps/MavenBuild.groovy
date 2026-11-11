package com.connectify.steps

class MavenBuild implements StepRun {


    def script

    MavenBuild(script) {
        this.script = script
    }

    @Override
    def run (stageName) {
        build();
    }

    def build() {
        script.echo 'Executing build tasks'
        script.bat 'mvn clean install -DskipTests=true' // sh in case of master
    }
}
