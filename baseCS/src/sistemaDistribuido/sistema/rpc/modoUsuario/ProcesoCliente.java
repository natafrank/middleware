package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso
{
	private Libreria lib;

	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		lib=new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
		//lib=new LibreriaCliente(esc);  //luego con esta comentando la anterior, para subrutina servidor remota
		start();
	}

	/**
	 * Programa Cliente
	 */
	public void run()
	{
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Salio de suspenderProceso");
		
		//Get the parameters and process the tasks.

		

		imprimeln("Fin del cliente.");
	}
	
	public Libreria getLib()
	{
		return lib;
	}
}
