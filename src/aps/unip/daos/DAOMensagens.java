package aps.unip.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import aps.unip.conexao.SingletonConnection;
import aps.unip.protocolo.Mensagem;
/*
 * Essa Classe manipula as mensagens que nao foram enviadas.
 */
public class DAOMensagens {
	private static Connection connection = SingletonConnection.getConnection();
	
	/*
	 * Esse metodo arquiva a mensagem que nao foi enviada para o destinatario.
	 * @param mensagem, mensagem que nao foi enviada.
	 */
	public boolean arquivarMensagem(Mensagem mensagem) {
		PreparedStatement statement = null;
		
		try {
			String SQL = String.format(
					"INSERT INTO mensagens_arquivadas (remetente_id, destinatario_id, mensagem) VALUES (%s,%s,'%s');",
					mensagem.getParametro("remetente_id"),
					mensagem.getParametro("destinatario_id"),
					mensagem.getParametro("mensagem"));
			
			statement = connection.prepareStatement(SQL);
			statement.execute();
			connection.commit();
			return true;
			
		}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}finally {
			SingletonConnection.close(statement);
		}
	}
	
	/*
	 * Esse metodo busca todas as mensagem que foram arquivadas.
	 * @param idUsuario, ID do destinatario que nao recebeu as mensagens.
	 * @return
	 * 		Object[][], retorna o id do remetente e a mensagem que nao foi enviada.
	 * 		null, quando o destinatario nao tem nenhuma mensagem arquivada.
	 */
	public Object[][] buscarMensagensArquivadas(int idUsuario){
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		ResultSet resultSet = null;
		try {
			//
			String SQL = String.format("SELECT * FROM mensagens_arquivadas where destinatario_id = %s;", idUsuario);
			statement = connection.prepareStatement(SQL);
			resultSet = statement.executeQuery();
			
			ArrayList<Object> retornoBD = new ArrayList<Object>();
			while(resultSet.next()) {
				retornoBD.add(resultSet.getInt("remetente_id"));
				retornoBD.add(resultSet.getString("mensagem"));
			}
			int retornoBdSize = retornoBD.size();
			if(retornoBdSize > 2) {	
				Object[][] retorno = new Object[retornoBdSize/2][2];
				for (int i = 0; i < retornoBdSize/2; i++) {
					retorno[i][0] = retornoBD.get(0);
					retorno[i][1] = retornoBD.get(1);
					retornoBD.remove(0);
					retornoBD.remove(0);
				}
				//
				String SQL2 = String.format("DELETE FROM mensagens_arquivadas WHERE destinatario_id = %s;", idUsuario);
				statement2 = connection.prepareStatement(SQL2);
				statement2.execute();
				connection.commit();
				return retorno;
			}
			else {
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			SingletonConnection.close(statement, resultSet);
			SingletonConnection.close(statement2);
		}
		return null;
	}

}












