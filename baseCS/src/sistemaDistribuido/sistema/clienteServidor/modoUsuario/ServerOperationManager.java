package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public abstract class ServerOperationManager
{
	//SERVERS.
	public final static String[] SERVERS = {"Choose a server" ,"File Server"};
	
	private int idServer;
	protected String selectedOperation;
	private boolean parametersEnabled;
	private boolean parameter1Enabled;
	private boolean parameter2Enabled;
	
	public abstract String[] getOperations();
	
	public abstract String[] getFirstParameters();
	
	public abstract String[] getSecondParameters();
	
	public abstract void setSelectedOperation(String selectedOperation);
	
	public void setParameter1Enabled(boolean b)
	{
		parameter1Enabled = b;
	}
	
	public void setParameter2Enabled(boolean b)
	{
		parameter2Enabled = b;
	}
	
	public boolean isParameter1Enabled()
	{
		return parameter1Enabled;
	}
	
	public boolean isParameter2Enabled()
	{
		return parameter2Enabled;
	}
	
	public void setParametersEnabled(boolean b)
	{
		this.parametersEnabled = b;
	}
	
	public boolean hasParametersEnabled()
	{
		return parametersEnabled;
	}
	
	public String getSelectedOperatio()
	{
		return selectedOperation;
	}
	
	public int getIdServer()
	{
		return idServer;
	}
}
