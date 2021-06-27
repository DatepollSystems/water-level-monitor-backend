package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Property not found")
class NotFoundException(property: String): Exception(message = "$property not found")