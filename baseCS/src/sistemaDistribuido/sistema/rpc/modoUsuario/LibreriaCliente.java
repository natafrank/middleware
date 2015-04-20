package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para práctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria
{
	/**
	 * 
	 */
	public LibreriaCliente(Escribano esc)
	{
		super(esc);		
	}

	@Override
	protected void create()
	{
		int asaDest=0;
		Nucleo.send(asaDest, null);
	}

	@Override
	protected void read()
	{
	}

	@Override
	protected void write()
	{
	}

	@Override
	protected void delete()
	{
	}
}