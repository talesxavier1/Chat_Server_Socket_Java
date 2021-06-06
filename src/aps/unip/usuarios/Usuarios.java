package aps.unip.usuarios;

import java.util.HashMap;
import java.util.Map;
/*
 * Essa classe armazena todos os usuarios que estao online no servidor.
 */
public class Usuarios {
	private static Map<Integer, Usuario> usuariosOnLine = new HashMap<>();
	
	/*
	 * Adiciona um usuario no Map.
	 * @param usuario, Usuario que vai ser adicionado no Map.
	 */
	public static void addUsuario(Usuario usuario) {
		usuariosOnLine.put(usuario.getUsuarioId(), usuario);
	}
	
	/*
	 * Esse metodo busca um usuario.
	 * @param id, id do usuario que vai ser procurado.
	 * @return
	 * 		Usuario, quando o usuario é encontrado.
	 * 		null, quando nenhum usuario é encontrado.
	 */
	public static Usuario getUsuario(int id) {
		return usuariosOnLine.get(id);
	}
	
	/*
	 * Remove um usuario do Map.
	 * @param id, ID do usuario que vai ser removido.
	 */
	public static void removeUsuario(int id) {
		usuariosOnLine.remove(id);
	}	
}
