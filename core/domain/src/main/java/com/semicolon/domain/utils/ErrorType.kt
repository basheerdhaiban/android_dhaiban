package com.semicolon.domain.utils

open class DhaibanException() : Exception()
open class InternetException : DhaibanException()
class BadRequestException() : Exception()
class BadRequestExceptionTest(message: String) : Exception(message)
class LocalStringFileNotFoundException() : Exception()

open class AuthorizationException(message: String) : Exception(message) {
    class InvalidUsernameException(message: String) : AuthorizationException(message)
    class InvalidPasswordException(message: String) : AuthorizationException(message)
    class InvalidEmailException(message: String) : AuthorizationException(message)
    class InvalidPhoneException(message: String) : AuthorizationException(message)
    class InvalidInputsException(message: String) : AuthorizationException(message)
    class InvalidOtpCodeException(message: String) : AuthorizationException(message)
    class AccountActivationException(message: String) : AuthorizationException(message)
    class UserNotFoundException(message: String) : AuthorizationException(message)
}