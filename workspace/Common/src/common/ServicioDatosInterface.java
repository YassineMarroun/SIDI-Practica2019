/**
 * @ (#) ServicioDatosInterface.java
 * 
 * Contiene la interfaz remota del servicio Datos que depende de la entidad Base de Datos.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServicioDatosInterface extends Remote{
	
	
	/**
	 * Recibe un nick, busca el UsuarioObj al que corresponde en mapaUsuarios y lo devuelve.
	 * @param String nick
	 * @return UsuarioObj usuario
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public UsuarioObj recuperarUsuario(String nick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Recibe UsuarioObj para que sea guardado con sus datos actualizados en mapaUsuarios.
	 * @param UsuarioObj seguidor 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void grabarUsuario(UsuarioObj seguidor)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Procede a registrar un usuario en la Base de Datos.
	 * @param UsuarioObj usuario, recibe un objeto usuario para registrar
	 * @return int -1 si ya existe un usuario con el nick recibido, int 0 si el registro es correcto
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int registrarUsuario(UsuarioObj usuario) throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Procede a autenticar al usuario en la Base de Datos.
	 * @param String nick, String password, datos del usuario para autenticarse
	 * @return int 0 si la autenticacion es correcta, int -1 si el password no es correcto, int -2 si no existe el usuario
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int autenticarUsuario(String nombre, String password) throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Procede a incluir el trino en la lista de trinos del usuario.
	 * @param String mensaje, String nick, recibe los datos del trino
	 * @return Trino
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public Trino enviarTrino(String mensaje, String nick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Accede al mapa de usuarios para devolver un listado de ellos.
	 * @return ArrayList<UsuarioObj> el metodo devuelve una lista de tipo UsuarioObj
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public ArrayList<UsuarioObj> listarUsuarios()throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Procede a incluirnos en la lista de seguidores del usuario si es localizado.
	 * @param String nick, String miNick, nick del usuario a seguir, y miNick para ser incluido en su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si ya estas en la lista de seguidores del usuario,
	 * @return int 0 si has sido incluido en su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int seguirA(String nick,String miNick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Procede a eliminarnos de la lista de seguidores del usuario si es localizado.
	 * @param String nick, String minick, nick del usuario y miNick para ser eliminado de su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si no estas en la lista de seguidores del usuario,
	 * @return int 0 si hemos sido eliminados de su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int dejarSeguirA(String nick, String miNick)throws RemoteException, MalformedURLException, NotBoundException;


	/**
	 * Elimina un trino en todos aquellos seguidores que aun lo tienen como pendiente de publicar.
	 * @param String nick
	 * @return String, mensaje confirmando la eliminacion del trino
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	public String borrarTrino(String nick, String trino) throws RemoteException, MalformedURLException, NotBoundException;
}
