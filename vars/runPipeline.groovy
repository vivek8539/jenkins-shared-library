def call() {
  echo "using shared library"
  def BUILDPOD_YAML = env.BUILDPOD_YAML
  pipeline {
    agent {
        kubernetes {
            cloud 'kubernetes'
            yaml BUILDPOD_YAML
        }
    }
    stages {
        stage('Build') {
            steps {
                container('maven') {
                    sh 'mvn --version'
                }
            }
        }
    }
  }
}
