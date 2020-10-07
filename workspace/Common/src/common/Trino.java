/**
 * @ (#) Trino.java
 * 
 * Clase que implementa y encapsula los trinos (suministrada por el equipo docente).
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 */
package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trino implements Serializable{

	private static final long serialVersionUID = 4369013758306474236L;
	private String trino;
	private String nickPropietario;	//Ojo no pueden haber varios usuarios con el mismo nick
	private Date timestamp; //momento en el que se produce el evento (tiempo en el servidor)
	private boolean pendiente; 
	
	public Trino(String trino,String nickPropietario)
	{
		this.trino=trino;
		this.nickPropietario=nickPropietario;
		this.timestamp = new Date();
		this.pendiente = false;
	}
	public String ObtenerTrino()
	{
		return (trino);
	}
	public String ObtenerNickPropietario()
	{
		return(nickPropietario);
	}
	public Date ObtenerTimestamp()
	{
		return (timestamp);
	}
	public boolean isPendiente() {
		return pendiente;
	}
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
	/**
	* El método darFormatoFecha recibe el dato correspondiente a la fecha, que es de tipo Date,
	* y mediante una instancia de SimpleDateFormat y llamando a su método format, devuelve la fecha en un String
	* en el formato que queremos visualizarlo en el trino.
	*/
	public String darFormatoFecha(Date timestamp){
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatoFecha.format(timestamp.getTime());
	}

	
	public String toString(){
		return ("@" + nickPropietario + " > " + trino + " | " + darFormatoFecha(timestamp) +" |");
	}
}