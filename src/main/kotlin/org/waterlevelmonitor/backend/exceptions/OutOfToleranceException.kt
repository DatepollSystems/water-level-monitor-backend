package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
class OutOfToleranceException : Exception()