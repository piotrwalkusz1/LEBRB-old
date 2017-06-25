pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew test'
      }
    }
    stage('Cucumber') {
      steps {
        sh './gradlew cucumber'
      }
    }
  }
}