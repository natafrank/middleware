//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import sistemaDistribuido.sistema.rpc.modoUsuario.Assembler;

public class MessageCreatorClock
{
	public final static int SHORT_BYTE_SIZE = 2;
	public final static int INT_BYTE_SIZE   = 4;
	public final static int LONG_BYTE_SIZE  = 8;
	
	//SIGNALS.
	public final static int SIGNAL_START_CLOCK = 1;
	public final static int SIGNAL_ENTER_GROUP = 2;
	public final static int SIGNAL_REAL_START  = 3;
	public final static int SIGNAL_REPORT_TIME = 4;
	
	//INDEXES.
	public final static int INDEX_SIGNAL  = 0;
	public final static int INDEX_IP_SIZE = 4;
	public final static int INDEX_GET_ID  = 4;
	public final static int INDEX_IP	  = 6;
	public final static int INDEX_TIME	  = 4;
	public final static int INDEX_CLIENT  = 12;
	
	public static void setShort(short value, byte[] message, int position)
	{	
		System.arraycopy(Assembler.shortToBytes(value), 0, message, position, SHORT_BYTE_SIZE);
	}
	
	public static void setInt(int value, byte[] message, int position)
	{	
		System.arraycopy(Assembler.intToBytes(value), 0, message, position, INT_BYTE_SIZE);
	}
	
	public static void setLong(long value, byte[] message, int position)
	{
		System.arraycopy(Assembler.longToBytes(value), 0, message, position, LONG_BYTE_SIZE);
	}
	
	public static void setString(String value, byte[] message, int position)
	{
		byte[] valueBytes = value.getBytes();
		System.arraycopy(valueBytes, 0, message, position, valueBytes.length);
	}
}
