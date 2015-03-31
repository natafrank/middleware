package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.util.Arrays;

public class MessageReader
{
	private byte[] message;
	
	public MessageReader(byte[] message)
	{
		this.message = message;
	}
	
	public int readInt(int position)
	{
		//Create a subarray.
		byte[] aux = Arrays.copyOfRange(message, position, position + 4);
		
		return aux[0] << 24  | (aux[1] & 0xFF) << 16 | (aux[2] & 0xFF) << 8 | (aux[3] & 0xFF);
	}
	
	public short readShort(int position)
	{
		//Create a subarray.
		byte[] aux = Arrays.copyOfRange(message, position, position + 2);
		
		return (short) (aux[0] << 8 | (aux[1] & 0xFF));
	}
	
	public String readString(int position, short length)
	{
		//Create a subarray.
		byte[] aux = Arrays.copyOfRange(message, position, position + length);
		
		return new String(aux);
	}
	
	public static int readIntFromMessage(byte[] message , int position)
	{
		//Create a subarray.
		byte[] aux = Arrays.copyOfRange(message, position, position + 4);
		
		return aux[0] << 24  | (aux[1] & 0xFF) << 16 | (aux[2] & 0xFF) << 8 | (aux[3] & 0xFF);
	}
}
