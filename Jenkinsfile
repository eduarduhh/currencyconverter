node {
    def WORKSPACE = "/var/lib/jenkins/workspace/springboot-deploy"
    def dockerImageTag = "springboot-deploy${env.BUILD_NUMBER}"

    try {
        stage('Cleanup') {  // Novo stage para limpar
            sh 'rm -f .env || true'  // Remove .env se existir
        }

        stage('Clone Repo') {
            git url: 'https://github.com/eduarduhh/currencyconverter.git',
                branch: 'main'
        }

        stage('Build docker') {
            // Adicione .dockerignore dinamicamente se necessário
            sh 'echo ".env" > .dockerignore || true'  // Ignora .env no build
            dockerImage = docker.build("springboot-deploy:${env.BUILD_NUMBER}", "--ulimit nofile=4096:65535 --memory=4g .")
        }

        stage('Deploy docker') {
            echo "Docker Image Tag Name: ${dockerImageTag}"
            withCredentials([string(credentialsId: 'exchangerates-api-key', variable: 'API_KEY')]) {
                sh 'echo "$API_KEY" > .env'
                sh "docker stop springboot-deploy || true && docker rm springboot-deploy || true"
                sh "docker run --name springboot-deploy -d -p 8081:8080 --env-file .env springboot-deploy:${env.BUILD_NUMBER}"
                sh "rm -f .env || true"  // Descomente e ajuste para sempre remover
            }
        }
    } catch(e) {
        throw e
    } finally {
        // Cleanup final opcional
        sh 'rm -f .env || true'
    }
}