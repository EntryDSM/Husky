pipeline {
  agent {
    docker {
      image 'gradle:jdk11'
      args '--user "$(id -u):$(id -g)" -v /etc/passwd:/etc/passwd:ro -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project'
    }

  }
  stages {
    stage('Gradle Build') {
      steps {
        sh 'gradle build'
      }
    }

  }
}