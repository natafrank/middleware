package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageCreatorClient extends MessageCreator
{
	//CONSTANTS.
	private final static int DATA_MAX_SIZE                = 1012;
	private final static int MESSAGE_INDEX_ID_CLIENT      = 0;
	private final static int MESSAGE_INDEX_ID_SERVER      = 4;
	private final static int MESSAGE_INDEX_OPERATION_CODE = 8;
	private final static int MESSAGE_INDEX_DATA_SIZE      = 10;
	private final static int MESSAGE_INDEX_DATA           = 12;
	
	private short operationCode;
	private short dataSize;
	
	public MessageCreatorClient(int idClient, int idServer, short operationCode, String data)
	{
		super(idClient, idServer, data);
		this.operationCode = operationCode;
		dataSize = (short) dataBytes.length;
	}

	public int createMessage()
	{
		//Check if data size doesn't overflow.
		if(dataBytes.length > DATA_MAX_SIZE)
		{
			//Error, data message too long.
			return ERROR_MESSAGE_TOO_LONG;
		}
		else
		{
			byte[] aux = new byte[INT_BYTE_SIZE];
			int i;
			
			//Get client id.
			for(i = 0; i < INT_BYTE_SIZE; i++)
			{
				aux[(i-3)*-1] = (byte) (idClient >>> (i * 8));
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_ID_CLIENT, INT_BYTE_SIZE);
			
			//Get server id.
			for(i = 0; i < INT_BYTE_SIZE; i++)
			{
				aux[(i-3)*-1] = (byte) (idServer >>> (i * 8));
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_ID_SERVER, INT_BYTE_SIZE);
			
			//Get the operation code.
			for(i = 0; i < SHORT_BYTE_SIZE; i++)
			{
				aux[(i-1)*-1] = (byte)(operationCode >>> (i * 8)); 
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_OPERATION_CODE, SHORT_BYTE_SIZE);
			
			//Get data size.
			for(i = 0; i < SHORT_BYTE_SIZE; i++)
			{
				aux[(i-1)*-1] = (byte)(dataSize >>> (i * 8));
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_DATA_SIZE, SHORT_BYTE_SIZE);
			
			//Get data.
			System.arraycopy(dataBytes, 0, message, MESSAGE_INDEX_DATA, dataBytes.length);
			
			//Return success.
			return MESSAGE_SUCCESSFULLY_CREATED;
		}
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
