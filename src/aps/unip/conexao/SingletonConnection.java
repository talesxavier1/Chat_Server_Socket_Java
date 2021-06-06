package aps.unip.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Essa classe faz a conexao com o banco de dados.
 */
public class SingletonConnection {
	
	private static String URL = "jdbc:sqlite:BD/BancoDeDados.db";
	private static Connection connection = null;
	
	static {
		connect();
	}
	
	public SingletonConnection() {
		
	}
	
	/*
	 * Conecta com o banco de dados.
	 */
	private static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			if (connection == null) {
				connection = DriverManager.getConnection(URL);
				connection.setAutoCommit(false);
				System.out.println("[BANCO DE DADOS CONECTADO]");
			}
		} catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Esse metodo retorna a conexao com o banco de dados.
	 */
	public static Connection getConnection() {
		return connection;
	}
	
	/*
	 * Esse metodo fecha o statement que foi aberta na operacao.
	 * @param statement, Statement que foi aberto na operacao.
	 */
	public static void close(PreparedStatement statement) {
		try {
			if( statement != null) {
				statement.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Esse metodo fecha o statement e o resultSet que foram abertos na operacao.
	 * @param statement, Statement que foi aberto na operacao.
	 * @param resultSet, resultSet que foi aberto na operacao.
	 */
	public static void close(PreparedStatement statement, ResultSet resultSet) {
		try {
			if(resultSet != null) {
				
				resultSet.close();
			}
			if(statement != null) {				
				statement.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
