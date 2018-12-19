package info.tommarsh.core.network

sealed class NetworkException(message: String) : Exception(message) {
    class NoResponseException : NetworkException("No Response from sever. Please try again")
    class NoInternetException : NetworkException("It seems you have no internet. Please try again.")
    class ServerException : NetworkException("Error with the server. Please try again later")
}