//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.rpc.modoUsuario;

public class ServerVersion
{
	private String server;
	private String version;
	
	public ServerVersion(String server, String version)
	{
		this.server  = server;
		this.version = version;
	}
	
	public void setServer(String server)
	{
		this.server = server;
	}
	
	public void setVersion(String version)
	{
		this.version = version;
	}
	
	public String getServer()
	{
		return server;
	}
	
	public String getVersion()
	{
		return version;
	}

	@Override
	public boolean equals(Object obj)
	{
		ServerVersion serverVersion = (ServerVersion) obj;
		boolean result = false;

		if(serverVersion.server.equals(this.server) && serverVersion.version.equals(this.version))
		{
			result = true;
		}
		
		return result;
	}
}
