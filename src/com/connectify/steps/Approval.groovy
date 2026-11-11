package com.connectify.steps

class Approval implements StepRun {

    def script

    Approval(script) {
        this.script = script
    }

    @Override
    def run(stageName) {
        approval(stageName)
    }

    def approval(stageName) {
        script.input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'

    }
}
