/**
 * @ (#) Basededatos.java
 * 
 * Clase que contiene el main de la entidad Base de Datos.
 * Se encarga de almacenar todos los datos del sistema: Usuarios, Seguidores, Trinos.
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package basedeDatos;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.GUI;
import common.ServicioDatosInterface;
import common.Trino;
import common.UsuarioObj;
import common.Utiles;


public class Basededatos {
	
	private static Registry registryBaseDatos;


	/**
	 * main, Arranca el Registry. Bindea el servicio de Datos.
	 * Crea un objeto Basededatos y llama a su metodo Iniciar.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		//Se levanta el registro
		arrancarRegistro();
		
		//Ubicacion de la clase
		Utiles.setCodeBase(ServicioDatosInterface.class);
		
		//Levantar Servicio Datos
		ServicioDatosImpl objetoDatos = new ServicioDatosImpl();
		Naming.rebind(Utiles.urlDatos, objetoDatos);
		System.out.println("Operacion: Servicio Datos preparado con exito");
		
		new Basededatos().iniciar();
	}


	/**
	 * Levanta el Registry en el puerto indicado.
	 * @throws RemoteException
	 */
	private static void arrancarRegistro() throws RemoteException {
		try {
			registryBaseDatos = LocateRegistry.getRegistry(Utiles.puertoBaseDatos);
			registryBaseDatos.list(); // Esta llamada lanza una excepcion si el registro no existe
		}
		catch (RemoteException e) {
			// Registro no valido en este puerto
			System.out.println("El registro RMI no se puede localizar en el puerto "+ Utiles.puertoBaseDatos);
			registryBaseDatos =	LocateRegistry.createRegistry(Utiles.puertoBaseDatos);
			System.out.println("Registro RMI creado en el puerto " + Utiles.puertoBaseDatos);
		}
	}
	
	
	/**
	 * Muestra el Menu de Base de Datos.
	 * @throws Exception
	 */
	private void iniciar() throws Exception {
		
		try{				
			int opcion = 0;
			Integer numOpcs = 4;
			do{
				opcion = GUI.menu("de la BASE de DATOS", new String[]{"Informacion de la Base de Datos",
						"Listar Usuarios Registrados", "Listar Trinos"});
				
				if (opcion<1 || opcion>numOpcs) {
					System.out.println("Opcion incorrecta. Vuelve a probar");
		    	}
				
				switch (opcion){
					case 1:
						infoBBDD();
						break;
					case 2:
						listarUsuarios();
						break;
					case 3:
						System.out.println("\nListado de todos los Trinos publicados\n");
						listarTrinos();
						break;
					case 4:
						cerrarBBDD();
						break; //Salir.
				}	
			}while (opcion != numOpcs);
		}catch (ConnectException e){
			System.out.println("Error de conexion, la Base de Datos no esta disponible, vuelva a intentarlo");
			GUI.pideDato("Pulse enter para finalizar...");
		}	
	}
	
	
	/**
	 * Lista los Servicios levantados en Base de Datos y el numero de usuarios y trinos almacenados en el sistema.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void infoBBDD() throws RemoteException, MalformedURLException, NotBoundException {
		
		String registryURL = Utiles.urlDatos;
		Integer totalTrinos = listarTrinos();
		
		System.out.println("********************************");
		System.out.println("Registry en el puerto " + Utiles.puertoBaseDatos + " contiene: ");
		String[] URLs =Naming.list(registryURL);
		for  (int i=0; i< URLs.length; i++) {
			System.out.println("rmi:" + URLs[i]);
		}
		System.out.println("\nEn este momento hay un total de " + ServicioDatosImpl.mapaUsuarios.size() + " usuarios registrados en el sistema.");
		System.out.println("Y se han publicado un total de " + totalTrinos + " trinos.");
		System.out.println("********************************");
	}
	
	
	/**
	 * Imprime un listado de todos los usuarios del sistema.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private void listarUsuarios() throws RemoteException, MalformedURLException, NotBoundException{
		
		ArrayList<UsuarioObj> usuariosLista = new ArrayList<UsuarioObj>(ServicioDatosImpl.mapaUsuarios.values());
		System.out.println("\nListado de Usuarios del Sistema\n");
		for (UsuarioObj usuario : usuariosLista) {
			System.out.println(usuario.toString());
		}
	}
	
	
	/**
	 * Imprime un listado de todos los trinos que se han publicado.
	 * @throws RemoteException
	 * @throws MalformedURLException 
	 * @throws NotBoundException 
	 */
	private Integer listarTrinos() throws RemoteException, MalformedURLException, NotBoundException{
		
		ArrayList<UsuarioObj> usuariosLista = new ArrayList<UsuarioObj>(ServicioDatosImpl.mapaUsuarios.values());
		Integer totalTrinos = 0;
		
		for (UsuarioObj usuario : usuariosLista) {
			for (Trino trino : usuario.getTrinosLista()) {
				if (trino.ObtenerNickPropietario().equals(usuario.getNick())) {
					System.out.println(trino.toString());
					totalTrinos++;
				}
			}
		}
		return totalTrinos;
	}
	
	
	/**
	 * Cierra los servicios levantados y finaliza Base de Datos.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void cerrarBBDD() throws RemoteException, MalformedURLException, NotBoundException {
		
		//Cerrar Servicio Datos
		System.out.println("Operacion: Servicio Datos cerrandose...");
		Naming.unbind(Utiles.urlDatos);
		System.out.println("Operacion: Servicio Datos cerrado con exito");
						
		//Cerrar RMIRegistry del objeto Registry unico
		try{
			UnicastRemoteObject.unexportObject(registryBaseDatos,true);
			System.out.println("Operacion: Registry cerrado con exito");
		} catch (java.rmi.NoSuchObjectException e){
			System.err.println("Operacion: Registry no se ha cerrado");
		}
		System.exit(0);
	}
}

