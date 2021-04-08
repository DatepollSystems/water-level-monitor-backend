package org.waterlevelmonitor.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.ACCEPTED)
class MaxAmountOfMeasurementsReachedException(msg: String = "Maximum amount of measurements reached") : Exception(msg)