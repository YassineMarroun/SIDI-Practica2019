/**
 * @ (#) ServicioAutenticacionInterface.java
 * 
 * Contiene la interfaz remota del servicio de autenticacion que depende de la entidad Servidor.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote {

	/**
	 * Solicita al Servicio de Datos el registro de un usuario.
	 * @param UsuarioObj usuario, Se le pasa un objeto usuario con sus datos
	 * @return int 0 si el registro es correcto, int -1 si ya existe usuario con ese nick
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int registrarUsuario(UsuarioObj usuario) throws RemoteException, MalformedURLException, NotBoundException;
	
	
	/**
	 * Solicita al Servicio de Datos la autenticacion de un usuario.
	 * @param String nick, String password, datos del usuario que se quiere autenticar
	 * @return int -2 si el usuario no esta registrado, int -1 si el password no es correcto, int 0 si accede correctamente
	 * @throws RemoteException
	 */
	public int autenticarUsuario(String nick, String password) throws RemoteException, MalformedURLException, NotBoundException;

}
