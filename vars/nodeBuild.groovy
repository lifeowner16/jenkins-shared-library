def call(Map config = [:]) {
    def nodeContainer = config.container ?: 'node'
    def testCommand = config.testCommand ?: 'npm test'
    def buildCommand = config.buildCommand ?: 'npm run build'

    stage('Node Build') {
        container(nodeContainer) {
            sh """
              npm ci --no-audit --no-fund
              npm run lint || true
              ${testCommand}
              ${buildCommand}
            """
        }
    }
}
