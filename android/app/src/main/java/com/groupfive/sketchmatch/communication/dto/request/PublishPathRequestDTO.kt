package com.groupfive.sketchmatch.communication.dto.request

data class PublishPathRequestDTO(
    val roomId: Int,
    val pathPayload: String
)