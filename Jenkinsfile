pipeline {
  agent {
    docker {
      image 'gradle:jdk11'
      args '--user jenkins -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project'
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