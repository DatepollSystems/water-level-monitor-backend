package at.fhj.features.auth

import at.fhj.database.Database
import at.fhj.exceptions.HttpUnauthorizedException
import at.fhj.features.auth.db.Token
import at.fhj.features.auth.db.User
import at.fhj.features.auth.dto.LoginRequestDto
import at.fhj.features.auth.dto.LoginResponseDto
import at.fhj.features.auth.dto.RefreshRequestDto
import at.fhj.features.auth.dto.RefreshResponseDto
import at.fhj.features.auth.utils.generateJWT
import at.fhj.features.auth.utils.generateRefreshToken
import com.moshbit.katerbase.equal
import java.util.*

object AuthService {

    fun login(dto: LoginRequestDto): LoginResponseDto {
        val user = Database.users.findOne(User::email equal dto.email)
        if (user == null || !user.checkPassword(dto.password)) throw HttpUnauthorizedException("Bad credentials")

        val accessToken = generateJWT(user._id)
        val refreshToken = generateRefreshToken(user._id)

        return LoginResponseDto(accessToken, refreshToken.token)
    }

    fun refreshToken(dto: RefreshRequestDto): RefreshResponseDto {
        val token = Database.tokens.findOne(Token::token equal dto.refreshToken)

        if (token == null || token.expiresAt.before(Date())) throw HttpUnauthorizedException("Invalid refreshToken")

        Database.tokens.deleteOne(Token::_id equal token._id)

        val accessToken = generateJWT(token.userId)
        val refreshToken = generateRefreshToken(token.userId)

        return RefreshResponseDto(accessToken, refreshToken.token)
    }
}