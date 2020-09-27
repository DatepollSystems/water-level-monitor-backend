package org.waterlevelmonitor.backend.exceptions

import java.lang.Exception

class RiverNotFoundException(msg: String = "River not found") : Exception(msg)