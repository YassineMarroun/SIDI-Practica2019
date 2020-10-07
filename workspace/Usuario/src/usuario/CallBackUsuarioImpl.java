/**
 * @ (#) CallBackUsuarioImpl.java
 * 
 * Clase que implementa la interfaz remota CallBackUsuarioInterface.
 * Es el unico servicio que arranca el usuario y tiene un unico metodo que se encarga de hacerle llegar
 * los trinos que publican los usuarios a los que sigue, de forma automatica.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package usuario;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.CallBackUsuarioInterface;

public class CallBackUsuarioImpl extends UnicastRemoteObject implements CallBackUsuarioInterface{

	private static final long serialVersionUID = 5636870803726135096L;

	/**
	 * Constructor por defecto
	 * @throws RemoteException
	 */
	protected CallBackUsuarioImpl() throws RemoteException {
		super();
	}

	
	/**
	 * Recibe el trino como un String y devuelve dicho String para se publique en el tablon de forma automatica
	 * si el seguidor esta logeado.
	 * @param String trino
	 * @return String
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	@Override
	public String notificar(String trino)  throws RemoteException{
		System.out.println(trino); 
		return trino;
	}
}