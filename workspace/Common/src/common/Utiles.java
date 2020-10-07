/**
 * @ (#) Utiles.java
 * 
 * Clase Utiles, setea el CodeBase usado en el video de Fermin.
 * 
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

public class Utiles {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	
	//Variables estaticas para puertos y URLs
	public static int puertoServidor = 8816;
	public static int puertoBaseDatos = 8817;
	public static String direccionIP = "localhost";
	public static String urlDatos = "rmi://" + direccionIP + ":" + puertoBaseDatos + "/BaseDatos/ServicioBaseDatos";
	public static String urlAutenticador = "rmi://" + direccionIP + ":" + puertoServidor + "/Servidor/ServicioAutenticador";
	public static String urlGestor = "rmi://" + direccionIP + ":" + puertoServidor + "/Servidor/ServicioGestor";
	
	
	// Class<?> no esta parametrizada, cualquier clase pediremos su ruta
	public static void setCodeBase(Class<?> c) {
		
		//Calculara la ruta donde este cargada la clase
		//A la clase donde esta el codigo fuente, dame la ubicacion y pasala a string
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();
		
		//si seteamos el codebase en otra ubicacion, antes de setearlo, mejor
		//comprobar si ya esta puesta, asi evitamos problemas si pedimos mas veces el codebase
		//esto es para no tener que lanzar desde el shell
		String path = System.getProperty(CODEBASE); //si se seteo contendra ya algo
		
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;
		}
		System.setProperty(CODEBASE,ruta);
	}
}