package io.bom.makBase.exception

class NotFoundException(
    override val message: String?
) : RuntimeException(message)
