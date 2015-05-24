//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.exclusion;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

public class ClientProcess extends Proceso
{

	private int criticalSectionRequested;
	private int secondsToUseCriticalSection;
	private boolean interrupted;
	
	public ClientProcess(Escribano esc)
	{
		super(esc);
		start();
	}

	public void setCriticalSectionRequested(int criticalSectionRequested)
	{
		this.criticalSectionRequested = criticalSectionRequested;
	}
	
	public void setSecondsToUseCriticalSection(int secondsToUseCriticalSection)
	{
		this.secondsToUseCriticalSection = secondsToUseCriticalSection;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			interrupted = false;
			imprimeln("Client Ready.");
			imprimeln("Waiting data to continue...");
			Nucleo.suspenderProceso();
			message = new byte[MessageCreator.MESSAGE_MAX_SIZE];
			MessageCreator.setInt(MessageCreator.CRTICIAL_SECTION_REQUEST_TASK, message, 
					MessageCreator.MESSAGE_INDEX_REQUEST_TASK);
			MessageCreator.setInt(criticalSectionRequested, message, MessageCreator
					.MESSAGE_INDEX_CRITICAL_SECTION);
			Nucleo.send(248, message);
			imprimeln("Request sent...");
			
			imprimeln("Waiting for the answer...");
			Nucleo.receive(dameID(), message);
			
			
			int criticalSectionStatus = MessageReader.readIntFromMessage(message, 
					MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
			imprimeln("Answer received.");
			switch(criticalSectionStatus)
			{
				case MessageCreator.CRITICAL_SECTION_FREE:
				{
					imprimeln("Ready to use the critical section.");
					useCriticalSection();
					break;
				}
				case MessageCreator.CRITICAL_SECTION_BUSSY:
				{
					imprimeln("Waiting for the critical section to be free.");
					Nucleo.receive(dameID(), message);
					useCriticalSection();
					break;
				}
				default:
				{
					imprimeln("Error, the answer can't be handled.");
				}
			}
		}
	}
	
	private void useCriticalSection()
	{
		while(secondsToUseCriticalSection > 0 && !interrupted)
		{
			imprimeln("Using critical section: " + secondsToUseCriticalSection + 
					" left.");
			final int SECOND = 1000;
			try
			{
				sleep(SECOND);
				secondsToUseCriticalSection--;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		//Send critical section free.
		int idServer = MessageReader.readIntFromMessage(message, MessageCreator
				.MESSAGE_INDEX_ORIGIN);
		message = new byte[MessageCreator.MESSAGE_MAX_SIZE];
		
		MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_SET_FREE_TASK, message, 
				MessageCreator.MESSAGE_INDEX_REQUEST_TASK);
		MessageCreator.setInt(criticalSectionRequested, message, 
				MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION);
		MessageCreator.setInt(MessageCreator.CRITICAL_SECTION_FREE, message, 
				MessageCreator.MESSAGE_INDEX_CRITICAL_SECTION_STATUS);
		Nucleo.send(idServer, message);	
		
	}
	
	public void setInterrupted(boolean interrupted)
	{
		this.interrupted = interrupted;
	}
}
