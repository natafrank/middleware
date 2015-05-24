//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.exclusion;

import java.util.LinkedList;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

public class CoordinatorProcess extends Proceso
{
	private LinkedList<Integer> queueFileServer;
	private LinkedList<Integer> queueHTTPServer;
	
	private boolean criticalSectionFileServerAvailable;
	private boolean criticalSectionHTTPServerAvailable;
	private boolean receivingMessages;
	
	public CoordinatorProcess(Escribano esc)
	{
		super(esc);
		receivingMessages = true;
		criticalSectionFileServerAvailable = true;
		criticalSectionHTTPServerAvailable = true;
		queueFileServer = new LinkedList<Integer>();
		queueHTTPServer = new LinkedList<Integer>();
		start();
	}

	public void setReceivingMessages(boolean receivingMessages)
	{
		this.receivingMessages = receivingMessages;
	}
	
	public boolean isReceivingMessages()
	{
		return receivingMessages;
	}
	@Override
	public void run()
	{
		while(isReceivingMessages())
		{
			imprimeln("Ready to receive messages...");
			Nucleo.receive(dameID(), message);
			imprimeln("Message Received.");
			byte[] answer = new byte[MessageCreator.MESSAGE_MAX_SIZE];
			int requestedTask = MessageReader.readIntFromMessage(message, MessageCreator
					.MESSAGE_INDEX_REQUEST_TASK);
			int idClient = MessageReader.readIntFromMessage(message, MessageCreator
					.MESSAGE_INDEX_ORIGIN);
			
			switch(requestedTask)
			{
				case MessageCreator.CRTICIAL_SECTION_REQUEST_TASK:
				{
					int criticalSection = MessageReader.readIntFromMessage(message, MessageCreator
							.MESSAGE_INDEX_CRITICAL_SECTION);
					switch(criticalSection)
					{
						case MessageCreator.CRITICAL_SECTION_FILE_SERVER:
						{
							if(criticalSectionFileServerAvailable)
							{
								criticalSectionFileServerAvailable = false;
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_FREE, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
							}
							else
							{
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_BUSSY, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
								queueFileServer.addLast(idClient);
							}
							//Send answer to client.
							Nucleo.send(idClient, answer);
							imprimeln("Answer sent.");
							break;
						}
						case MessageCreator.CRITICAL_SECTION_HTTP_SERVER:
						{
							if(criticalSectionHTTPServerAvailable)
							{
								criticalSectionHTTPServerAvailable = false;
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_FREE, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
							}
							else
							{
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_BUSSY, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
								queueHTTPServer.addLast(idClient);
							}
							//Send answer to client.
							Nucleo.send(idClient, answer);
							imprimeln("Answer sent.");
							break;
						}
					}
					break;
				}
				
				case MessageCreator.CRITICAL_SECTION_SET_FREE_TASK:
				{
					int criticalSection = MessageReader.readIntFromMessage(message, MessageCreator
							.MESSAGE_INDEX_CRITICAL_SECTION);
					
					switch(criticalSection)
					{
						case MessageCreator.CRITICAL_SECTION_FILE_SERVER:
						{	
							if(!queueFileServer.isEmpty())
							{
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_FREE, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
								Nucleo.send(queueFileServer.removeFirst(), answer);
							}
							else
							{
								criticalSectionFileServerAvailable = true;
								imprimeln("File server free.");
							}
							
							break;
						}
						case MessageCreator.CRITICAL_SECTION_HTTP_SERVER:
						{	
							if(!queueHTTPServer.isEmpty())
							{
								MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_FREE, 
										answer, MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
								Nucleo.send(queueHTTPServer.removeFirst(), answer);
							}
							else
							{
								criticalSectionHTTPServerAvailable = true;
								imprimeln("HTTP server free.");
							}
							
							break;
						}
					}
				}
			}//switch
		}
	}
}
