package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{

	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}
	
	public void setMessage(byte[] message)
	{
		this.message = message;
	}

	/**
	 * 
	 */
	public void run(){
		while(true)
		{
			imprimeln("Proceso cliente en ejecucion.");
			imprimeln("Esperando datos para continuar.");
			Nucleo.suspenderProceso();
		
			//Send the message
			Nucleo.send(248,message);
			//Nucleo.suspenderProceso();
			Nucleo.receive(dameID(), message);
			MessageReader messageReader = new MessageReader(message);
			short dataSize = messageReader.readShort(MessageCreatorServer.MESSAGE_INDEX_DATA_SIZE);
					
			//Procesar la respuesta.
			imprimeln("Answer: " + new String(messageReader.
					readString(MessageCreatorServer.MESSAGE_INDEX_DATA, dataSize)) + "\n");
		}
	}
}
