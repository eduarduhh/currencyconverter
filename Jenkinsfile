pipeline {
    agent any
    environment {
        DOCKER_IMAGE_TAG = "springboot-deploy:${env.BUILD_NUMBER}"
        PROJECT_VERSION = ''
    }
    stages {
        stage('Clone Repo') {
            steps {
                git url: 'https://github.com/eduarduhh/currencyconverter.git',
                    // credentialsId: 'springdeploy-user', // Uncomment if credentials are needed
                    branch: 'main'
            }
        }
        stage('Get Project Version') {
            steps {
                script {
                    PROJECT_VERSION = sh(
                        script: "grep '^version' build.gradle.kts | head -1 | cut -d '\"' -f2",
                        returnStdout: true
                    ).trim()
                    echo "📦 Project Version: ${PROJECT_VERSION}"
                }
            }
        }
        stage('Build Docker') {
            steps {
                script {
                    sh 'docker build -t springboot-deploy:${PROJECT_VERSION} .'
                    //dockerImage = docker.build("springboot-deploy:${PROJECT_VERSION}", "--ulimit nofile=4096:65535 --memory=4g .")
                }
            }
        }
        stage('Deploy Docker') {
            steps {
                withCredentials([string(credentialsId: 'exchangerates-api-key', variable: 'API_KEY')]) {
                    sh 'echo API_KEY="$API_KEY" > .env'
                    sh 'docker stop springboot-deploy || true && docker rm springboot-deploy || true'
                    sh "docker run --env-file .env --name springboot-deploy -d -p 8081:8080 springboot-deploy:${PROJECT_VERSION}"
                    sh 'rm .env || true'
                }
            }
        }
    }
    post {
        always {
            notifyBuild(currentBuild.result)
        }
        failure {
            script {
                currentBuild.result = 'FAILED'
            }
        }
    }
}

def notifyBuild(String buildStatus = 'STARTED') {
    buildStatus = buildStatus ?: 'SUCCESSFUL'
    def colorName = 'RED'
    def colorCode = '#FF0000'
    def now = new Date()
    def subject = "${buildStatus}, Job: ${env.JOB_NAME} FRONTEND - Deployment Sequence: [${env.BUILD_NUMBER}]"
    def summary = "${subject} - Check On: (${env.BUILD_URL}) - Time: ${now}"
    def subject_email = "Spring Boot Deployment"
    def details = """<p>${buildStatus} JOB </p>
        <p>Job: ${env.JOB_NAME} - Deployment Sequence: [${env.BUILD_NUMBER}] - Time: ${now}</p>
        <p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME}</a>"</p>"""

    emailext (
        to: "admin@gmail.com", // Replace with actual email
        subject: subject_email,
        body: details,
        recipientProviders: [[$class: 'DevelopersRecipientProvider']]
    )
}