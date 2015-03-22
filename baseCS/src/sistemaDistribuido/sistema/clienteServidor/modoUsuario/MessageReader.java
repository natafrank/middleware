package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.util.Arrays;

public abstract class MessageReader
{
	protected byte[] message;
	protected String readableMessage;
	
	protected MessageReader(byte[] message)
	{
		this.message = message;
	}
	
	public abstract void readMessage();
	
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
		byte[] aux = Arrays.copyOfRange(message, position, position + length - 1);
		
		return new String(aux);
	}
	
	public String getReadableMessage()
	{
		return readableMessage;
	}
}
