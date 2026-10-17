def call(Map config = [:], Closure body) {
    def label = validateToken(config.label ?: 'kubernetes', ~/^[a-zA-Z0-9][a-zA-Z0-9._-]*$/, 'label')
    def serviceAccount = validateToken(config.serviceAccount ?: 'jenkins-agent', ~/^[a-zA-Z0-9][a-zA-Z0-9-]*$/, 'serviceAccount')
    def defaultContainer = validateToken(config.defaultContainer ?: 'node', ~/^[a-zA-Z0-9][a-zA-Z0-9-]*$/, 'defaultContainer')
    def cpuRequest = validateToken(config.cpuRequest ?: '250m', ~/^[0-9]+m?$/, 'cpuRequest')
    def memoryRequest = validateToken(config.memoryRequest ?: '512Mi', ~/^[0-9]+(Mi|Gi)$/, 'memoryRequest')
    def cpuLimit = validateToken(config.cpuLimit ?: '1', ~/^[0-9]+m?$/, 'cpuLimit')
    def memoryLimit = validateToken(config.memoryLimit ?: '1Gi', ~/^[0-9]+(Mi|Gi)$/, 'memoryLimit')

    podTemplate(
        label: label,
        serviceAccount: serviceAccount,
        defaultContainer: defaultContainer,
        yaml: """
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins-agent: "true"
spec:
  serviceAccountName: ${serviceAccount}
  containers:
    - name: jnlp
      image: jenkins/inbound-agent:latest-jdk21
    - name: node
      image: node:22-alpine
      command:
        - sleep
      args:
        - infinity
      resources:
        requests:
          cpu: ${cpuRequest}
          memory: ${memoryRequest}
        limits:
          cpu: ${cpuLimit}
          memory: ${memoryLimit}
"""
    ) {
        node(label) {
            body()
        }
    }
}

// rejects any value that doesn't match the expected format, preventing YAML/pod-spec injection via config map
private String validateToken(String value, java.util.regex.Pattern pattern, String fieldName) {
    if (!(value ==~ pattern)) {
        error("k8sDynamicAgent: invalid value for '${fieldName}': ${value}")
    }
    return value
}
