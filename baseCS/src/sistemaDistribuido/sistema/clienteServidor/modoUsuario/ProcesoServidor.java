package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{

	private String name;
	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc, String name){
		super(esc);
		this.name = name;
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln("Proceso servidor en ejecucion.");
		byte[] solServidor = new byte[MessageCreator.MESSAGE_MAX_SIZE];
		byte[] respServidor;
		byte dato;
		while(continuar())
		{
			//We need to send the id of the server we start.
			int idServer = MessageCreator.getIdServerByName(name);
			Nucleo.receive(idServer,solServidor);
			
			imprimeln("el cliente envió un " + new String(solServidor));
			Pausador.pausa(1000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("enviando respuesta");
			Nucleo.send(dameID(), "Esta es mi respuesta".getBytes());
		}
	}
}
