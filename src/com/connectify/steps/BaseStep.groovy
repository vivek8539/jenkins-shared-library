package com.connectify.steps

abstract class BaseStep implements StepRun {

    protected String step;

    BaseStep(String step) {
        this.step = step
    }

    String getStep() {
        return step
    }
}
