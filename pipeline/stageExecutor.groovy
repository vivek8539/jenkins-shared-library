def executeStage(String stageName) {
    if (stageName == 'Build') {
        build()
    } else if (stageName == 'Pause') {
        input message: "Do you want to proceed with ${stageName} stage?", ok: 'Yes'
    }
    else {
        echo "Executing ${stageName} stage"
    }
}

def build() {
    echo 'Executing build tasks'
    sh 'mvn clean install'
}

return this
