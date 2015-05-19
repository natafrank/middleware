//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para pr�ctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreatorServer;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;
import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria
{
	
	/**
	 * 
	 */
	public LibreriaCliente(Escribano esc, byte[] message)
	{
		super(esc, message);
	}

	@Override
	protected void create()
	{
		int asaDest = RPC.importarInterfaz(Libreria.SERVER_NAME, Libreria.VERSION);
		
		if(asaDest < 0)
		{
			imprimeln("Couldn't find interface.");
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_CREATE_FILE));
		}
		else
		{
			Nucleo.send(asaDest, message);	
			imprimeln("Receiving Message");
			
			
			Nucleo.receive(idProcess, answer);
			imprimeln("Message Received");

			//Read the answer.
			parameters.push(Assembler.intToBytes(MessageReader.readIntFromMessage(answer, 
					MessageCreatorServer.MESSAGE_INDEX_SIMPLE_ANSWER)));
		}
	}

	@Override
	protected void read()
	{
		int asaDest = RPC.importarInterfaz(Libreria.SERVER_NAME, Libreria.VERSION);
		
		if(asaDest < 0)
		{
			imprimeln("Couldn't find interface.");
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_READ_FILE));
		}
		else
		{
			Nucleo.send(asaDest, message);	
			imprimeln("Receiving Message");
			
			
			Nucleo.receive(idProcess, answer);
			imprimeln("Message Received");

			//Read the answer.
			int result = MessageReader.readIntFromMessage(answer, 
					MessageCreatorServer.MESSAGE_INDEX_SIMPLE_ANSWER);
			
			//Trigger to add the buffer if exists to the parameters stack.
			if(result == FileServerOperationManager.SUCCESS_READ_FILE)
			{
				int bufferLengthPosition = MessageCreatorServer.MESSAGE_INDEX_SIMPLE_ANSWER + 
						MessageCreator.INT_BYTE_SIZE;
				int bufferLength = MessageReader.readIntFromMessage(answer, bufferLengthPosition);
				
				parameters.push(MessageReader.readStringFromMessage(answer, bufferLengthPosition
						+ MessageCreator.INT_BYTE_SIZE, (short) bufferLength)
						.getBytes());
			}
			
			parameters.push(Assembler.intToBytes(result));	
		}
	}

	@Override
	protected void write()
	{
		int asaDest = RPC.importarInterfaz(Libreria.SERVER_NAME, Libreria.VERSION);
		
		if(asaDest < 0)
		{
			imprimeln("Couldn't find interface.");
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_WRITE_FILE));
		}
		else
		{
			Nucleo.send(asaDest, message);	
			imprimeln("Receiving Message");
			
			
			Nucleo.receive(idProcess, answer);
			imprimeln("Message Received");

			//Read the answer.
			parameters.push(Assembler.intToBytes(MessageReader.readIntFromMessage(answer, 
					MessageCreatorServer.MESSAGE_INDEX_SIMPLE_ANSWER)));
		}
	}

	@Override
	protected void delete()
	{
		int asaDest = RPC.importarInterfaz(Libreria.SERVER_NAME, Libreria.VERSION);
		
		if(asaDest < 0)
		{
			imprimeln("Couldn't find interface.");
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_DELETE_FILE));
		}
		else
		{
			Nucleo.send(asaDest, message);	
			imprimeln("Receiving Message");
			
			
			Nucleo.receive(idProcess, answer);
			imprimeln("Message Received");

			//Read the answer.
			parameters.push(Assembler.intToBytes(MessageReader.readIntFromMessage(answer, 
					MessageCreatorServer.MESSAGE_INDEX_SIMPLE_ANSWER)));
		}
	}
}