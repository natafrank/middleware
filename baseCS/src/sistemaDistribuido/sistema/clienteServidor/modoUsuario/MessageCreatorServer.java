package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageCreatorServer extends MessageCreator
{
	//CONSTANTS.
	public final static int DATA_MAX_SIZE               = 1014;
	public final static int MESSAGE_INDEX_DATA_SIZE     = 8;
	public final static int MESSAGE_INDEX_DATA          = 10;
	public final static int MESSAGE_INDEX_SIMPLE_ANSWER = 8;
	
	private final static String ANSWER_ERROR_MESSAGE_TOO_LONG = "Error while creating answer.";
	
	private String data;
	private short dataSize;
	
	public MessageCreatorServer(String data)
	{
		this.data = data;
		dataSize  = (short)data.length();
	}
	
	public int createMessage()
	{
		//Check if data size doesn't overflow.
		if(data.length() > DATA_MAX_SIZE)
		{
			//Error, data message too long.
			dataSize = (short) ANSWER_ERROR_MESSAGE_TOO_LONG.length();
			setShort(dataSize, message, MESSAGE_INDEX_DATA_SIZE);
			setString(ANSWER_ERROR_MESSAGE_TOO_LONG, message, MESSAGE_INDEX_DATA);
			return ERROR_MESSAGE_TOO_LONG;
		}
		else
		{
			setShort(dataSize, message, MESSAGE_INDEX_DATA_SIZE);
			setString(data, message, MESSAGE_INDEX_DATA);
			
			//Return success.
			return MESSAGE_SUCCESSFULLY_CREATED;
		}
	}
}
