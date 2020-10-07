/**
 * @ (#) ServicioGestorInterface.java
 * 
 * Contiene la interfaz remota del servicio Gestor que depende de la entidad Servidor.
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

public interface ServicioGestorInterface extends Remote{
	
	
	/**
	 * Recibe un nick y un objeto CallBackUsuario para guardarlo en el mapa que tenemos en esta clase.
	 * @param String nick, CallBackUsuarioInterface callbackClientObject
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void registrarCallback(String nick, CallBackUsuarioInterface callbackClientObject) throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Recibe un nick, solicita a servicio Datos recuperar este objeto Usuario, y lo devuelve.
	 * @param String nick
	 * @return UsuarioObj usuario
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public UsuarioObj recuperarUsuario(String nick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Recibe un UsuarioObj y se lo pasa a servcio Datos para que sea guardado con sus datos actualizados.
	 * @param UsuarioObj seguidor 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void grabarUsuario(UsuarioObj seguidor)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Solicita al servicio de Datos el envio de un Trino.
	 * @param String mensaje, String nick, recibe los datos del trino
	 * @return boolean
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public boolean enviarTrino(String mensaje, String nick)throws RemoteException, MalformedURLException, NotBoundException;

	
	/**
	 * Solicita al servicio de Datos un listado de usuarios.
	 * @return ArrayList<UsuarioObj> el metodo devuelve una lista de tipo UsuarioObj
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public ArrayList<UsuarioObj> listarUsuarios()throws RemoteException, MalformedURLException, NotBoundException;
	

	/**
	 * Solicita al servicio de Datos seguir a un usuario.
	 * @param String nick, String miNick, nick del usuario a seguir, y miNick para ser incluido en su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si ya estas en la lista de seguidores del usuario,
	 * @return int 0 si has sido incluido en su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public int seguirA(String nick, String miNick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Solicita al servicio de Datos que ya no sigamos a un usuario.
	 * @param String nick, String minick, nick del usuario y miNick para ser eliminado de su lista de seguidores
	 * @return int -2 si no existe el usuario, int -1 si no estas en la lista de seguidores del usuario,
	 * @return int 0 si hemos sido eliminados de su lista de seguidores
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public int dejarSeguirA(String nick, String miNick)throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Solicita al servicio de Datos que elimine un trino para aquellos seguidores que aun no lo hayan recibido.
	 * @param String nick, String trino
	 * @return String, mensaje confirmando la eliminacion del trino
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	public String borrarTrino(String nick, String trino) throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Cierra la sesion del Usuario.
	 * @param String nick
	 * @return String, mensaje confirmando el cierre de sesion.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * */
	public String cerrarUsuario(String nick)throws RemoteException, MalformedURLException, NotBoundException;
}	