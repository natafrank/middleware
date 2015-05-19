//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.rpc.modoUsuario;

public class Assembler
{
	public final static int LONG_BYTE_SIZE  = 8;
	public final static int INT_BYTE_SIZE   = 4;
	public final static int SHORT_BYTE_SIZE = 2;
	
	public static byte[] longToBytes(long number)
	{
		byte[] aux = new byte[LONG_BYTE_SIZE];
		for(int i = 0; i < LONG_BYTE_SIZE; i++)
		{
			aux[(i-(LONG_BYTE_SIZE-1))*-1] = (byte) (number >>> (i * 8));
		}
		
		return aux;
	}
	
	public static byte[] intToBytes(int number)
	{
		byte[] aux = new byte[INT_BYTE_SIZE];
		for(int i = 0; i < INT_BYTE_SIZE; i++)
		{
			aux[(i-(INT_BYTE_SIZE-1))*-1] = (byte) (number >>> (i * 8));
		}
		
		return aux;
	}
	
	public static byte[] shortToBytes(short number)
	{
		byte[] aux = new byte[SHORT_BYTE_SIZE];
		for(int i = 0; i < SHORT_BYTE_SIZE; i++)
		{
			aux[(i-(SHORT_BYTE_SIZE-1))*-1] = (byte)(number >>> (i * 8)); 
		}
		
		return aux;
	}
	
	public static long bytesToLong(byte[] buffer)
	{
		return buffer[0] << 56 | (buffer[1] & 0xFF) << 48 | (buffer[2] & 0xFF) << 40 |
				(buffer[3] & 0xFF) << 32 | (buffer[4] & 0xFF) << 24 | (buffer[5] & 0xFF)
				<< 16 | (buffer[6] & 0xFF) << 8 | (buffer[7] & 0xFF);
	}
	
	public static int bytesToInt(byte[] buffer)
	{
		return buffer[0] << 24  | (buffer[1] & 0xFF) << 16 | (buffer[2] & 0xFF) << 8 | 
				(buffer[3] & 0xFF);
	}
	
	public static short bytesToShort(byte[] buffer)
	{
		return (short) (buffer[0] << 8 | (buffer[1] & 0xFF));
	}
}
