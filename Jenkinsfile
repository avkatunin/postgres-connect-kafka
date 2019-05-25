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
                
                GIT_COMMIT_DESC = sh (
                    script: 'git log -1 --pretty=format:\'%s\' ${GIT_COMMIT}',
                    returnStdout: true
                ).trim()
                
                echo "${GIT_COMMIT}"
                echp "${GIT_COMMIT_DESC}"
                sh 'makedir -p ${GIT_COMMIT_DESC}'
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
    }
}
