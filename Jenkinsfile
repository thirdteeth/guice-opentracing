#!groovy
pipeline {
    agent {
        docker {
            image 'maven:3.5.2-jdk-8'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -C -e -U clean install'
            }
        }
    }
}