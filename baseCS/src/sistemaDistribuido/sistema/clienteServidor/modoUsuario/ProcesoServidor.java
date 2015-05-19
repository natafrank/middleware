//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
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
		message = new byte[MessageCreator.MESSAGE_MAX_SIZE];
		while(continuar())
		{
			//We need to send the id of the server we start.
			Nucleo.receive(dameID(),message);
			imprimeln("Message Received");
			//Read the message.
			MessageReader messageReader = new MessageReader(message);
			
			Pausador.pausa(1000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("Processing request...");
			
			short operationCode = messageReader.readShort(MessageCreatorClient
					.MESSAGE_INDEX_OPERATION_CODE);
			short fileNameSize = messageReader.readShort(MessageCreatorClient
					.MESSAGE_INDEX_FILE_NAME_SIZE);
			String fileName = messageReader.readString(MessageCreatorClient.MESSAGE_INDEX_FILE_NAME, 
					fileNameSize);
			
			String answerToClient = "";
			switch(operationCode)
			{
			
				case FileServerOperationManager.CREATE:
				{
					try
					{
						PrintWriter file = new PrintWriter(fileName, 
								FileServerOperationManager.CHARACTER_ENCODING);
						file.close();
						answerToClient = FileServerOperationManager.UIM_FILE_CREATED_SUCCESS;
						imprimeln(answerToClient);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
						imprimeln(FileServerOperationManager.UIM_FILE_NOT_FOUND);
						answerToClient = FileServerOperationManager.UIM_FILE_CREATED_FAILURE;
						imprimeln(answerToClient);
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
						imprimeln(FileServerOperationManager.UIM_UNSUPPORTED_ENCODING);
						answerToClient = FileServerOperationManager.UIM_FILE_CREATED_FAILURE;
						imprimeln(answerToClient);
					}
					break;
				}
				case FileServerOperationManager.DELETE:
				{
					File file = new File(fileName);
					if(file.delete())
					{
						answerToClient = FileServerOperationManager.UIM_FILE_DELETED_SUCCESS;
						imprimeln(answerToClient);	
					}
					else
					{
						answerToClient = FileServerOperationManager.UIM_FILE_DELETED_FAILURE;
						imprimeln(answerToClient);
					}
					break;
				}
				case FileServerOperationManager.READ:
				{
					int offsetIndex = fileNameSize + MessageCreatorClient.MESSAGE_INDEX_FILE_NAME;
					int lengthIndex = offsetIndex + 4;
					int offset = messageReader.readInt(offsetIndex);
					int length = messageReader.readInt(lengthIndex);
					
					File file = new File(fileName);
					try
					{
						BufferedReader reader = new BufferedReader(new FileReader(file));
						char[] buffer = new char[MessageCreatorClient.DATA_MAX_SIZE];
						char[] returnBuffer = new char[MessageCreatorClient.DATA_MAX_SIZE];
						reader.read(buffer, 0, length + offset);
						System.arraycopy(buffer, offset, returnBuffer, 0, length);
						reader.close();
						answerToClient = new String(returnBuffer);
						imprimeln(FileServerOperationManager.UIM_FILE_READ_SUCCESS);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
						imprimeln(FileServerOperationManager.UIM_FILE_NOT_FOUND);
						answerToClient = FileServerOperationManager.UIM_FILE_READ_FAILURE;
						imprimeln(answerToClient);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						imprimeln(FileServerOperationManager.UIM_IO_ERROR);
						answerToClient = FileServerOperationManager.UIM_FILE_READ_FAILURE;
						imprimeln(answerToClient);
					}
					
					break;
				}
				case FileServerOperationManager.WRITE:
				{
					int writingTextSizeIndex = fileNameSize + MessageCreatorClient
							.MESSAGE_INDEX_FILE_NAME;
					int writingTextIndex = writingTextSizeIndex + 2;
					short writingTextSize = (short)messageReader.readShort(writingTextSizeIndex);
					String writingText = messageReader.readString(writingTextIndex, writingTextSize);
					File file = new File(fileName);
					try
					{
						FileWriter fileWriter = new FileWriter(file, true);
						BufferedWriter writer = new BufferedWriter(fileWriter);
						writer.write(writingText);
						writer.close();
						answerToClient = FileServerOperationManager.UIM_FILE_WRITE_SUCCESS;
						imprimeln(answerToClient);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						imprimeln(FileServerOperationManager.UIM_IO_ERROR);
						answerToClient = FileServerOperationManager.UIM_FILE_WRITE_FAILURE;
						imprimeln(answerToClient);
					}
					break;
				}
			}
			
			//Send answer to client.
			int idClient = messageReader.readInt(MessageCreator.MESSAGE_INDEX_ORIGIN);
			MessageCreatorServer messageCreatorServer = new MessageCreatorServer(answerToClient);
			messageCreatorServer.createMessage();
			Nucleo.send(idClient, messageCreatorServer.getMessage());
			imprimeln("Answer Sent.");
		}
	}
}
