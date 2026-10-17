def call(String name = 'developer') {
    echo "Hello ${name}, running on Jenkins dynamic Kubernetes agent."
    sh 'hostname'
}
