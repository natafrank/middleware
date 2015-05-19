//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageCreatorClient extends MessageCreator
{
	//CONSTANTS.
	//Data max size Without origin,destiny, opc and filename size.
	public final static int DATA_MAX_SIZE                = 1012;
	public final static int MESSAGE_INDEX_OPERATION_CODE = 8;
	public final static int MESSAGE_INDEX_FILE_NAME_SIZE = 10;
	public final static int MESSAGE_INDEX_FILE_NAME      = 12;
	
	private short operationCode;
	private String fileName;
	private short fileNameSize;
	private int offset;
	private int length;
	private String writingText;
	private short writingTextSize;
	
	//Constructor for CREATE and DELETE.
	public MessageCreatorClient(short operationCode, String fileName)
	{
		this.operationCode = operationCode;
		this.fileName      = fileName;
		fileNameSize	   = (short) fileName.length();
	}
	
	//Constructor for READ.
	public MessageCreatorClient(short operationCode, String fileName, int offset, int length)
	{
		this.operationCode = operationCode;
		this.fileName      = fileName;
		fileNameSize	   = (short) fileName.length();
		this.offset		   = offset;
		this.length		   = length;
	}
	
	//Constructor for WRITE.
	public MessageCreatorClient(short operationCode, String fileName, String writingText)
	{
		this.operationCode = operationCode;
		this.fileName      = fileName;
		fileNameSize	   = (short) fileName.length();
		this.writingText   = writingText;
		writingTextSize    = (short) writingText.length();
	}

	public int createMessage()
	{
		switch(operationCode)
		{
			case FileServerOperationManager.CREATE:
			{
				if(fileNameSize > DATA_MAX_SIZE)
				{
					return ERROR_MESSAGE_TOO_LONG;
				}
				else
				{
					setShort(operationCode, message, MESSAGE_INDEX_OPERATION_CODE);
					setShort(fileNameSize, message, MESSAGE_INDEX_FILE_NAME_SIZE);
					setString(fileName, message, MESSAGE_INDEX_FILE_NAME);
					return MESSAGE_SUCCESSFULLY_CREATED;
				}
			}
			case FileServerOperationManager.DELETE:
			{
				if(fileNameSize > DATA_MAX_SIZE)
				{
					return ERROR_MESSAGE_TOO_LONG;
				}
				else
				{
					setShort(operationCode, message, MESSAGE_INDEX_OPERATION_CODE);
					setShort(fileNameSize, message, MESSAGE_INDEX_FILE_NAME_SIZE);
					setString(fileName, message, MESSAGE_INDEX_FILE_NAME);
					return MESSAGE_SUCCESSFULLY_CREATED;
				}
			}
			case FileServerOperationManager.READ:
			{
				final int OFFSET_LENGTH_SIZE = 8;
				
				if(fileNameSize + OFFSET_LENGTH_SIZE > DATA_MAX_SIZE)
				{
					return ERROR_MESSAGE_TOO_LONG;
				}
				else
				{
					setShort(operationCode, message, MESSAGE_INDEX_OPERATION_CODE);
					setShort(fileNameSize, message, MESSAGE_INDEX_FILE_NAME_SIZE);
					setString(fileName, message, MESSAGE_INDEX_FILE_NAME);
					int offsetIndex = MESSAGE_INDEX_FILE_NAME + fileNameSize;
					setInt(offset, message, offsetIndex);
					setInt(length, message, offsetIndex + 4);
					return MESSAGE_SUCCESSFULLY_CREATED;
				}
			}
			case FileServerOperationManager.WRITE:
			{
				final int WRITING_TEXT_SIZE_SIZE = 2;
				
				if(fileNameSize + writingTextSize + WRITING_TEXT_SIZE_SIZE > DATA_MAX_SIZE)
				{
					return ERROR_MESSAGE_TOO_LONG;
				}
				else
				{
					setShort(operationCode, message, MESSAGE_INDEX_OPERATION_CODE);
					setShort(fileNameSize, message, MESSAGE_INDEX_FILE_NAME_SIZE);
					setString(fileName, message, MESSAGE_INDEX_FILE_NAME);
					int writingTextSizeIndex = MESSAGE_INDEX_FILE_NAME + fileNameSize;
					setShort(writingTextSize, message, writingTextSizeIndex);
					setString(writingText, message, writingTextSizeIndex + 2);
					return MESSAGE_SUCCESSFULLY_CREATED;
				}
			}
		}
		
		//If no matches, return error.
		return ERROR_MESSAGE_CREATION;
	}
	
	public short getOperationCode() 
	{
		return operationCode;
	}

	public void setOperationCode(short operationCode) 
	{
		this.operationCode = operationCode;
	}
}
