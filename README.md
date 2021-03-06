<h1> Cliente Chat</h1>

<h4>Finalidade</h4>

Esse projeto é um trabalho da matéria de Redes de Computadores, proposto pela faculdade. O projeto tem a finalidade de colocar em prática os conceitos de comunicação TCP/IP entre um cliente e um servidor.

------------
<h4>Cliente</h4>

Esse projeto necessita de um cliente para funcionar. O projeto do cliente pode ser conferido [aqui](https://github.com/talesxavier1/Chat_Cliente_Socket_Java).

------------
<h4>Programas utilizados</h4>

-  [Eclipse 2019-12](https://www.eclipse.org/downloads/packages/release/2019-12 "Eclipse 2019-12")
-  [SQLiteStudio 3.3.3 ](https://sqlitestudio.pl/ "SQLiteStudio 3.3.3")

------------

<h4>Ferramentas utilizadas</h4>

-  [Java SE Development Kit 8](https://www.oracle.com/br/java/technologies/javase/javase-jdk8-downloads.html "Eclipse 2019-12")
-  [SQLite 3.35.5 ](https://www.sqlite.org/index.html "SQLite 3.35.5")

------------

#### Comunicação Cliente-Servidor.

O cliente e o servidor utilizam o protocolo TCP para a sua comunicação. Para construir uma conexão entre os dois pontos, utilizamos a biblioteca [java.net.Socket](https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html) do java. Para a troca de informações, criamos uma classe chamada [Mensagem](https://github.com/talesxavier1/Chat_Server_Socket_Java/blob/main/src/aps/unip/protocolo/Mensagem.java) que fica responsável por carregar os dados de requisição e resposta entre os dois pontos.  

------------
#### Banco de Dados.

O Banco de Dados utilizado no servidor foi o [SQLite 3.35.5 ](https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html). Os dados dos usuários cadastrados e as mensagens que não foram encaminhadas para os destinatários são armazenados no arquivo [BancoDeDados.db](https://github.com/talesxavier1/Chat_Server_Socket_Java/blob/main/BD/BancoDeDados.db). As imagens abaixo representam a estrutura das tabelas contidas no banco de dados.


- <b><h5> Tabela usuário</h5></b>

<a href="https://imgur.com/cP2pE5h"><img src="https://i.imgur.com/cP2pE5h.png" title="source: imgur.com" /></a>

- <b><h5> Mensagens arquivadas</h5></b>

<a href="https://imgur.com/jrdHNv6"><img src="https://i.imgur.com/jrdHNv6.png" title="source: imgur.com" /></a>

------------
#### Funcionalidades.

O Servidor trabalha com base nas requisições que partem do [cliente](https://github.com/talesxavier1/Chat_Cliente_Socket_Java). Após a conexão ser estabelecida, O cliente pode enviar [mensagens](https://github.com/talesxavier1/Chat_Server_Socket_Java/blob/main/src/aps/unip/protocolo/Mensagem.java) para o servidor contendo o que deve ser feito e os dados necessários para a execução
Cada funcionalidade do servidor corresponde a um tipo de requisição e cada requisição possui uma resposta que é enviada ao cliente.

- [Funcionalidades.](#Funcionalidades)
  * [Modelo.](#Modelo)
  * [Cadastro.](#Cadastro)
    * Requisição.
    * Resposta de sucesso
    * Resposta de erro.
  * [Login.](#Login)
    * Requisição de login.
    * Resposta de sucesso.
    * Usuário não cadastrado.
  * [Buscar um usuário.](#Buscar-um-usuário)
    * Requisição Buscar um Usuário.
    * Resposta de Usuário Encontrado.
    * Resposta de Usuário Não Encontrado.
  * [Buscar Usuários.](#Buscar-Usuários)
    * Requisicao Buscar Usuários.
    * Resposta Usuários Encontrados.
    * Resposta Nenhum Usuário Encontrado.
  * [Enviar Mensagem.](#Enviar-Mensagem)
    * Requisiçao Enviar Mensagem.
    * Resposta Mensagem Enviada.
    * Resposta Mensagem Arquivada.

<br>

##### Modelo.

As requisições e respostas possuem uma estrutura definida. A imagem abaixo ilustra a estrutura básica das requisições.

<a href="https://imgur.com/mlzBNiW"><img src="https://i.imgur.com/mlzBNiW.png" title="source: imgur.com" /></a>

- REQUISIÇÃO: Esse campo carrega um enumerador que dirá ao servidor o que deve ser executado. Os possíveis valores para esse campo podem ser observados [aqui](https://github.com/talesxavier1/Chat_Server_Socket_Java/blob/main/src/aps/unip/enums/Requisicao.java).
- STATUS: Esse campo só terá valor atribuído nas resposta de requisição que partem do servidor para o cliente. Os possíveis valores para esse campo podem ser observados [aqui](https://github.com/talesxavier1/Chat_Server_Socket_Java/blob/main/src/aps/unip/enums/Status.java).
- Parâmetro: Esse campo é um [Map](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html) com chave do tipo [String](https://docs.oracle.com/javase/7/docs/api/java/lang/String.html) e valor do tipo [Object](https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html).

##### Cadastro.

O servidor pode fazer o cadastro de um novo usuário. Para fazer o cadastro, o servidor espera uma requisição que a imagem abaixo ilustra.


- Requisição.

<a href="https://imgur.com/6KrYK22"><img src="https://i.imgur.com/6KrYK22.png" title="source: imgur.com" /></a>


- Resposta de sucesso.

<a href="https://imgur.com/4MONePD"><img src="https://i.imgur.com/4MONePD.png" title="source: imgur.com" /></a>

- Resposta de erro.

<a href="https://imgur.com/h0QaoJ2"><img src="https://i.imgur.com/h0QaoJ2.png" title="source: imgur.com" /></a>

Essa resposta pode ocorrer caso algum dado esteja faltando ou um usuário já esteja cadastrado com o e-mail enviado. As informações sobre o erro do cadastro ficam dentro do parâmetro mensagem.


##### Login.

O servidor pode executar o login de usuários cadastrados. A requisição e reposta do login são ilustradas pelas imagens abaixo.

- Requisição de login.

<a href="https://imgur.com/g4oXCKu"><img src="https://i.imgur.com/g4oXCKu.png" title="source: imgur.com" height="195" width="295" /></a>

- Respostas de sucesso.

<a href="https://imgur.com/UAKVpvs"><img src="https://i.imgur.com/UAKVpvs.png" title="source: imgur.com" height="384" width="364" /></a>

Em caso de login efetuado, o cliente pode receber dois tipos de respostas.

|Tipo da mensagem|Ocasião|
| ------------ | ------------ |
|Sem mensagens arquivadas|  Caso não tenha recebido nenhuma mensagem enquanto estava off-line. |
|Com mensagem arquivada|  caso tenha recebido alguma mensagem enquanto estava off-line. |


- Usuário não cadastrado.

<a href="https://imgur.com/nWAwxDU"><img src="https://i.imgur.com/nWAwxDU.png" title="source: imgur.com" height="150" width="320" /></a>

<br>

##### Buscar um usuário

O cliente pode enviar uma requisição de buscar um usuário para o servidor. A execução dessa requisição busca o usuário com base no id enviado.

- Requisição Buscar um Usuário.

<a href="https://imgur.com/8mPdWKL"><img src="https://i.imgur.com/8mPdWKL.png" title="source: imgur.com" /></a>

- Resposta de Usuário Encontrado.

<a href="https://imgur.com/kCYNITd"><img src="https://i.imgur.com/kCYNITd.png" title="source: imgur.com" /></a>

- Resposta de Usuário Não Encontrado.

<a href="https://imgur.com/RYbtoBD"><img src="https://i.imgur.com/RYbtoBD.png" title="source: imgur.com" /></a>

<br>

##### Buscar Usuários.

O cliente pode enviar uma requisição de buscar usuário para o servidor. A execução dessa requisição busca o usuário com base no nome enviado.

- Requisicao Buscar Usuários.

<a href="https://imgur.com/ovrRmXD"><img src="https://i.imgur.com/ovrRmXD.png" title="source: imgur.com" /></a>

- Resposta Usuários Encontrados.

<a href="https://imgur.com/5ioZTzq"><img src="https://i.imgur.com/5ioZTzq.png" title="source: imgur.com" height="150" width="450" /></a>

O parâmetro usuarios consiste em um [Array]() de Objetos.

|ID usuario|Nome Usuário|Foto Usuário|
| ------------ | ------------ | ------------ |
|  usuarios[linha][0] |usuarios[linha][1]   |usuarios[linha][2]   |

<br>
- Resposta Nenhum Usuário Encontrado.

<a href="https://imgur.com/3mi85BC"><img src="https://i.imgur.com/3mi85BC.png" title="source: imgur.com" /></a>

##### Enviar Mensagem.

O usuário pode enviar uma requisição de encaminhamento de mensagem. O servidor irá enviar a mensagem para o destinatário, se ele estiver online. Caso o Cliente estiver Off-line, a mensagem será arquivada.

- Requisiçao Enviar Mensagem.

<a href="https://imgur.com/We9OLvp"><img src="https://i.imgur.com/We9OLvp.png" title="source: imgur.com" /></a>

- Resposta Mensagem Enviada.

<a href="https://imgur.com/dyusoz3"><img src="https://i.imgur.com/dyusoz3.png" title="source: imgur.com" /></a>

- Resposta Mensagem Arquivada.

<a href="https://imgur.com/UWKs8GD"><img src="https://i.imgur.com/UWKs8GD.png" title="source: imgur.com" /></a>






