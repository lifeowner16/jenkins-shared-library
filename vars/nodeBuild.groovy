def call(Map config = [:]) {
    def nodeContainer = config.container ?: 'node'
    def testCommand = config.testCommand ?: 'npm test'
    def buildCommand = config.buildCommand ?: 'npm run build'

    stage('Node Build') {
        container(nodeContainer) {
            sh 'npm ci --no-audit --no-fund'
            def lintStatus = sh(script: 'npm run lint', returnStatus: true)
            if (lintStatus != 0) {
                unstable('Lint checks failed')
            }
            sh testCommand
            sh buildCommand
        }
    }
}
