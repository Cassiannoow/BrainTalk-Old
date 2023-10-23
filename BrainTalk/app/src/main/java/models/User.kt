package models
data class User(
    val userID: Int,  // ID do usuário
    val nome: String, // Nome do usuário
    val email: String, // Endereço de e-mail do usuário
    val senha: String, // Senha do usuário
    val fotoPerfil: String // Imagem de perfil codificada em base64
)
