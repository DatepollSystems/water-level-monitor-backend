package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason="Username or password incorrect")
class UsernameOrPasswordIncorrectException: Exception()