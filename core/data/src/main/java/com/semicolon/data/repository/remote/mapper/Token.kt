package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.TokenDto
import com.semicolon.domain.entity.Token


fun TokenDto.toEntity(): Token {
    return Token(token = token)
}