package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class OrganizationNotFoundException(msg: String = "Organization not found") : Exception(msg)