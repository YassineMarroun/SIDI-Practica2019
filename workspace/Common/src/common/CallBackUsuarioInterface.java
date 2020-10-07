/**
 * @ (#) CallBackUsuarioInterface.java
 * 
 * Contiene la interfaz remota del servicio que le permite recibir al usuario los trinos
 * de aquellos usuarios a los que sigue.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallBackUsuarioInterface extends Remote {
	
	/**
	 * Recibe el trino como un String y devuelve dicho String para se publique en el tablon de forma automatica
	 * si el seguidor esta logeado.
	 * @param String trino
	 * @return String
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public String notificar(String trino) throws RemoteException, MalformedURLException, NotBoundException;

}
