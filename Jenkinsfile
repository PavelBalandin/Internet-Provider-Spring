pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-8'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}