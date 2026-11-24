package com.connectify.steps

class PipelineUtils {

    List<StepRun> getStages(script) {
         [
//                 new BuildJava(script),
//                 new BuildDockerImage(script),
//                 new Pause(script),
                 new Deploy(script)
        ]
    }
}
