package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Stack;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreatorClient;
import sistemaDistribuido.util.Escribano;

public abstract class Libreria
{
	/* Size for the origin, destiny, operation code and file name size.
	 * 
	 * 		origin          = 4 bytes.
	 * 		destiny         = 4 bytes.
	 *		operation code  = 2 bytes.
	 * 		file name size  = 2 bytes.
	 */
	private final static int HEADER_LENGTH = 12;
	
	private Escribano esc;

	//Parameters Stack.
	protected Stack<byte[]> parameters;
	
	//Message to send.
	protected byte[] message;
	
	//Process that owns the library.
	protected int idProcess;
	protected byte[] answer;


	
	/**
	 * 
	 */
	public Libreria(Escribano esc, byte[] message)
	{
		this.esc=esc;
		parameters = new Stack<byte[]>();
		this.message = message;
	}

	/**
	 * 
	 */
	protected void imprime(String s)
	{
		esc.imprime(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s)
	{
		esc.imprimeln(s);
	}

	//Bridge methods for the RPC.
	public int create(char[] fileName)
	{	
		short fileNameLength = (short) fileName.length;
		
		//Create the space for the message.
		if(message != null)
		{
			//Calculate message length and check if it is reusable. If it's not then activate
			//the trigger to resize the message space.
			if(message.length != HEADER_LENGTH + fileNameLength)
			{
				message = new byte[HEADER_LENGTH + fileNameLength];
			}
		}
		else
		{
			message = new byte[HEADER_LENGTH + fileNameLength];
		}
		
		/*
		 * Set the data to the message. 
		 */
		//Operation code.
		MessageCreator.setShort((short) FileServerOperationManager.CREATE, message, 
				MessageCreatorClient.MESSAGE_INDEX_OPERATION_CODE);
		
		//File name size.
		MessageCreator.setShort(fileNameLength, message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME_SIZE);
		
		//File name.
		MessageCreator.setString(new String(fileName), message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME);
		
		//Call the method implemented by the client.
		create();
		
		//Get the answer from the stack.
		return Assembler.bytesToInt(parameters.pop());
	}
	
	public int read(char[] fileName, char[] bufferRead, long bytesToRead, long startingPosition)
	{
		//Initialize variables.
		short fileNameLength = (short) fileName.length;
		
		//Create the space for the message.
		if(message != null)
		{
			//Calculate message length and check if it is reusable. If it's not then activate
			//the trigger to resize the message space.
			if(message.length != HEADER_LENGTH + fileNameLength + bytesToRead + (MessageCreator
					.LONG_BYTE_SIZE * 2))
			{
				message = new byte[HEADER_LENGTH + fileNameLength + (int) bytesToRead + 
				                   (MessageCreator.LONG_BYTE_SIZE * 2)];
			}
		}
		else
		{
			message = new byte[HEADER_LENGTH + fileNameLength + (int) bytesToRead + 
			                   (MessageCreator.LONG_BYTE_SIZE * 2)];
		}
		
		/*
		 * Set the data to the message. 
		 */
		//Operation code.
		MessageCreator.setShort((short) FileServerOperationManager.READ, message, 
				MessageCreatorClient.MESSAGE_INDEX_OPERATION_CODE);
		
		//File name size.
		MessageCreator.setShort(fileNameLength, message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME_SIZE);
		
		//File name.
		MessageCreator.setString(new String(fileName), message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME);
		
		//Bytes to read.
		int bytesToReadPosition = MessageCreatorClient.MESSAGE_INDEX_FILE_NAME + fileNameLength;
		MessageCreator.setLong(bytesToRead, message, bytesToReadPosition);
		
		//Starting position.
		MessageCreator.setLong(startingPosition, message, bytesToReadPosition + MessageCreator
				.LONG_BYTE_SIZE);
		
		//Call the method implemented by the client.
		read();
		
		return Assembler.bytesToInt(parameters.pop());
	}
	
	public int write(char[] fileName, char[] bufferToWrite, long bytesToWrite)
	{
		short fileNameLength = (short) fileName.length;
		int bufferToWriteLength = bufferToWrite.length;
		
		//Create the space for the message.
		if(message != null)
		{
			//Calculate message length and check if it is reusable. If it's not then activate
			//the trigger to resize the message space.
			if(message.length != HEADER_LENGTH + fileNameLength + bufferToWriteLength 
					+ MessageCreator.LONG_BYTE_SIZE )
			{
				message = new byte[HEADER_LENGTH + fileNameLength + bufferToWriteLength + 
				                   MessageCreator.LONG_BYTE_SIZE + MessageCreator.INT_BYTE_SIZE];
			}
		}
		else
		{
			message = new byte[HEADER_LENGTH + fileNameLength + bufferToWriteLength + 
			                   MessageCreator.LONG_BYTE_SIZE + MessageCreator.INT_BYTE_SIZE];
		}
		
		/*
		 * Set the data to the message. 
		 */
		//Operation code.
		MessageCreator.setShort((short) FileServerOperationManager.WRITE, message, 
				MessageCreatorClient.MESSAGE_INDEX_OPERATION_CODE);
		
		//File name size.
		MessageCreator.setShort(fileNameLength, message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME_SIZE);
		
		//File name.
		MessageCreator.setString(new String(fileName), message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME);
		
		//Buffer to write length.
		int bufferToWriteLengthPosition = MessageCreatorClient.MESSAGE_INDEX_FILE_NAME + 
				fileNameLength;
		MessageCreator.setInt(bufferToWriteLength, message, bufferToWriteLengthPosition);
		
		//Buffer to write.
		int bufferToWritePosition = bufferToWriteLengthPosition + MessageCreator.INT_BYTE_SIZE;
		MessageCreator.setString(new String(bufferToWrite), message, bufferToWritePosition);
		
		//Bytes to write.
		MessageCreator.setLong(bytesToWrite, message, bufferToWritePosition + bufferToWriteLength);
		
		//Call the method implemented by the client.
		write();
		
		//Get the answer from the stack.
		return Assembler.bytesToInt(parameters.pop());
	}
	
	public int delete(char[] fileName)
	{
		short fileNameLength = (short) fileName.length;
		
		//Create the space for the message.
		if(message != null)
		{
			//Calculate message length and check if it is reusable. If it's not then activate
			//the trigger to resize the message space.
			if(message.length != HEADER_LENGTH + fileNameLength)
			{
				message = new byte[HEADER_LENGTH + fileNameLength];
			}
		}
		else
		{
			message = new byte[HEADER_LENGTH + fileNameLength];
		}
		
		/*
		 * Set the data to the message. 
		 */
		//Operation code.
		MessageCreator.setShort((short) FileServerOperationManager.DELETE, message, 
				MessageCreatorClient.MESSAGE_INDEX_OPERATION_CODE);
		
		//File name size.
		MessageCreator.setShort(fileNameLength, message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME_SIZE);
		
		//File name.
		MessageCreator.setString(new String(fileName), message, MessageCreatorClient
				.MESSAGE_INDEX_FILE_NAME);
		
		//Call the method implemented by the client.
		delete();
		
		//Get the answer from the stack.
		return Assembler.bytesToInt(parameters.pop());
	}

	//Abstract methods for the RPC porpouse.
	protected abstract void create();
	protected abstract void read();
	protected abstract void write();
	protected abstract void delete();
	
	public Stack<byte[]> getParameters()
	{
		return parameters;
	}
	
	public void setIdProcess(int idProcess)
	{
		this.idProcess = idProcess;
	}
	
	public void setMessage(byte[] processMessage)
	{
		message = processMessage;
	}
	
	public void setAnswer(byte[] answer)
	{
		this.answer = answer;
	}
}