package com.connectify

import com.connectify.steps.BuildJava
import com.connectify.steps.StepRun;

class PipelineUtils {

    List<StepRun> getStages(script) {
         [
                 new BuildJava(script)
        ]
    }
}
