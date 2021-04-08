package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class LocationNotFoundException(msg: String = "Location not found") : Exception(msg)