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
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		
		//byte[] respCliente=new byte[MessageCreator.MESSAGE_MAX_SIZE];

		//Send the message
		Nucleo.send(messageCreatorClient.getIdServer(),message);
		Nucleo.receive(248,message);
		imprimeln("el servidor me envi� un " + new String(message));
	}
}
