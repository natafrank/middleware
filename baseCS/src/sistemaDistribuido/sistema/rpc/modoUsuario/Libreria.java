package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Stack;

import sistemaDistribuido.util.Escribano;

public abstract class Libreria
{
	private Escribano esc;

	//Parameters Stack.
	protected Stack<byte[]> parameters;
	
	/**
	 * 
	 */
	public Libreria(Escribano esc)
	{
		this.esc=esc;
		parameters = new Stack<byte[]>();
	}

	/**
	 * 
	 */
	protected void imprime(String s)
	{
		esc.imprime(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s)
	{
		esc.imprimeln(s);
	}

	//Bridge methods for the RPC.
	public int create(char[] fileName)
	{
		return 0;
	}
	
	public int read(char[] fileName, char[] bufferReaded, long bytesToRead, long startingPosition)
	{
		return 0;
	}
	
	public int write(char[] fileName, char[] bufferToWrite, long bytesToWrite)
	{
		return 0;
	}
	
	public int delete(char[] fileName)
	{
		return 0;
	}

	//Abstract methods for the RPC porpouse.
	protected abstract void create();
	protected abstract void read();
	protected abstract void write();
	protected abstract void delete();
	
	public Stack<byte[]> getParameters()
	{
		return parameters;
	}
}