CREATE SCHEMA BrainTalk

-- Criação da tabela de Usuários
CREATE TABLE BrainTalk.Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Nome VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Senha VARCHAR(255) NOT NULL,
    FotoPerfil VARBINARY(MAX) -- Caminho para a imagem de perfil
);

-- Criação da tabela de Tópicos de Discussão
CREATE TABLE BrainTalk.DiscussionTopics (
    TopicID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL, -- Chave estrangeira para o autor do tópico
    Titulo VARCHAR(255) NOT NULL,
    Descricao TEXT,
    DataCriacao DATETIME,
    Categoria VARCHAR(100),
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID)
);

-- Criação da tabela de Posts do Fórum
CREATE TABLE BrainTalk.ForumPosts (
    PostID INT PRIMARY KEY IDENTITY,
    UserID INT NOT NULL, -- Chave estrangeira para o autor do post
    Conteudo TEXT, -- Conteúdo do post
    TipoConteudo VARCHAR(50), -- Pode ser 'Texto', 'Imagem' ou 'Video', por exemplo
    ArquivoConteudo VARBINARY(MAX), -- Dados binários do conteúdo (imagem ou vídeo)
    DataPostagem DATETIME,
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID)
)

-- Criação da tabela de Curtidas
CREATE TABLE BrainTalk.Likes (
    LikeID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL, -- Chave estrangeira para o usuário que deu a curtida
    PostID INT NOT NULL, -- Chave estrangeira para o post que recebeu a curtida
    DataCurtida DATETIME,
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID),
    FOREIGN KEY (PostID) REFERENCES BrainTalk.ForumPosts(PostID)
);

-- Criação da tabela de Amigos
CREATE TABLE BrainTalk.Friends (
    FriendshipID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL, -- Chave estrangeira para o usuário que envia a solicitação de amizade
    FriendUserID INT NOT NULL, -- Chave estrangeira para o usuário que recebe a solicitação de amizade
    Status VARCHAR(20), -- Pendente, Aceito, Recusado, etc.
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID),
    FOREIGN KEY (FriendUserID) REFERENCES BrainTalk.Users(UserID)
);

-- Criação da tabela de Categorias de Discussão (se aplicável)
CREATE TABLE BrainTalk.DiscussionCategories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    NomeCategoria VARCHAR(100) NOT NULL,
    DescricaoCategoria TEXT
);

-- Criação da tabela de Respostas (se aplicável)
CREATE TABLE BrainTalk.Responses (
    ResponseID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL, -- Chave estrangeira para o autor da resposta
    TopicID INT NOT NULL, -- Chave estrangeira para o tópico relacionado
    ConteudoResposta TEXT,
    DataResposta DATETIME,
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID),
    FOREIGN KEY (TopicID) REFERENCES BrainTalk.DiscussionTopics(TopicID)
);

-- Criação da tabela de Materiais de Estudo com armazenamento de BLOBs
CREATE TABLE BrainTalk.StudyMaterials (
    MaterialID INT PRIMARY KEY IDENTITY,
    UserID INT NOT NULL, -- Chave estrangeira para o autor do material
    TituloMaterial VARCHAR(255) NOT NULL,
    DescricaoMaterial TEXT,
    Formato VARCHAR(50), -- PDF, vídeo, etc.
    Arquivo VARBINARY(MAX), -- Armazenamento do arquivo BLOB
    DataUpload DATETIME,
    FOREIGN KEY (UserID) REFERENCES BrainTalk.Users(UserID)
);

-- Inserir dados fictícios na tabela Users
INSERT INTO BrainTalk.Users (Nome, Email, Senha, FotoPerfil)
VALUES
    ('Usuário 1', 'usuario1@email.com', 'senha1', 00000), -- Dados binários da imagem de perfil 1
    ('Usuário 2', 'usuario2@email.com', 'senha2', 00000); -- Dados binários da imagem de perfil 2

-- Inserir dados fictícios na tabela DiscussionTopics
INSERT INTO BrainTalk.DiscussionTopics (UserID, Titulo, Descricao, DataCriacao, Categoria)
VALUES
    (1, 'Tópico 1', 'Descrição do Tópico 1', GETDATE(), 'Categoria A'),
    (2, 'Tópico 2', 'Descrição do Tópico 2', GETDATE(), 'Categoria B');

-- Inserir dados fictícios na tabela ForumPosts
INSERT INTO BrainTalk.ForumPosts (UserID, Conteudo, TipoConteudo, ArquivoConteudo, DataPostagem)
VALUES
    (1, 'Este é um post de texto.', 'Texto', NULL, GETDATE()), -- Post de texto
    (2, 'Este é um post de imagem.', 'Imagem', 0000, GETDATE()), -- Dados binários de uma imagem (JPG, por exemplo)
    (1, 'Este é um post de video.', 'Video', 0000, GETDATE()); -- Dados binários de um vídeo (AVI, por exemplo)

-- Inserir dados fictícios na tabela Likes
INSERT INTO BrainTalk.Likes (UserID, PostID, DataCurtida)
VALUES
    (1, 1, GETDATE()),
    (2, 2, GETDATE()),
    (1, 3, GETDATE());

-- Inserir dados fictícios na tabela Friends
INSERT INTO BrainTalk.Friends (UserID, FriendUserID, Status)
VALUES
    (1, 2, 'Aceito'),
    (2, 1, 'Aceito');

-- Inserir dados fictícios na tabela DiscussionCategories (se aplicável)
INSERT INTO BrainTalk.DiscussionCategories (NomeCategoria, DescricaoCategoria)
VALUES
    ('Categoria A', 'Descrição da Categoria A'),
    ('Categoria B', 'Descrição da Categoria B');

-- Inserir dados fictícios na tabela Responses (se aplicável)
INSERT INTO BrainTalk.Responses (UserID, TopicID, ConteudoResposta, DataResposta)
VALUES
    (1, 1, 'Resposta ao Tópico 1', GETDATE()),
    (2, 1, 'Outra resposta ao Tópico 1', GETDATE());

-- Inserir dados fictícios na tabela StudyMaterials
INSERT INTO BrainTalk.StudyMaterials (UserID, TituloMaterial, DescricaoMaterial, Formato, Arquivo, DataUpload)
VALUES
    (1, 'Material de Estudo 1', 'Descrição do Material 1', 'PDF', 0000, GETDATE()), -- Dados binários do arquivo PDF
    (2, 'Material de Estudo 2', 'Descrição do Material 2', 'Vídeo', 0000, GETDATE()); -- Dados binários do arquivo de vídeo

-- Visualizar todos os dados na tabela Users
SELECT * FROM BrainTalk.Users;

-- Visualizar todos os dados na tabela DiscussionTopics
SELECT * FROM BrainTalk.DiscussionTopics;

-- Visualizar todos os dados na tabela ForumPosts
SELECT * FROM BrainTalk.ForumPosts;

-- Visualizar todos os dados na tabela Likes
SELECT * FROM BrainTalk.Likes;

-- Visualizar todos os dados na tabela Friends
SELECT * FROM BrainTalk.Friends;

-- Visualizar todos os dados na tabela DiscussionCategories (se aplicável)
SELECT * FROM BrainTalk.DiscussionCategories;

-- Visualizar todos os dados na tabela Responses (se aplicável)
SELECT * FROM BrainTalk.Responses;

-- Visualizar todos os dados na tabela StudyMaterials
SELECT * FROM BrainTalk.StudyMaterials;



-- Excluir todos os dados na tabela Users
DELETE FROM BrainTalk.Users;

-- Excluir todos os dados na tabela DiscussionCategories
DELETE FROM BrainTalk.DiscussionCategories;

-- Excluir todos os dados na tabela DiscussionTopics
DELETE FROM BrainTalk.DiscussionTopics;

-- Excluir todos os dados na tabela Responses
DELETE FROM BrainTalk.Responses;

-- Excluir todos os dados na tabela ForumPosts
DELETE FROM BrainTalk.ForumPosts;

-- Excluir todos os dados na tabela Likes
DELETE FROM BrainTalk.Likes;

-- Excluir todos os dados na tabela Friends
DELETE FROM BrainTalk.Friends;

-- Excluir todos os dados na tabela StudyMaterials
DELETE FROM BrainTalk.StudyMaterials;



-- Excluir permanentemente a tabela Users
DROP TABLE BrainTalk.Users;

-- Excluir permanentemente a tabela DiscussionCategories
DROP TABLE BrainTalk.DiscussionCategories;

-- Excluir permanentemente a tabela DiscussionTopics
DROP TABLE BrainTalk.DiscussionTopics;

-- Excluir permanentemente a tabela Responses
DROP TABLE BrainTalk.Responses;

-- Excluir permanentemente a tabela ForumPosts
DROP TABLE BrainTalk.ForumPosts;

-- Excluir permanentemente a tabela Likes
DROP TABLE BrainTalk.Likes;

-- Excluir permanentemente a tabela Friends
DROP TABLE BrainTalk.Friends;

-- Excluir permanentemente a tabela StudyMaterials
DROP TABLE BrainTalk.StudyMaterials;
