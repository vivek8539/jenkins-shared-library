package com.connectify

import com.connectify.steps.BuildJava
import com.connectify.steps.StepRun;

class PipelineUtils {
    static List<StepRun> getStages() {
         [
                 new BuildJava()
        ]
    }
}
