/**
 * @ (#) GUI.java
 * 
 * Clase Interfaz, con metodos estatitos para imprimir menus,
 * captar datos e imprimir/guardar mensajes en pantalla.
 * 
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

public class GUI {
	
	public static final Scanner opcion = new Scanner(System.in);
	public static final Scanner sc = new Scanner(System.in);

	/**
	 * Muestra un menu y recoge una opcion.
	 * @param String titulo, para cada menu 
	 * @param String[] opciones, vector de Strings con las opciones a mostrar
	 * @return int, con el numero de opcion seleccionado
	 */
	public static int menu(String titulo,String[] opciones){
		
		try {
			System.out.println("\nMenu " + titulo);
			System.out.println("-----------------");
			for (int i = 0; i < opciones.length; i++) {
				System.out.println((i+1) + ".- " + opciones[i]);
			}
			System.out.println(opciones.length + 1 + ".- Salir");
			System.out.println("Seleccione una opcion > ");
			return opcion.nextInt();
		} catch (Exception e){
			return -1;
		}
	}
	
	
	/**
	 * Solicita que se introduzca un dato por teclado y devuelve dicho dato de tipo String.
	 * @param String queDato, el mensaje a mostrar para la peticion del dato a leer
	 * @return String s, devuelve un String con el dato leido por teclado
	 */
	public static String pideDato(String queDato){
		System.out.println("\n " + queDato + " > ");
		return sc.nextLine();	
	}
	
	
	/**
	 * Imprime en pantalla y en el fichero del log,
	 * cada dia de ejecucion se crea el fichero si no existe usando la fecha actual
	 * @param String mensaje
	 */
	public static void imprime(String mensaje)	{
		BufferedWriter salida = null;
		System.out.println(mensaje);
		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH)+1);
		String anio = Integer.toString(c.get(Calendar.YEAR));
		try {   
			salida = new BufferedWriter(new FileWriter("log" + anio + mes + dia +".txt", true));   
			salida.write("" + c.get(Calendar.DATE)+ "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR) + " " +
	   				c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE)+":"+ c.get(Calendar.SECOND) + " : " + mensaje + "\r\n");
			salida.close();
		} catch (IOException e) {   
			System.out.println("Error al escribir en fichero Log");   
		} 
	}
}