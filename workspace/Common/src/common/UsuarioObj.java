package common;

import java.io.Serializable;
import java.util.ArrayList;

public class UsuarioObj implements Serializable{

	private static final long serialVersionUID = 4385616843278653443L;
	private String nombre;
	private String nick;
	private String password;
	private ArrayList<Trino> trinosLista;
	private ArrayList<String> seguidores; //Se recogen los nicks de cada usuario, ya que son unicos.

	
	public UsuarioObj() {
		
		nombre = "";
		nick = "";
		password = "";
		trinosLista = new ArrayList<Trino>();
		seguidores = new ArrayList<String>();		
	}
	
	public UsuarioObj(String nombre, String nick, String password) {
		
		this.nombre = nombre;
		this.nick = nick;
		this.password = password;
		this.trinosLista = new ArrayList<Trino>();
		this.seguidores = new ArrayList<String>();
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Trino> getTrinosLista() {
		return trinosLista;
	}

	public void setTrinosLista(ArrayList<Trino> trinosLista) {
		this.trinosLista = trinosLista;
	}

	public ArrayList<String> getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(ArrayList<String> seguidores) {
		this.seguidores = seguidores;
	}
	
	
	@Override
	public String toString() {
		return "Nombre de Usuario: " + nombre + " NICK: " + nick;
	}

}
