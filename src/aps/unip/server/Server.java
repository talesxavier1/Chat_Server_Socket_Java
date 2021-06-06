package aps.unip.server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import aps.unip.conexao.SingletonConnection;
import aps.unip.tratamento.TratamentoConexao;

/*
 * Essa é a classe principal do servidor.
 */

public class Server {
	private ServerSocket serverSocket = null;

	/*
	 * Esse metodo cria um serverSocket.
	 * 
	 * @param port, porta que o servidor vai usar.
	 */
	private void criarServerSocket(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/*
	 * Esse metodo aceita as novas conexoes.
	 */
	private Socket esperaConexao() throws IOException {
		return serverSocket.accept();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		JLabel lbJLabel = new JLabel("Servidor OFF");
		lbJLabel.setSize(100, 20);
		lbJLabel.setLocation(50, 20);
		frame.add(lbJLabel);

		JButton button = new JButton("Iniciar Servidor");
		button.setSize(150, 40);
		button.setLocation(15, 100);
		frame.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							try {
								Server server = new Server();
								server.criarServerSocket(80);
								@SuppressWarnings("unused")
								SingletonConnection bdConnection = new SingletonConnection();
								for (;;) {
									System.out.println("[SERVIDOR AGUARDANDO NOVA CONEXÃO]");
									Socket socket = server.esperaConexao();
									System.out.println("[CLIENTE CONECTADO]");
									new Thread() {
										public void run() {
											TratamentoConexao conexao = new TratamentoConexao();
											conexao.tratarConexao(socket);
										}
									}.start();
									System.out.println("//-----------------------//");
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}.start();
					
					button.setEnabled(false);
					lbJLabel.setText("Servidor ON");

			}
		});

	}
}
