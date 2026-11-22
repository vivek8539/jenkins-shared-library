package com.connectify.steps

class Pause extends BaseStep {

    def script

    Pause(script) {
        super("Pause")
        this.script = script
    }

    @Override
    void execute() {
        script.input message: 'Do you want to continue?', ok: 'Yes, Continue'
    }
}
