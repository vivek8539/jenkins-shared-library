package com.connectify

import com.connectify.steps.Approval
import com.connectify.steps.DockerImage
import com.connectify.steps.MavenBuild
import com.connectify.steps.StepRun;

class PipelineUtils {
    static List<StepRun> getStages() {
         [
                 new MavenBuild(),
                 new DockerImage(),
                 new Approval()
        ]
    }
}
