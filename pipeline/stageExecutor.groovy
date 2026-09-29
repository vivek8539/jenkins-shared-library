def executeStage(String stageName) {
    if (stageName == 'Build') {
        build()
    } else {
        echo "Executing ${stageName} stage"
    }
}

def build() {
    echo 'Executing build tasks'
    sh 'mvn clean install'
}

return this
