package com.connectify

import com.connectify.steps.Approval
import com.connectify.steps.DockerImage
import com.connectify.steps.MavenBuild
import com.connectify.steps.StepRun;

class PipelineUtils {
    static List<StepRun> getStages(script) {
         [
                 new MavenBuild(script),
                 new DockerImage(),
                 new Approval(script)
        ]
    }
}
