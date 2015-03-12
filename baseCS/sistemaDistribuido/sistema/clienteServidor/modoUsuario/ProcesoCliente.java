package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{

	private static final int TAMANO_MENSAJE = 1024;
	
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Hola =)");
		byte[] solCliente=new byte[TAMANO_MENSAJE];
		byte[] respCliente=new byte[TAMANO_MENSAJE];
		byte dato;
		solCliente[0]=(byte)10;
		Nucleo.send(248,solCliente);
		Nucleo.receive(dameID(),respCliente);
		dato=respCliente[0];
		imprimeln("el servidor me envi� un "+dato);
	}
}
