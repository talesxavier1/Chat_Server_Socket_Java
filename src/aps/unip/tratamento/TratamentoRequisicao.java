package aps.unip.tratamento;


import aps.unip.daos.DAOMensagens;
import aps.unip.daos.DAOBuscarUsuarios;
import aps.unip.enums.Requisicao;
import aps.unip.enums.Status;
import aps.unip.protocolo.Mensagem;
import aps.unip.usuarios.Usuario;
import aps.unip.usuarios.Usuarios;

/*
 * Essa classe trata as requisicoes do cliente. 
 */
public class TratamentoRequisicao{
	private final String ERRO_USUARIO_OFFLINE = "Cannot invoke \"aps.unip.usuarios.Usuario.dispararMensagem(aps.unip.protocolo.Mensagem)\" because \"usuarioDestinatario\" is null";
	
	/*
	 * Esse metodo envia uma mensagem para um cliente.
	 * [Cliente online], A mensagem é encaminhada.
	 * [Cliente Offline], A mensagem é arquivada.
	 */
	private Mensagem enviarMensagem(Mensagem mensagemInput) {
		Mensagem mensagemDestinatario;
		Mensagem mensagemRemetenteReply;
		try {
			mensagemDestinatario = mensagemInput;
			mensagemDestinatario.setRequisicao(Requisicao.NOVA_MENSAGEM);
			mensagemDestinatario.setStatus(Status.NOVA_MENSAGEM);
			Usuario usuarioDestinatario = Usuarios.getUsuario((Integer) mensagemInput.getParametro("destinatario_id"));
			if (usuarioDestinatario != null) {
				System.out.println("[MENSAGEM ENVIADA DE"+ mensagemInput.getParametro("remetente_id")+" PARA "+ mensagemInput.getParametro("destinatario_id")+"]");
				usuarioDestinatario.dispararMensagem(mensagemDestinatario);
			}else {
				System.out.println("USUARIO NAO ESTA ONLINE [MENSAGEM ARQUIVADA]");
				DAOMensagens daoMensagens = new DAOMensagens();
				daoMensagens.arquivarMensagem(mensagemInput);
			}
			
			mensagemRemetenteReply = new Mensagem();
			mensagemRemetenteReply.setRequisicao(Requisicao.ENVIAR_MENSAGEM_REPLY);
			mensagemRemetenteReply.setStatus(Status.MENSAGEM_ENVIADA);
			mensagemRemetenteReply.setParametros("mensagem", "Mensagem envidad com sucesso!");
			return mensagemRemetenteReply;
		} catch (Exception e) {
			if(e.getMessage().equals(ERRO_USUARIO_OFFLINE)) {
				mensagemRemetenteReply = new Mensagem();
				mensagemRemetenteReply.setRequisicao(Requisicao.ENVIAR_MENSAGEM_REPLY);
				mensagemRemetenteReply.setStatus(Status.MENSAGEM_ARQUIVADA);
				mensagemRemetenteReply.setParametros("mensagem", "usuario offline, mensagem arquivada");
				return mensagemRemetenteReply;
			}else {
				e.printStackTrace();
				mensagemRemetenteReply = new Mensagem();
				mensagemRemetenteReply.setRequisicao(Requisicao.ENVIAR_MENSAGEM_REPLY);
				mensagemRemetenteReply.setStatus(Status.MENSAGEM_NAO_ENVIADA);
				mensagemRemetenteReply.setParametros("mensagem", "Erro ao enviar mensagem");
				return mensagemRemetenteReply;
			}

		}
	}
	
	/*
	 * Esse metodo busca os usuario.
	 * @param mensagemInput, mensagem com os dados da consulta.
	 * @param idSolicitante, id do
	 * @return 
	 * 		Mensagem, retorna a mensagem de resposta da consulta.
	 */
	private Mensagem buscarUsuario(Mensagem mensagemInput, int idSolicitante) {
		DAOBuscarUsuarios buscarUsuarios = new DAOBuscarUsuarios();
		Mensagem mensagemReply = new Mensagem();
		try {
			Object[][] retorno = buscarUsuarios.busacarUsuarios( (String) mensagemInput.getParametro("nome"), idSolicitante);
			if (retorno != null) {
				mensagemReply.setRequisicao(Requisicao.BUSCAR_USUARIO_REPLY);
				mensagemReply.setStatus(Status.USUARIOS_ENCONTRADOS);
				mensagemReply.setParametros("usuarios", retorno);
				return mensagemReply;
			}
			else {
				mensagemReply.setRequisicao(Requisicao.BUSCAR_USUARIO_REPLY);
				mensagemReply.setStatus(Status.NENHUM_USUARIO_ENCONTRADO);
				mensagemReply.setParametros("mensagem", "Nenhum usuario retornado do servidor.");
				return mensagemReply;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mensagemReply.setRequisicao(Requisicao.BUSCAR_USUARIO_REPLY);
			mensagemReply.setStatus(Status.STATUS_ERRO_SERVIDOR);
			mensagemReply.setParametros("mensagem", e);
			return mensagemReply;
		}

	}
	/*
	 * Esse metodo busca um usuario.
	 * @param mensagemInput, mensagem com os dados da consulta.
	 * @return 
	 * 		Mensagem, mensagem com os dados da consulta
	 */
	private Mensagem buscarUmUsuario(Mensagem mensagemInput) {
		DAOBuscarUsuarios buscarUsuarios = new DAOBuscarUsuarios();
		Object[] dadosUsuario = buscarUsuarios.buscarUmUsuario((int) mensagemInput.getParametro("idusuario"));
		Mensagem mensagemResposta = new Mensagem();
		if (dadosUsuario != null) {
			mensagemResposta.setRequisicao(Requisicao.BUSCAR_UM_USUARIO_REPLY);
			mensagemResposta.setStatus(Status.USUARIO_ENCONTRADO);
			mensagemResposta.setParametros("idContato", dadosUsuario[0]);
			mensagemResposta.setParametros("Nome", dadosUsuario[1]);
			mensagemResposta.setParametros("foto", dadosUsuario[2]);
			return mensagemResposta;
		}
		else {
			mensagemResposta.setRequisicao(Requisicao.BUSCAR_UM_USUARIO_REPLY);
			mensagemResposta.setStatus(Status.USUARIO_NAO_CADASTRADO);
			mensagemResposta.setParametros("mensagem", "Nenhum usuario encontrado");
			return mensagemResposta;
		}
	}
	
	/*
	 * Esse metodo faz um filtro das requisicoes do cliente.
	 * @param mensagemInput, Mensagem que o servidor recebeu.
	 * @param idSolicitante, ID do cliente solicitante.
	 */
	public Mensagem tratarRequisicao(Mensagem mensagemInput, int idSolicitante) {
		Requisicao requisicao = mensagemInput.getRequisicao();
		
		switch (requisicao) {
		case ENVIAR_MENSAGEM:
			return (enviarMensagem(mensagemInput));
			
		case BUSCAR_USUARIO:
			return (buscarUsuario(mensagemInput,idSolicitante));
		case BUSCAR_UM_USUARIO:
			return buscarUmUsuario(mensagemInput);
		default:
			break;
		}
		return null;
	}

}
