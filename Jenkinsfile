pipeline {
  agent {
    docker {
      args '-u root -v /home/entrydsm/g radle/project:/home/gradle/project'
      image 'gradle:jdk8'
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