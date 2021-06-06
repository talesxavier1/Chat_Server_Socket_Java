package aps.unip.tratamento;

import java.net.Socket;
import java.util.Map;

import aps.unip.daos.DAOMensagens;
import aps.unip.daos.DAOUserCadastro;
import aps.unip.daos.DAOUserLogin;
import aps.unip.enums.Requisicao;
import aps.unip.enums.Status;
import aps.unip.protocolo.Mensagem;
import aps.unip.usuarios.Usuario;
import aps.unip.usuarios.Usuarios;

/*
 * Essa classe trata as requisicoes de conexao.
 */
public class TratamentoConexao {
	
	/*
	 * Esse metodo cria um novo usuario e trata a requisicao (Cadastro ou Login).
	 * @param socket, novo socket que foi recebido.
	 */
	public void tratarConexao(Socket socket) {
		Usuario usuario = new Usuario(socket);
		
		if(usuario.getMensagemInput().getRequisicao() == Requisicao.CADASTRO) {
			Mensagem mensagemRespostaCadastro = new Mensagem();
			DAOUserCadastro cadastro = new DAOUserCadastro();
			if(cadastro.cadastrarUsuario(usuario.getMensagemInput().getMap())) {
				mensagemRespostaCadastro.setRequisicao(Requisicao.CADASTRO_REPLY);
				mensagemRespostaCadastro.setStatus(Status.CADASTRO_EFETUADO);
				mensagemRespostaCadastro.setParametros("mensagem", "cadastro efetuado");
				usuario.dispararMensagem(mensagemRespostaCadastro);
				try {
					Thread.sleep(2000);
					usuario.fecharUsuario();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				mensagemRespostaCadastro.setRequisicao(Requisicao.CADASTRO_REPLY);
				mensagemRespostaCadastro.setStatus(Status.CADASTRO_NAO_EFETUADO);
				mensagemRespostaCadastro.setParametros("mensagem", "usuario existente");
				usuario.dispararMensagem(mensagemRespostaCadastro);
				try {
					Thread.sleep(2000);
					usuario.fecharUsuario();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		else if (usuario.getMensagemInput().getRequisicao() == Requisicao.LOGIN) {
			DAOUserLogin login = new DAOUserLogin();
			Map<String, Object> retorno = login.validarUsuario((String) usuario.getMensagemInput().getParametro("email"),
                    (String) usuario.getMensagemInput().getParametro("senha"));
		
			if(retorno != null) {
				usuario.setNome( (String) retorno.get("nome"));
				usuario.setUsuarioId((Integer) retorno.get("id"));
				Usuarios.addUsuario(usuario);
				
				Mensagem mensagemRespostaLogin = new Mensagem();
				DAOMensagens daoMensagens = new DAOMensagens();
				Object[][] mensagensArquivadas = daoMensagens.buscarMensagensArquivadas(usuario.getUsuarioId());
				mensagemRespostaLogin.setRequisicao(Requisicao.LOGIN_REPLY);
				mensagemRespostaLogin.setStatus(Status.STATUS_OK);
				mensagemRespostaLogin.setMap(retorno);
				if (mensagensArquivadas != null) {
					mensagemRespostaLogin.setParametros("mensagensArquivadas", mensagensArquivadas);
				}
				mensagemRespostaLogin.setParametros("mensagem", "usuario logado");
				usuario.dispararMensagem(mensagemRespostaLogin);
				usuario.iniciarRequisicaoListener();
				
			}else {
				Mensagem mensagemRespostaLogin = new Mensagem();
				mensagemRespostaLogin.setRequisicao(Requisicao.LOGIN_REPLY);
				mensagemRespostaLogin.setStatus(Status.USUARIO_NAO_CADASTRADO);
				mensagemRespostaLogin.setParametros("mensagem", "Usuario nao cadastrado");
				usuario.dispararMensagem(mensagemRespostaLogin);
				
				try {
					Thread.sleep(2000);
					usuario.fecharUsuario();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
