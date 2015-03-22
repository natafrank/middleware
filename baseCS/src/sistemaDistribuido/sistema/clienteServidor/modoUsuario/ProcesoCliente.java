package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{
	
	private MessageCreatorClient messageCreatorClient;
	private byte[] message;

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
	
	public void setMessageCreatorClient(MessageCreatorClient mcc)
	{
		messageCreatorClient = mcc;
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
			
			byte[] answerClient = new byte[MessageCreator.MESSAGE_MAX_SIZE];
			int idServerToSend = messageCreatorClient.getIdServer();
			//Send the message
			Nucleo.send(idServerToSend,message);
			Nucleo.suspenderProceso();
			Nucleo.receive(idServerToSend, answerClient);
			
			//Procesar la respuesta.
			imprimeln("el servidor me envi� un " + new String(answerClient));
		}
	}
}
