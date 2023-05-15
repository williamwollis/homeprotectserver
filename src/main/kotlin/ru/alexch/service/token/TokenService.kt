package ru.alexch.service.token

import ru.alexch.service.token.TokenClaim
import ru.alexch.service.token.TokenConfig


interface TokenService {

    fun generate(
        config: TokenConfig,
        vararg claim: TokenClaim
    ): String
}