pipeline {
  agent {
    docker {
      image 'gradle:6.3.0-jdk8'
      args '-u root -v /home/entrydsm/g radle/project:/home/gradle/project'
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