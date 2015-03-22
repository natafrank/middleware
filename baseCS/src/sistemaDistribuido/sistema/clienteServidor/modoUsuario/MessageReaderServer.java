package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageReaderServer extends MessageReader
{
	private final static int MESSAGE_INDEX_ID_CLIENT      = 0;
	private final static int MESSAGE_INDEX_ID_SERVER      = 4;
	private final static int MESSAGE_INDEX_OPERATION_CODE = 8;
	private final static int MESSAGE_INDEX_DATA_SIZE      = 10;
	private final static int MESSAGE_INDEX_DATA           = 12;
	
	private boolean extraDataOnMessage;
	private int idClient;
	private int idServer;
	private int operationCode;
	private short dataSize;
	private String data;
	short extraDataSize;
	String extraData = "";
	
	public MessageReaderServer(byte[] message, boolean extraDataOnMessage)
	{
		super(message);
		this.extraDataOnMessage = extraDataOnMessage;
	}

	@Override
	public void readMessage()
	{
		//Read client id.
		idClient = readInt(MESSAGE_INDEX_ID_CLIENT);
		
		//Read server id.
		idServer = readInt(MESSAGE_INDEX_ID_SERVER);
		
		//Read operation code.
		operationCode = readShort(MESSAGE_INDEX_OPERATION_CODE);
		
		//Read data size.
		dataSize = readShort(MESSAGE_INDEX_DATA_SIZE);
		
		//Read data.
		data = readString(MESSAGE_INDEX_DATA, dataSize);
		
		//Trigger to read extra data.
		if(extraDataOnMessage)
		{
			int messageIndexExtraDataSize = MESSAGE_INDEX_DATA + data.length() - 1;
			
			extraDataSize = readShort(messageIndexExtraDataSize);
			extraData = readString(messageIndexExtraDataSize + 2, extraDataSize);
		}
		
		//INSTANCIATE THE APPROPIATE SERVER TO MAKE THE PROCEDURES REQUESTED BY THE CLIENT.
		
		//To print create a readeable meesage.
		readableMessage = "Client: " + idClient + "\nServer: " + idServer + "\nOperation: "
				+ operationCode + "\nData: " + data + " " + extraData;
	}
}
