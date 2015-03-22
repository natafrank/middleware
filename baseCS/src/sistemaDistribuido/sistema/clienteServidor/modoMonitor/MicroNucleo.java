package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public final class MicroNucleo extends MicroNucleoBase{
	private static MicroNucleo nucleo=new MicroNucleo();
	
	//UI Messages (UIM).
	private final static String UIM_ERROR_SOCKET      = "Error while creating Socket.";
	private final static String UIM_ERROR_ADDRESS     = "Error while creating address.";
	private final static String UIM_ERROR_SEND_PACKET = "Error while sending packet.";

	/**
	 * 
	 */
	private MicroNucleo(){
	}

	/**
	 * 
	 */
	public final static MicroNucleo obtenerMicroNucleo(){
		return nucleo;
	}

	/*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
	byte[] mensaje;
	
	public void sendFalso(int dest,byte[] message){
	
	}

	public void receiveFalso(int addr,byte[] message)
	{
		
	}
	/*---------------------------------------------------------*/

	/**
	 * 
	 */
	protected boolean iniciarModulos(){
		return true;
	}

	/**
	 * 
	 */
	protected void sendVerdadero(int dest,byte[] message){
		
		//lo siguiente aplica para la pr�ctica #2
		ParMaquinaProceso pmp=dameDestinatarioDesdeInterfaz();
		String ip = pmp.dameIP();
		imprimeln("Enviando mensaje a IP="+ip+" ID="+pmp.dameID());
		//suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
		
		//Package and sent the message.
		DatagramSocket socket = null;
		DatagramPacket packet;
		InetAddress address;

		try
		{
			socket  = new DatagramSocket();
			
			//Get the addrerss (ip). take it form the text box or by dest.
			if(!ip.equals(""))
			{
				address = InetAddress.getByName(ip);
			}
			else
			{
				address = InetAddress.getByName(MessageCreator.getAddress(dest));
			}
			
			imprimeln("mensaje a enviar: " + new String(message));
			packet  = new DatagramPacket(message, message.length, address, MessageCreator
					.getPort(dest));
			socket.send(packet);
			socket.close();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_SOCKET);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_ADDRESS);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_SEND_PACKET);
		}
	}

	/**
	 * 
	 */
	protected void receiveVerdadero(int addr,byte[] message)
	{
		DatagramSocket socket = null;
		DatagramPacket packet;
		
		//Get the port using the address that is the idServer.
		int port = MessageCreator.getPort(addr);
		
		try
		{
			socket = new DatagramSocket(port);
			packet = new DatagramPacket(message, message.length);
			imprimeln("recibiendo");
			socket.receive(packet);
			imprimeln("recibido: " + new String(packet.getData()));
			message = packet.getData();
			socket.close();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_SOCKET);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_SEND_PACKET);
		}
		Pausador.pausa(1000);
		notificarHilos();
	}

	/**
	 * Para el(la) encargad@ de direccionamiento por servidor de nombres en pr�ctica 5  
	 */
	protected void sendVerdadero(String dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void sendNBVerdadero(int dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void receiveNBVerdadero(int addr,byte[] message){
	}

	/**
	 * 
	 */
	public void run(){

		while(seguirEsperandoDatagramas()){
			/* Lo siguiente es reemplazable en la pr�ctica #2,
			 * sin esto, en pr�ctica #1, seg�n el JRE, puede incrementar el uso de CPU
			 */ 
			try{
				sleep(60000);
			}catch(InterruptedException e){
				System.out.println("InterruptedException");
			}
		}
	}
}
