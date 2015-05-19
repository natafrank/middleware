//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public abstract class MessageCreator
{
	//CONSTANTS.
	public final static int MESSAGE_MAX_SIZE      	   = 1024;
	public final static int LONG_BYTE_SIZE		  	   = 8;
	public final static int INT_BYTE_SIZE        	   = 4;
	public final static int SHORT_BYTE_SIZE       	   = 2;
	public final static int MESSAGE_INDEX_ORIGIN  	   = 0;
	public final static int MESSAGE_INDEX_DESTINY 	   = 4;
	public final static int MESSAGE_INDEX_MESSAGE_TYPE = 4;
	
	//RETURN CODES.
	public final static int MESSAGE_SUCCESSFULLY_CREATED = 0;
	public final static int ERROR_MESSAGE_TOO_LONG       = -1;
	public final static int ERROR_MESSAGE_CREATION       = -2;

	//UI Messages (UIM).
	public final static String UIM_ERROR_MESSAGE_TOO_LONG = "Error while creating message "
			+ "message too long.";
	public final static String UIM_ERROR_MESSAGE_CREATION = "Error while creating message.";
	
	protected byte[] message = new byte[MESSAGE_MAX_SIZE];
	
	public byte[] getMessage()
	{
		return message;
	}
	
	public static void setLong(long value, byte[] message, int position)
	{
		byte[] aux = new byte[LONG_BYTE_SIZE];
		for(int i = 0; i < LONG_BYTE_SIZE; i++)
		{
			aux[(i-7)*-1] = (byte) (value >>> (i * 8));
		}
		System.arraycopy(aux, 0, message, position, LONG_BYTE_SIZE);
	}
	
	public static void setInt(int value, byte[] message, int position)
	{
		byte[] aux = new byte[INT_BYTE_SIZE];
		for(int i = 0; i < INT_BYTE_SIZE; i++)
		{
			aux[(i-3)*-1] = (byte) (value >>> (i * 8));
		}
		System.arraycopy(aux, 0, message, position, INT_BYTE_SIZE);
	}
	
	public static void setShort(short value, byte[] message, int position)
	{
		byte[] aux = new byte[SHORT_BYTE_SIZE];
		for(int i = 0; i < SHORT_BYTE_SIZE; i++)
		{
			aux[(i-1)*-1] = (byte)(value >>> (i * 8)); 
		}
		System.arraycopy(aux, 0, message, position, SHORT_BYTE_SIZE);
	}
	
	public static void setString(String value, byte[] message, int position)
	{
		byte[] valueBytes = value.getBytes();
		System.arraycopy(valueBytes, 0, message, position, valueBytes.length);
	}
	
	public static void setDestiny(int destiny, byte[] message)
	{
		setInt(destiny, message, MESSAGE_INDEX_DESTINY);
	}
	
	public static void setOrigin(int origin, byte[] message)
	{
		setInt(origin, message, MESSAGE_INDEX_ORIGIN);
	}
}
