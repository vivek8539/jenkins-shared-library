package com.connectify.steps

class Approval implements StepRun {

    def script

    Approval(script) {
        this.script = script
    }

    @Override
    def run() {
        approval()
    }

    def approval() {
        input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'

    }
}
