def call(Map config = [:]) {
    def status = config.status ?: currentBuild.currentResult
    def emoji = status == 'SUCCESS' ? ':white_check_mark:' : ':x:'

    echo "${emoji} Build ${env.JOB_NAME} #${env.BUILD_NUMBER} finished with ${status}"
    echo "Build URL: ${env.BUILD_URL}"
}
