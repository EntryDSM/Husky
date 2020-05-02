pipeline {
    agent {
        docker { image 'gradle:jdk11' }
    }
    stages {
        stage('Gradle Build') {
            steps {
                sh 'gradle build'
            }
        }
    }
}
