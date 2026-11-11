package com.connectify.steps

class MavenBuild implements StepRun {

    @Override
    def run () {
        build();
    }

    def build() {
        echo 'Executing build tasks'
        bat 'mvn clean install -DskipTests=true' // sh in case of master
    }
}
