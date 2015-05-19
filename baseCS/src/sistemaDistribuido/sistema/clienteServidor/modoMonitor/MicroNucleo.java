//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;

/**
 * 
 */
public final class MicroNucleo extends MicroNucleoBase{
	private static MicroNucleo nucleo=new MicroNucleo();
	
	//User Interface Messages (UIM).
	private final static String UIM_ERROR_SOCKET         = "Error while creating Socket.";
	private final static String UIM_ERROR_IP		     = "Error while creating ip.";
	private final static String UIM_ERROR_RECEIVE_PACKET = "Error while receiving packet.";
	private final static String UIM_AU					 = "Address Unknown";
	private final static String UIM_TA					 = "Try Again";
	
	//MESSAGES FORMS.
	private final static int AU = -10;
	private final static int TA = -11;
	
	private HashMap<Integer, String[]> emitionTable = new HashMap<Integer, String[]>();
	private HashMap<Integer, byte[]> receptionTable = new HashMap<Integer, byte[]>();

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
	
	protected boolean iniciarModulos(){
		return true;
	}

	/**
	 * 
	 */
	protected void sendVerdadero(int dest,byte[] message)
	{	
		//Get ip and id from table.
		String[] idIp = emitionTable.get(dest);
		String ip;
		int id;
		
		if(idIp != null)
		{
			id = Integer.parseInt(idIp[0]);
			ip = idIp[1];
		}
		else
		{
			/*AsaId asaId = RPC.getAsaFromId(dest);
			
			if(asaId != null)
			{
				ParMaquinaProceso pmp = asaId.getAsa();
				ip = pmp.dameIP();
				id = pmp.dameID();
			}
			else
			{
				//Error.
				ip = "";
				id = -1;
			}*/
			
			ParMaquinaProceso pmp = dameDestinatarioDesdeInterfaz();
			ip = pmp.dameIP();
			id = pmp.dameID();
			
			imprimeln("IP: " + ip + "\nID: " + id);
		}
		
		MessageCreator.setDestiny(id, message);
		MessageCreator.setOrigin(dameIdProceso(), message);
		try
		{
			DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress
					.getByName(ip), damePuertoRecepcion());
			dameSocketEmision().send(packet);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_IP);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_SOCKET);
		}
	}

	/**
	 * 
	 */
	protected void receiveVerdadero(int addr,byte[] message)
	{
		receptionTable.put(addr, message);
		suspenderProceso();
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
	 * Thread for receive datagrams.
	 */
	public void run()
	{
		try
		{
			byte[] buffer = new byte[MessageCreator.MESSAGE_MAX_SIZE];
			DatagramSocket socket = dameSocketRecepcion();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			//Message mail control.
			byte[] savedMessage = null;
			
			while(seguirEsperandoDatagramas())
			{
				
				imprimeln("Receiving messages...");
				socket.receive(packet);
				
				imprimeln("Message Received.");
				
				//Check if the message is a try again, if it is, then re-send the saved message.
				int messageType = MessageReader.readIntFromMessage(buffer, MessageCreator
						.MESSAGE_INDEX_MESSAGE_TYPE);
				
				if(messageType == TA)
				{
					//Wait 5 seconds and then send the saved message.
					final int TIME_WAIT_RESEND = 5000;//5 seconds.
					try
					{
						sleep(TIME_WAIT_RESEND);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
						imprimeln("Error while waiting to resned the message.");
					}
					
					DatagramPacket packetAnswer = new DatagramPacket(savedMessage, savedMessage
							.length);
					dameSocketEmision().send(packetAnswer);
				}
				else
				{
					//Read origin and destiny of message and check if the process is in the table.
					int origin  = MessageReader.readIntFromMessage(buffer, MessageCreator.
							MESSAGE_INDEX_ORIGIN);
					int destiny = MessageReader.readIntFromMessage(buffer, MessageCreator.
							MESSAGE_INDEX_DESTINY);
					
					ParMaquinaProceso pmp=dameDestinatarioDesdeInterfaz();
					String ip = pmp.dameIP();
					
					long[] allProcessIds = getAllProcessIDs();
					boolean isLocalProcess = false;
					
					for(int i = 0; i < allProcessIds.length; i++)
					{
						if(allProcessIds[i] == destiny)
						{
							isLocalProcess = true;
							break;
						}
					}
		
					//Trigger to wait the process if it's not started.
					if(!isLocalProcess)
					{
						try
						{
							final int TIME_WAIT_START = 10000;//10 seconds.
							sleep(TIME_WAIT_START);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
							imprimeln("Error while waiting for the process to start.");
						}
					}
					
					if(isLocalProcess)
					{	
						//Check if destiny is waiting to receive a message.
						if(receptionTable.containsKey(destiny))
						{
							//Register the client.
							emitionTable.put(origin, new String[]{Integer.toString(origin), ip});
							
							//Copy the message to the receiver.
							receptionTable.put(destiny, buffer);
							Proceso proceso = dameProcesoLocal(destiny);
							//proceso.getLibrary().setAnswer(buffer);
							proceso.setMessage(buffer);
							reanudarProceso(proceso);
						}
						else
						{
							//Save the message to try again later.
							savedMessage = buffer;
							
							//Send TA.
							imprimeln(UIM_TA);
							byte[] message = new byte[MessageCreator.MESSAGE_MAX_SIZE];
							MessageCreator.setDestiny(destiny, message);
							MessageCreator.setInt(TA, message, MessageCreator
									.MESSAGE_INDEX_MESSAGE_TYPE);
							DatagramPacket packetAnswer = new DatagramPacket(message, message
									.length);
							dameSocketRecepcion().send(packetAnswer);
						}
					}
					else
					{
						//Send AU.
						imprimeln(UIM_AU);
						byte[] message = new byte[MessageCreator.MESSAGE_MAX_SIZE];
						MessageCreator.setDestiny(destiny, message);
						MessageCreator.setInt(AU, message, MessageCreator.MESSAGE_INDEX_MESSAGE_TYPE);
						DatagramPacket packetAnswer = new DatagramPacket(message, message.length);
						dameSocketRecepcion().send(packetAnswer);
					}
				}	
			}
			socket.close();
		}
		catch (SocketException e1)
		{
			e1.printStackTrace();
			imprimeln(UIM_ERROR_SOCKET);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			imprimeln(UIM_ERROR_RECEIVE_PACKET);
		}
	}
}
