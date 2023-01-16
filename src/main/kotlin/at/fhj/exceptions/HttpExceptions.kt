package at.fhj.exceptions

sealed class HttpException(message: String) : IllegalStateException(message)

class HttpUnauthorizedException(message: String) : HttpException(message)