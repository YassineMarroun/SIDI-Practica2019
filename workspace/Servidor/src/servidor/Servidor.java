/**
 * @ (#) Servidor.java
 * 
 * Clase que contiene el main de la entidad Servidor.
 * Se encarga de controlar el proceso de autenticacion de los usuarios y gestion de sus mensajes(trinos).
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package servidor;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;

import common.GUI;
import common.ServicioAutenticacionInterface;
import common.ServicioDatosInterface;
import common.UsuarioObj;
import common.Utiles;

public class Servidor {
	
	private static ServicioDatosInterface servicioDatos;
	private static Registry registryServidor;
	
	/**
	 * main, Arranca el Registry. Bindea los servicios de Autenticacion y Gestor.
	 * Crea un objeto Servidor y llama a su metodo Iniciar.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		//Se levanta el registro
		arrancarRegistro();
		
		//Ubicacion de la clase
		Utiles.setCodeBase(ServicioAutenticacionInterface.class);

		//Levantar Autenticador
		ServicioAutenticacionImpl objetoAutenticador = new ServicioAutenticacionImpl();
		Naming.rebind(Utiles.urlAutenticador, objetoAutenticador);
		System.out.println("Operacion: Servicio Autenticador preparado con exito");
		
		//Levantar Gestor
		ServicioGestorImpl objetoGestor = new ServicioGestorImpl();
		Naming.rebind(Utiles.urlGestor, objetoGestor);
		System.out.println("Operacion: Servicio Gestor preparado con exito");
		
		new Servidor().iniciar();
	}	
	
	
	/**
	 * Arranca el Registry en el puerto indicado.
	 * @throws RemoteException
	 */
	private static void arrancarRegistro() throws RemoteException {
		try {
			registryServidor = LocateRegistry.getRegistry(Utiles.puertoServidor);
			registryServidor.list(); // Esta llamada lanza una excepcion si el registro no existe
		}
		catch (RemoteException e) {
			// Registro no valido en este puerto
			System.out.println("El registro RMI no se puede localizar en el puerto "+ Utiles.puertoServidor);
			registryServidor =	LocateRegistry.createRegistry(Utiles.puertoServidor);
			System.out.println("Registro RMI creado en el puerto " + Utiles.puertoServidor);
		}
	}
	
	
	/**
	 * Muestra el Menu de SERVIDOR.
	 * @throws Exception
	 */
	private void iniciar() throws Exception {
		
		//Si Base de Datos no esta disponible, cierra informando de ello
		try{
			servicioDatos = (ServicioDatosInterface) Naming.lookup(Utiles.urlDatos);
			
			//Mostrar menu del Servidor
			int opcion = 0;
			Integer numOpcs = 3;
			do{			
				opcion = GUI.menu("del SERVIDOR",new String[]{
						"Informacion del Servidor","Listar Usuarios Logeados"});
				
				if (opcion<1 || opcion>numOpcs) {
					System.out.println("Opcion incorrecta. Vuelve a probar");
		    	}
				
				switch(opcion){
					case 1:
						infoServidor();
						break;
					case 2:
						listarUsuariosLOG();
						break;
					case 3:
						cerrarServidor();
						break; //Salir.
				}
			}while (opcion != numOpcs);
		}catch (ConnectException e){
			System.out.println("Error de conexion, la Base de Datos no esta disponible, vuelva a intentarlo");
			GUI.pideDato("Pulse enter para finalizar...");
		}	
	}
	
	
	/**
	 * Lista los Servicios levantados en Servidor y el numero de usuarios logeados en el momento.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void infoServidor() throws RemoteException, MalformedURLException, NotBoundException {
		
		String registryURL = Utiles.urlAutenticador;
		System.out.println("********************************");
		System.out.println("Registry en el puerto " + Utiles.puertoServidor + " contiene: ");
		String[] URLs =Naming.list(registryURL);
		for  (int i=0; i< URLs.length; i++) {
			System.out.println("rmi:" + URLs[i]);
		}
		System.out.println("\nEn este momento hay un total de " + ServicioGestorImpl.mapaClientes.size() + " usuarios logeados.");
		System.out.println("********************************");
	}
	
	
	/**
	 * Muestra en pantalla los usuarios que estan logeados en el momento.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void listarUsuariosLOG() throws RemoteException, MalformedURLException, NotBoundException {
		
		Set<String> usuariosConectados = ServicioGestorImpl.mapaClientes.keySet();
		ArrayList<UsuarioObj> usuariosLista = servicioDatos.listarUsuarios();
		
		System.out.println("\nListado de Usuarios Logeados\n");
			for(UsuarioObj usuarioLOG : usuariosLista) {
				if (usuariosConectados.contains(usuarioLOG.getNick())) {
					System.out.println(usuarioLOG.toString());
				}
			}
	}
	
	
	/**
	 * Cierra los servicios levantados y finaliza Servidor.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void cerrarServidor() throws RemoteException, MalformedURLException, NotBoundException {
		
		//Cerrar Autenticador
		System.out.println("Operacion: Servicio Autenticador cerrandose...");
		Naming.unbind(Utiles.urlAutenticador);	
		System.out.println("Operacion: Servicio Autenticador cerrado con exito");
		
		//Cerrar Gestor
		System.out.println("Operacion: Servicio Gestor cerrandose...");
		Naming.unbind(Utiles.urlGestor);
		System.out.println("Operacion: Servicio Gestor cerrado con exito");		
		
				
		//Cerrar RMIRegistry del objeto Registry unico
		try{
			UnicastRemoteObject.unexportObject(registryServidor,true);
			System.out.println("Operacion: Registry cerrado con exito");
		} catch (java.rmi.NoSuchObjectException e){
			System.err.println("Operacion: Registry no se ha cerrado");
		}
		System.exit(0);
	}
}