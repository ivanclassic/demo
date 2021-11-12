pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
                sh '''
                    cd LaucnherDemo
                    ls -lah
                    ./gradlew app:assembleDebug
                '''
            }
        }
    }
}
