package aps.unip.usuarios;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import aps.unip.protocolo.Mensagem;
import aps.unip.tratamento.TratamentoRequisicao;
/*
 * Essa classe representa o cliente no servidor.
 */
public class Usuario {
	private String nome = null;
	private Integer id = 0;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	Mensagem mensagemInput;
	
	public Usuario(Socket socket) {
		try {	
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			mensagemInput = (Mensagem) input.readObject();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Esse Thread fica monitorando o InputStream.
	 */
	private Thread requisicaoListener = new Thread() {
		public void run() {
			for (;;) {
				try {
					mensagemInput = (Mensagem) input.readObject();
					TratamentoRequisicao tratamento = new TratamentoRequisicao();
					Mensagem mensagemOutput = tratamento.tratarRequisicao(mensagemInput,id);
					dispararMensagem(mensagemOutput);
				} catch (Exception e) {
					if(e.getMessage().equalsIgnoreCase("Connection reset")) {
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[USUARIO DESCONECTADO]");
						fecharUsuario();
						Usuarios.removeUsuario(getUsuarioId());
						break;
					}
					e.printStackTrace();
				}
			}
		}
	};
	
	/*
	 * Esse metodo dispara uma mensagem para o cliente.
	 * @param mensagemOutput, mensagem que vai ser enviada para o cliente.
	 */
	public void dispararMensagem(Mensagem mensagemOutput) {
		new Thread() {
			public void run() {
				try {
					output.writeObject(mensagemOutput);
					output.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	};
	
	/*
	 * Esse metodo inicia a Thread que fica monitorando o InputStream.
	 */
	public void iniciarRequisicaoListener() {
		requisicaoListener.start();
	}
	
	/*
	 * Esse metodo fecha o socket do cliente.
	 */
	public void fecharUsuario() {
		try {
			if(socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
	}

	public Mensagem getMensagemInput() {
		return mensagemInput;
	}

	public void setMensagemInput(Mensagem mensagemInput) {
		this.mensagemInput = mensagemInput;
	}
	
	public int getUsuarioId() {
		return id;
	}

	public void setUsuarioId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Usuario [nome=" + nome + "]";
	}
}
