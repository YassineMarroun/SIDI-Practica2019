/**
 * 
 * @ (#) Usuario.java
 * 
 * Clase que contiene el main de la entidad Usuario.
 * Los usuarios (clientes) son los actores principales del sistema e interactuan entre ellos enviandose trinos
 * y haciendose seguidores unos de otros.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package usuario;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.CallBackUsuarioInterface;
import common.GUI;
import common.ServicioAutenticacionInterface;
import common.ServicioGestorInterface;
import common.Trino;
import common.UsuarioObj;
import common.Utiles;

public class Usuario {
	
	private static ServicioAutenticacionInterface servicioAutenticador;
	private static ServicioGestorInterface servicioGestor;
	private static UsuarioObj usuario = new UsuarioObj();
	
	
	public Usuario() {
		
	}
	
	
	/**
	 * main del Usuario, crea una instancia de Usuario y llama al metodo Iniciar.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{

		new Usuario().iniciar();
		System.exit(0); //Finaliza el programa.
	}

	
	/**
	 * Conecta con los servicios Autenticador  y Gestor, y muestra el Menu de Inicio de USUARIO.
	 * @throws Exception
	 */
	private void iniciar() throws Exception {
		
		//Si el Servidor no esta disponible, cierra informando de ello
		try{
			servicioAutenticador = (ServicioAutenticacionInterface) Naming.lookup(Utiles.urlAutenticador);
			servicioGestor = (ServicioGestorInterface) Naming.lookup(Utiles.urlGestor);
		
			//Mostrar menu inicial
			int opcion = 0;
			Integer numOpcs = 3;
			do{
				opcion = GUI.menu("de USUARIO",new String[]
						{"Registrar un nuevo usuario","Hacer login"});
			
				if (opcion<1 || opcion>numOpcs) {
					System.out.println("Opcion incorrecta. Vuelve a probar");
		    	}
				
				switch (opcion){
					case 1:
						registrar();
						break;
					case 2:
						autenticar();
						break;
				}			
			}while (opcion != numOpcs);
		}catch (ConnectException e){
			System.out.println("Error de conexion, el Servidor no esta disponible, vuelva a intentarlo");
			GUI.pideDato("Pulse enter para finalizar...");
		}
		
	}
	
	
	/**
	 * Inicia el registro de un usuario nuevo en el sistema, solicita los datos desde teclado.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private static void registrar() throws RemoteException, MalformedURLException, NotBoundException{
		String nombre = GUI.pideDato("Introduzca un nombre de Usuario");
		String nick = GUI.pideDato("Introduzca su NICK de Usuario");
		String password = GUI.pideDato("Introduzca un password");
		UsuarioObj usuario = new UsuarioObj(nombre, nick, password);
		Integer error = servicioAutenticador.registrarUsuario(usuario);
		switch (error){
		case 0: System.out.println("USUARIO creado correctamente, puede acceder al sistema pulsando la opcion 2, Hacer login"); break;
		case -1: System.out.println(nombre + ", ya existe un usuario con el nick " + nick); break;
		}		
	}	

	
	/**
	 * Inicia la autenticacion de un usuario en el sistema, solicita datos
	 * y entra en un menu con diferentes opciones de usuario.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void autenticar() throws RemoteException, MalformedURLException, NotBoundException{
		
		String nick = GUI.pideDato("Introduzca su NICK");
		String password = GUI.pideDato("Introduzca su password");
		if ((servicioAutenticador.autenticarUsuario(nick, password)) >= 0) {
			registrarCallBack(nick);
			usuario.setNick(nick); //asi la clase almacena el NICK del usuario
			usuario.setPassword(password);
			mostrarTrinosPendientes(nick);
			menuUsuario(nick);
		}
		else 
			System.out.println("No existe usuario con ese NICK o PASSWORD incorrrecto.");		
	}
	
	
	/**
	 * Recibe un nick, le pasa a servicio Gestor el nick y un nuevo objeto CallBackUsuarioInterface.
	 * @param String nick, se le pasa el nick del usuario
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void registrarCallBack(String nick) throws RemoteException, MalformedURLException, NotBoundException {
		
		CallBackUsuarioInterface callbackObj = new CallBackUsuarioImpl();
		servicioGestor.registrarCallback(nick, callbackObj);
	}
	
	
	/**
	 * Imprime en pantalla todos los datos guardados de un usuario.
	 * @param String nick, se le pasa el nick del usuario
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void infoUsuario(String nick) throws RemoteException, MalformedURLException, NotBoundException{
		
		usuario = servicioGestor.recuperarUsuario(nick);
		System.out.println("********************************");
		System.out.println("Nombre de Usuario: " + usuario.getNombre());
		System.out.println("Nick de Usuario: " + usuario.getNick());
		System.out.println("Password: " + usuario.getPassword());
		System.out.println("Tiene un total de " + usuario.getTrinosLista().size() + " trinos publicados en su tablon.");
		System.out.println("Tiene un total de " + usuario.getSeguidores().size() + " seguidores.");
		System.out.println("********************************");
	}
	
	
	/**
	 * Solicita que se introduzca un mensaje para el Trino y se lo pasa al servicio Gestor.
	 * @param String nick, se le pasa el nick del usuario
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void enviarTrino(String nick) throws RemoteException, MalformedURLException, NotBoundException{
		
		String mensaje = GUI.pideDato("Escriba su TRINO");
		servicioGestor.enviarTrino(mensaje, nick);
	}

	
	/**
	 * Solicita al servicio Gestor un listado de todos los usuarios del sistema.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void listarUsuarios() throws RemoteException, MalformedURLException, NotBoundException{
		
		ArrayList<UsuarioObj> usuariosLista = servicioGestor.listarUsuarios();
		System.out.println("\nListado de Usuarios del Sistema\n");
		for (UsuarioObj usuario : usuariosLista) {
			System.out.println(usuario.toString());
		}
	}
	
	
	/**
	 * Solicita al servicio Gestor que inicie la funcion de seguir a un usuario pasandole un nick.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void seguirA() throws RemoteException, MalformedURLException, NotBoundException{
		
		String nick = GUI.pideDato("Escriba el NICK de usuario al que quiere seguir");
		Integer error = servicioGestor.seguirA(nick, usuario.getNick());
		switch (error){
		case 0: System.out.println("Has sido incluido en la lista de seguidores de " + nick); break;
		case -1: System.out.println("Ya eras seguidor de " + nick); break;
		case -2: System.out.println("El usuario " + nick + " no esta registrado"); break;
		}	
	}
	
	
	/**
	 * Solicita al servicio Gestor que inicie la funcion para dejar de seguir a un usuario.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void dejarSeguirA() throws RemoteException, MalformedURLException, NotBoundException{
		
		String nick = GUI.pideDato("Escriba el NICK de usuario al que no quiere seguir mas");
		Integer error = servicioGestor.dejarSeguirA(nick, usuario.getNick());
		switch (error){
		case 0: System.out.println("Has sido eliminado de la lista de seguidores de " + nick); break;
		case -1: System.out.println("No figurabas como seguidor de " + nick); break;
		case -2: System.out.println("El usuario " + nick + " no esta registrado"); break;
		}	
	}
	
	
	
	/**
	 * Solicita al servicio Gestor que inicie la funcion de eliminar un trino para aquellos seguidores
	 * que aun no lo hayan recibido.
	 * @param String nick
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void borrarTrino(String nick) throws RemoteException, MalformedURLException, NotBoundException{
		
		String trino = GUI.pideDato("Copie y pegue de su Tablon el Trino que desea eliminar para los seguidores que aun no lo han recibido");
		String mensaje = servicioGestor.borrarTrino(nick, trino);
		System.out.println(mensaje);
	}
	
	
	/**
	 * Solicita al servicio Gestor cerrar la sesion del Usuario conectado.
	 * @param String nick
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void cerrarUsuario(String nick) throws RemoteException, MalformedURLException, NotBoundException {
		
		String mensaje = servicioGestor.cerrarUsuario(nick);
		System.out.println(mensaje);
		System.exit(0);
	}
	
	
	/**
	 * Menu que llama al metodo que inicia la funcionalidad deseada segun la opcion seleccionada.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void menuUsuario(String nick) throws RemoteException, MalformedURLException, NotBoundException{
		
		int opcion = 0;
		Integer numOpcs = 7;
		
		do{
			mostrarTrinosSeguidores(nick);
			
			opcion = GUI.menu("de " + nick,new String[]
					{"Informacion del Usuario","Enviar Trino","Listar Usuarios del Sistema","Seguir a",
					"Dejar de seguir a","Borrar trino a los usuarios que todavia no lo han recibido"});				
			
			if (opcion<1 || opcion>numOpcs) {
				System.out.println("Opcion incorrecta. Vuelve a probar");
	    	}
			
			switch (opcion){
				case 1:
					infoUsuario(nick);
					break;
				case 2:
					enviarTrino(nick);
					break;
				case 3:
					listarUsuarios();
					break;
				case 4:
					seguirA();
					break;
				case 5:
					dejarSeguirA();
					break;
				case 6:
					borrarTrino(nick);
					break;
				case 7: cerrarUsuario(nick); break; //Salir.
			}
		}while (opcion != numOpcs);
	}
	
	
	/**
	 * Obtiene un usuario pasando el nick que ha recibido como parametro a servicio Gestor.
	 * Imprime las listas de trinos y seguidores del usuario.
	 * @param String nick, se le pasa el nick del usuario
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void mostrarTrinosSeguidores(String nick) throws RemoteException, MalformedURLException, NotBoundException {
		
		usuario = servicioGestor.recuperarUsuario(nick);
		
		System.out.println("\n--- TRINOS de " + nick + " ---\n");
		for (Trino trino : usuario.getTrinosLista()) {
			System.out.println(trino.toString());
		}
		System.out.println("\n--- SEGUIDORES de " + nick + " ---\n");
		for (String seg : usuario.getSeguidores()) {
			System.out.println(seg.toString());
		}
	}
	
	
	/**
	 * Muestra al Usuario los Trinos nuevos que han publicado las personas a las que sigue
	 * mientras no ha estado conectado.
	 * @param String nick, se le pasa el nick del usuario
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void mostrarTrinosPendientes(String nick) throws RemoteException, MalformedURLException, NotBoundException {
		
		try {
			usuario = servicioGestor.recuperarUsuario(nick);
			ArrayList<Trino> trinosNuevos = new ArrayList<Trino>();
			
			for (Trino trino : usuario.getTrinosLista()) {
				if (trino.isPendiente()){
					trinosNuevos.add(trino);
					trino.setPendiente(false);
				}
			}
			if (!trinosNuevos.isEmpty()) {
				System.out.println("-- Nuevos TRINOS Publicados --");
			}
			for (Trino trino : trinosNuevos) {
				System.out.println("Nuevo Trino > " + trino);
			}		
			servicioGestor.grabarUsuario(usuario);
		} catch (Exception e) {
			System.out.println("Error en la publicacion de trinos pendientes");
		}
	}
}