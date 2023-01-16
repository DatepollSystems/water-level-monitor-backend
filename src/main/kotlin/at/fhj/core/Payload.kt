package at.fhj.core

interface Dto

interface ResponsePayload<T : Dto> {
    fun toResponse(): T
}

fun <T : Dto> Iterable<ResponsePayload<T>>.toResponse() = map { it.toResponse() }