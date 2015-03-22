package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{

	private String serverName;
	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc, String serverName){
		super(esc);
		this.serverName = serverName;
		start();
	}
	
	public String getServerName()
	{
		return serverName;
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
			int idServer = MessageCreator.getIdServerByName(serverName);
			Nucleo.receive(idServer,solServidor);
			
			//Read the message.
		//////cambios habrá que destapar el mensaje y darnos cuenta si tiene información extra
			//una vez este abierto. En base al código de operación, sabremos si tiene información
			//extra.
			MessageReaderServer messageReaderServer = new MessageReaderServer(solServidor, true);
			messageReaderServer.readMessage();
			
			imprimeln("el cliente envi� un " + messageReaderServer.getReadableMessage());
			Pausador.pausa(1000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("enviando respuesta");
			
			//Crear respuesta. Abstraer una clase servidor, intanciar el servidor necesario,
			//Server server = Serger.getServer(idServer);
			//realizar la tarea con el server y enviar la respuesta.
			
			Nucleo.send(idServer, "Respuesta del servidor".getBytes());/////
			imprimeln("Respuesta enviada");
		}
	}
}
