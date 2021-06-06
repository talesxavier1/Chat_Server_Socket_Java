package aps.unip.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import aps.unip.conexao.SingletonConnection;
/*
 * Essa classe faz a manipulacao dos usuarios no banco de dados.
 */
public class DAOBuscarUsuarios {
	private Connection connection = SingletonConnection.getConnection();
	
	/*
	 * Esse metodo busca um usuario no banco de dados.
	 * @param idUsuario, id do usuario que foi solicitado.
	 * @return
	 * 		Object[], retorna todos os dados do usuario em um array de objetos.
	 * 		null, retorna nulo quando nenhum usuario é encontrado.
	 */
	public Object[] buscarUmUsuario(int idUsuario) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = SingletonConnection.getConnection();
		try {
			String SQL = String.format("SELECT * FROM usuario WHERE usuario_id = %s", idUsuario);
			statement = connection.prepareStatement(SQL);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				Object[] retorno = new Object[3];
				retorno[0] = resultSet.getInt("usuario_id");
				retorno[1] = resultSet.getString("usuario_nome");
				retorno[2] = resultSet.getBytes("usuario_foto");
				return retorno;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			SingletonConnection.close(statement, resultSet);
		}
	}
	
	/*
	 * Esse metodo busca todos os contatos com o nome que foi enviado e com o ID diferente do solicitante.
	 * @param nome, nome do usuario que o cliente esta procurando.
	 * @return
	 * 		Object[][], retorna todos os dados dos usuarios que tem um nome semelhante ao procurado.
	 * 		null, retorna nulo quando nenhum usuario é encontrado.
	 */
	public Object[][] busacarUsuarios(String nome, int idSolicitante){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<Object> resultados = new ArrayList<Object>();
		try {
			String SQL = "SELECT * FROM usuario WHERE usuario_nome LIKE '%"+nome+"%' AND usuario_id <> "+idSolicitante+";";
		
			statement = connection.prepareStatement(SQL);
			resultSet = statement.executeQuery();

			
			while (resultSet.next()) {
				resultados.add(resultSet.getString("usuario_id"));
				resultados.add(resultSet.getString("usuario_nome"));
				resultados.add(resultSet.getBytes("usuario_foto"));
			}
			int listSize = resultados.size();

			Object[][] retorno = new Object[listSize/3][3];
			

			if (listSize >= 2) {
				for (int i = 0 ; i < ((listSize)/3) ; i++) {
					retorno[i][0] = resultados.get(0);
					retorno[i][1] = resultados.get(1);
					retorno[i][2] = resultados.get(2);
					resultados.remove(0);
					resultados.remove(0);
					resultados.remove(0);
				}
				return retorno;
			}
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
