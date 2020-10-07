/**
 * @ (#) ServicioDatosImpl.java
 * 
 * Clase que implementa la interfaz remota ServicioDatosInterface.
 * Desarrolla las funciones de una base de datos que relacione Usuarios-Seguidores-Trinos.
 * Mantiene la lista de usuarios registrados y/o conectados al sistema, junto con sus seguidores y trinos;
 * y los relaciona permitiendo operaciones tipicas de consulta, añadir y borrado.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package basedeDatos;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.ServicioDatosInterface;
import common.Trino;
import common.UsuarioObj;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	private static final long serialVersionUID = 123711131719L;
	public static Map<String, UsuarioObj> mapaUsuarios = new HashMap<String, UsuarioObj>();
	

	/**
	 * Contructor necesario al extender UnicastRemoteOBject y poder utilizar Naming.
	 * @throws RemoteException
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	public ServicioDatosImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
	
	}
	
	
	/**
	 * Recibe un nick, busca el UsuarioObj al que corresponde en mapaUsuarios y lo devuelve.
	 * @param String nick
	 * @return UsuarioObj usuario
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public UsuarioObj recuperarUsuario(String nick)throws RemoteException, MalformedURLException, NotBoundException {
		
		UsuarioObj usuario = mapaUsuarios.get(nick);
		return usuario;
	}
	
	
	/**
	 * Recibe UsuarioObj para que sea guardado con sus datos actualizados en mapaUsuarios.
	 * @param UsuarioObj seguidor 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void grabarUsuario(UsuarioObj seguidor)throws RemoteException, MalformedURLException, NotBoundException {
		
		mapaUsuarios.put(seguidor.getNick(), seguidor);
	}	
	
	
	/**
	 * Procede a registrar un usuario en la Base de Datos.
	 * @param UsuarioObj usuario, recibe un objeto usuario para registrar
	 * @return int -1 si ya existe un usuario con el nick recibido, int 0 si el registro es correcto
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	@Override
	public int registrarUsuario(UsuarioObj usuario) throws RemoteException, MalformedURLException, NotBoundException{

		if (mapaUsuarios.containsKey(usuario.getNick())) {
			return -1; //Existe un usuario con ese NICK
		}
		else {
			mapaUsuarios.put(usuario.getNick(), usuario);
			return 0; //Usuario registrado correctamente
		}
	}
	
	
	/**
	 * Procede a autenticar  al usuario en la Base de Datos.	
	 * @param String nick, String password, datos del usuario para autenticarse
	 * @return int 0 si la autenticacion es correcta, int -1 si el password no es correcto, int -2 si no existe el usuario
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	@Override
	public int autenticarUsuario(String nick, String password) throws RemoteException, MalformedURLException, NotBoundException{
		if (mapaUsuarios.containsKey(nick)) {
			UsuarioObj usuario = mapaUsuarios.get(nick);
			if (usuario.getPassword().equals(password)) {
				return 0; //Autenticacion correcta
			} else {
				return -1; //Password incorrecto
			}
		} else {
			return -2; //Usuario inexistente
		}
	}
	
	
	/**
	 * Localiza al usuario con su Nick, obtiene su lista de trinos e incluye en ella el nuevo trino.	
	 * @param String mensaje, String nick, datos para el trino
	 * @return Trino
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	@Override
	public Trino enviarTrino(String mensaje, String nick)throws RemoteException, MalformedURLException, NotBoundException {
		
		UsuarioObj usuario = mapaUsuarios.get(nick);
		ArrayList<Trino> lista = usuario.getTrinosLista();
		Trino trino = new Trino(mensaje, nick);
		lista.add(trino);
		usuario.setTrinosLista(lista);
		return trino;
	}
	
	
	/**
	 * Accede al mapa de usuarios para devolver un listado de ellos.
	 * @return ArrayList<UsuarioObj> uuariosLista, el metodo devuelve una lista de tipo UsuarioObj
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public ArrayList<UsuarioObj> listarUsuarios()throws RemoteException, MalformedURLException, NotBoundException {
		
		ArrayList<UsuarioObj> usuariosLista = new ArrayList<UsuarioObj>(mapaUsuarios.values());
		return usuariosLista;
	}
	
	
	/**
	 * Localiza al usuario con su Nick, obtiene su lista de seguidores y nos incluye en ella.	
	 * @param String nick, String miNick, nick del usuario a seguir, y miNick para ser incluido en su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si ya estas en la lista de seguidores del usuario,
	 * @return int 0 si has sido incluido en su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	@Override
	public int seguirA(String nick, String miNick)throws RemoteException, MalformedURLException, NotBoundException {
		
		if (mapaUsuarios.containsKey(nick)) {
			UsuarioObj usuario = mapaUsuarios.get(nick);
			ArrayList<String> seguidores = usuario.getSeguidores();
			if (seguidores.contains(miNick)) {
				return -1; //Ya figuras en la lista de seguidores del usuario
			} else {
				seguidores.add(miNick);
				usuario.setSeguidores(seguidores);
				return 0; //Has sido incluido en la lista de seguidores del usuario
			}
		} else {
			return -2; //No existe usuario
		}
	}
	
	
	/**
	 * Localiza al usuario con su Nick, obtiene su lista de seguidores y nos elimina de ella.	
	 * @param String nick, String minick, nick del usuario y miNick para ser eliminado de su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si no estas en la lista de seguidores del usuario,
	 * @return int 0 si hemos sido eliminados de su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	@Override
	public int dejarSeguirA(String nick, String miNick)throws RemoteException, MalformedURLException, NotBoundException {
		
		if (mapaUsuarios.containsKey(nick)) {
			UsuarioObj usuario = mapaUsuarios.get(nick);
			ArrayList<String> seguidores = usuario.getSeguidores();
			if (seguidores.contains(miNick)) {
				seguidores.remove(miNick);
				usuario.setSeguidores(seguidores);
				return 0; //Eliminado de la lista de seguidores del usuario
			} else {
				return -1; //No estas en la lista de seguidores del usuario
			}
		} else {
			return -2; //No existe usuario
		}
	}
	
	/**
	 * Elimina un trino en todos aquellos seguidores que aun lo tienen como pendiente de publicar.
	 * @param String nick
	 * @return String, mensaje confirmando la eliminacion del trino
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	public String borrarTrino(String nick, String trinoBorrar) throws RemoteException, MalformedURLException, NotBoundException {
		
		UsuarioObj usuario = mapaUsuarios.get(nick);
		
		for (String nickSeguidor : usuario.getSeguidores()) {
			UsuarioObj seguidor = mapaUsuarios.get(nickSeguidor);
			ArrayList<Trino> nuevosTrinos = new ArrayList<Trino>();
			for(Trino trino : seguidor.getTrinosLista()) {
				if (!trino.isPendiente() || !trino.ObtenerTrino().trim().equals(trinoBorrar.trim())) {
					nuevosTrinos.add(trino);
				}
			}
			seguidor.setTrinosLista(nuevosTrinos);
		}
		return "Finalizada la eliminacion del trino pendiente en seguidores.";
	}
}
