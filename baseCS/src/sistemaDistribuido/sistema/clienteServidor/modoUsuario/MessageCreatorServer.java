package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageCreatorServer extends MessageCreator
{
	//CONSTANTS.
	private final static int DATA_MAX_SIZE = 1016;
	private final static int MESSAGE_INDEX_ID_SERVER = 0;
	private final static int MESSAGE_INDEX_ID_CLIENT = 4;
	private final static int MESSAGE_INDEX_DATA      = 8;
	
	public MessageCreatorServer(int idClient, int idServer, String data)
	{
		super(idClient, idServer, data);
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
			
			//Get server id.
			for(i = 0; i < INT_BYTE_SIZE; i++)
			{
				aux[(i-3)*-1] = (byte) (idServer >>> (i * 8));
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_ID_SERVER, INT_BYTE_SIZE);
			
			//Get client id.
			for(i = 0; i < INT_BYTE_SIZE; i++)
			{
				aux[(i-3)*-1] = (byte) (idClient >>> (i * 8));
			}
			System.arraycopy(aux, 0, message, MESSAGE_INDEX_ID_CLIENT, INT_BYTE_SIZE);
			
			//Get data.
			System.arraycopy(dataBytes, 0, message, MESSAGE_INDEX_DATA, dataBytes.length);
			
			//Return success.
			return MESSAGE_SUCCESSFULLY_CREATED;
		}
	}
}
