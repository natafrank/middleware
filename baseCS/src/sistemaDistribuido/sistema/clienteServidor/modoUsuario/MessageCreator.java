package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class MessageCreator
{
	//CONSTANTS.
	protected final static int MESSAGE_MAX_SIZE = 1024;
	protected final static int INT_BYTE_SIZE    = 4;
	protected final static int SHORT_BYTE_SIZE  = 2;
	
	//SERVER IDS.
	public final static int ID_FILE_SERVER = 248;
	
	//PORTS.
	public final static int PORT_FILE_SERVER  = 44555;
	
	//ADDRESSESS (IP'S).
	public final static String IP_FILE_SERVER = "127.0.0.1";//Localhost for this example.
	
	//RETURN CODES.
	public final static int MESSAGE_SUCCESSFULLY_CREATED = 0;
	public final static int ERROR_MESSAGE_TOO_LONG       = -1;
	public final static int ERROR_PORT		             = -2;
	public final static int ERROR_IP					 = -3;
	public final static int ERROR_ID					 = -4;
	
	//UI Messages (UIM).
	public final static String UIM_ERROR_MESSAGE_TOO_LONG = "Error while creating message "
			+ "message too long.";
	
	protected int idClient;
	protected int idServer;
	protected String data;
	protected byte[] dataBytes;
	protected byte[] message = new byte[MESSAGE_MAX_SIZE];
	
	public MessageCreator(int idClient, int idServer, String data)
	{
		this.idClient = idClient;
		this.idServer = idServer;
		this.data = data;
		dataBytes = data.getBytes();
	}
	
	public static int getPort(int idServer)
	{
		switch(idServer)
		{
			case ID_FILE_SERVER:
			{
				return PORT_FILE_SERVER;
			}
		}
		
		//If no id matches, return error.
		return ERROR_PORT;
	}
	
	public static String getAddress(int idServer)
	{
		switch(idServer)
		{
			case ID_FILE_SERVER:
			{
				return IP_FILE_SERVER;
			}
		}
		
		//If no id matches, return error.
		return Integer.toString(ERROR_IP);
	}
	
	public static int getIdServerByName(String name)
	{
		if(name.equals("File Server"))
		{
			return ID_FILE_SERVER;
		}
		
		//If no id matches, return error.
		return ERROR_ID;
	}
	
	public int getIdClinet()
	{
		return idClient;
	}
	
	public int getIdServer()
	{
		return idServer;
	}
	
	public byte[] getMessage()
	{
		return message;
	}
	
	public void setIdClient(int idClient)
	{
		this.idClient = idClient;
	}
	
	public void setIdServer(int idServer)
	{
		this.idServer = idServer;
	}
	
	public String getData() 
	{
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
	}
}
