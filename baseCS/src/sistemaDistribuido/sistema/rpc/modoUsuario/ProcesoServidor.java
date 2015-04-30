package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para pr�ctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreatorClient;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreatorServer;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	//private LibreriaServidor library;
	private byte[] answer;
	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		library = new LibreriaServidor(esc, message);
		answer = null;
		start();
	}

	/**
	 * Resguardo del servidor
	 */
	public void run()
	{
		imprimeln("Proceso servidor en ejecucion.");
		//idUnico=RPC.exportarInterfaz("FileServer", "3.1", asa)  //para pr�ctica 4

		while(continuar())
		{
			Nucleo.receive(dameID(), library.answer);

			//Read the message and add the parameters to the stack.
			short operationCode = MessageReader.readShortFromMessage(library.answer, MessageCreatorClient
					.MESSAGE_INDEX_OPERATION_CODE);
			int idClient = MessageReader.readIntFromMessage(library.answer, MessageCreator
					.MESSAGE_INDEX_ORIGIN);
			
			switch(operationCode)
			{
				case FileServerOperationManager.CREATE:
				{
					//Get the data.
					short fileNameSize = MessageReader.readShortFromMessage(library.answer, 
							MessageCreatorClient.MESSAGE_INDEX_FILE_NAME_SIZE);
					library.parameters.push(Assembler.shortToBytes(fileNameSize));
					library.parameters.push(MessageReader.readStringFromMessage(library.answer, 
						MessageCreatorClient.MESSAGE_INDEX_FILE_NAME, fileNameSize).getBytes());
					
					//Call the function.
					library.create();
					
					//Get the answer and make the message.
					int result = Assembler.bytesToInt(library.parameters.pop());
					
					if(answer != null)
					{
						//Trigger to resize the space of thew answer if necesary.
						if(answer.length != FileServerOperationManager.ANSWER_LENGTH)
						{
							answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
						}
					}
					else
					{
						answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
					}
					
					MessageCreator.setInt(result, answer, MessageCreatorServer
							.MESSAGE_INDEX_SIMPLE_ANSWER);
					
					break;
				}//CREATE
				
				case FileServerOperationManager.READ:
				{
					//Get the data.
					short fileNameSize = MessageReader.readShortFromMessage(library.answer, 
							MessageCreatorClient.MESSAGE_INDEX_FILE_NAME_SIZE);
					library.parameters.push(Assembler.shortToBytes(fileNameSize));
					library.parameters.push(MessageReader.readStringFromMessage(library.answer, 
						MessageCreatorClient.MESSAGE_INDEX_FILE_NAME, fileNameSize).getBytes());
					
					//Bytes to read.
					int bytesToReadPosition = MessageCreatorClient.MESSAGE_INDEX_FILE_NAME + 
							fileNameSize;
					long bytesToRead = MessageReader.readLongFromMessage(library.answer, 
							bytesToReadPosition);
					library.parameters.push(Assembler.longToBytes(bytesToRead));
					
					//Starting position.
					library.parameters.push(Assembler.longToBytes(MessageReader
							.readLongFromMessage(library.answer, bytesToReadPosition + 
									MessageCreator.LONG_BYTE_SIZE)));
					
					//Call the function.
					library.read();
					
					//Get the answer and make the message.
					int result = Assembler.bytesToInt(library.parameters.pop());
					
					switch(result)
					{
						case FileServerOperationManager.SUCCESS_READ_FILE:
						{
							//Get the buffer.
							int bufferReadLength = Assembler.bytesToInt(library.parameters.pop()); 
							String bufferRead    = new String(library.parameters.pop());
							
							if(answer != null)
							{
								//Trigger to resize the space of thew answer if necesary.
								if(answer.length != FileServerOperationManager.ANSWER_LENGTH + 
										bytesToRead + MessageCreator.INT_BYTE_SIZE)
								{
									answer = new byte[FileServerOperationManager.ANSWER_LENGTH + 
									             (int) bytesToRead + MessageCreator.INT_BYTE_SIZE];
								}
							}
							else
							{
								answer = new byte[FileServerOperationManager.ANSWER_LENGTH + 
								               (int) + bytesToRead + MessageCreator.INT_BYTE_SIZE];
							}
							
							MessageCreator.setInt(result, answer, MessageCreatorServer
									.MESSAGE_INDEX_SIMPLE_ANSWER);
							
							int bufferReadLengthPosition = MessageCreatorServer
									.MESSAGE_INDEX_SIMPLE_ANSWER + MessageCreator.INT_BYTE_SIZE;
							MessageCreator.setInt(bufferReadLength, answer, 
									bufferReadLengthPosition);
							
							MessageCreator.setString(bufferRead, answer, bufferReadLengthPosition
									+ MessageCreator.INT_BYTE_SIZE);
							
							break;
						}
						
						case FileServerOperationManager.ERROR_READ_FILE:
						{
							break;
						}
					}
					
					break;
				}//READ
				
				case FileServerOperationManager.WRITE:
				{
					//Get the data.
					short fileNameSize = MessageReader.readShortFromMessage(library.answer, 
							MessageCreatorClient.MESSAGE_INDEX_FILE_NAME_SIZE);
					library.parameters.push(Assembler.shortToBytes(fileNameSize));
					library.parameters.push(MessageReader.readStringFromMessage(library.answer, 
						MessageCreatorClient.MESSAGE_INDEX_FILE_NAME, fileNameSize).getBytes());
					
					//Buffer to write length.
					int bufferToWriteLengthPosition = MessageCreatorClient.MESSAGE_INDEX_FILE_NAME + 
							fileNameSize;
					int bufferToWriteLength = MessageReader.readIntFromMessage(library.answer,
							bufferToWriteLengthPosition);
					library.parameters.push(Assembler.intToBytes(bufferToWriteLength));
					
					//Buffer to write.
					int bufferToWritePosition = bufferToWriteLengthPosition + MessageCreator
							.INT_BYTE_SIZE;
					library.parameters.push(MessageReader.readStringFromMessage(library.answer, 
							bufferToWritePosition, (short) bufferToWriteLength).getBytes());
					
					//Bytes to write.
					library.parameters.push(Assembler.longToBytes(MessageReader
							.readLongFromMessage(library.answer, bufferToWritePosition + 
									bufferToWriteLength)));
					
					//Call the function.
					library.write();
					
					//Get the answer and make the message.
					int result = Assembler.bytesToInt(library.parameters.pop());
					
					if(answer != null)
					{
						//Trigger to resize the space of the answer if necesary.
						if(answer.length != FileServerOperationManager.ANSWER_LENGTH)
						{
							answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
						}
					}
					else
					{
						answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
					}
					
					MessageCreator.setInt(result, answer, MessageCreatorServer
							.MESSAGE_INDEX_SIMPLE_ANSWER);
					
					break;
				}//WRITE
				
				case FileServerOperationManager.DELETE:
				{
					//Get the data.
					short fileNameSize = MessageReader.readShortFromMessage(library.answer, 
							MessageCreatorClient.MESSAGE_INDEX_FILE_NAME_SIZE);
					library.parameters.push(Assembler.shortToBytes(fileNameSize));
					library.parameters.push(MessageReader.readStringFromMessage(library.answer, 
						MessageCreatorClient.MESSAGE_INDEX_FILE_NAME, fileNameSize).getBytes());
					
					//Call the function.
					library.delete();
					
					//Get the answer and make the message.
					int result = Assembler.bytesToInt(library.parameters.pop());
					
					if(answer != null)
					{
						//Trigger to resize the space of thew answer if necesary.
						if(answer.length != FileServerOperationManager.ANSWER_LENGTH)
						{
							answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
						}
					}
					else
					{
						answer = new byte[FileServerOperationManager.ANSWER_LENGTH];
					}
					
					MessageCreator.setInt(result, answer, MessageCreatorServer
							.MESSAGE_INDEX_SIMPLE_ANSWER);
					
					break;
				}//DELETE
			}
			
			//Send the message.
			Nucleo.send(idClient, answer);
			imprimeln("Answer Sent.");
		}

		//RPC.deregistrarInterfaz(nombreServidor, version, idUnico)  //para pr�ctica 4
	}
}
