#!groovy

pipeline {
    agent { label 'master' }
    tools {
        maven 'maven'
    }
    stages {
        stage('checkout') {
            steps {
                git branch: 'jenkins', credentialsId: 'ef993206-01aa-4558-b234-fe6535943384', url: 'https://github.com/avkatunin/postgres-connect-kafka.git'
            }
        }
        stage('compile') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('test') {
            steps {
                sh 'ls'
                sh 'pwd'
            }
        }
        stage('delivery') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                echo "${GIT_COMMIT}"
                def commit_name = "${GIT_COMMIT}"
                sh 'makedir -p ${commit_name}'
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
    }
}
