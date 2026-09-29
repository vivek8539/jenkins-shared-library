def call() {
  echo "using shared library"
  def BUILDPOD_YAML = env.BUILDPOD_YAML
  node {
      stage('Generate Pipeline') {
          stagesList = ['Build', 'Test', 'Deploy']
      }
  }
  podTemplate(label: 'build-pod', yaml: BUILDPOD_YAML) {
      node ('build-pod') {
          stagesList.each { stageName ->
              stage(stageName) {
                  echo "Executing ${stageName} stage"
                  // Add your build, test, or deploy logic here
              }
          }
      }
  }
}
