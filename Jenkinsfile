pipeline {
  agent {
    docker {
      image 'gradle:jdk8'
      args '-u root -v "$PWD":/home/gradle/project'
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