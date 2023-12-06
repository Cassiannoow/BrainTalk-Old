package Models

import java.io.Serializable

data class User(
    val name: String, // Nome do usuário
    val email: String, // Endereço de e-mail do usuário
    val password: String, // Senha do usuário
    val photo: String, // Imagem de perfil codificada em base64
    val biograpy: String,
    val username: String,
    val bannerPhoto: String
): Serializable
