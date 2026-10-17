package org.devopsbuilds

class Utils implements Serializable {
    static String imageTag(String buildNumber, String gitSha) {
        def shortSha = gitSha ? gitSha.take(7) : 'nogit'
        return "${buildNumber}-${shortSha}"
    }
}
