package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

public class AsaId
{
	private ParMaquinaProceso asa;
	private String id;
	private boolean availability;
	
	public AsaId(ParMaquinaProceso asa, String id)
	{
		this.asa     = asa;
		this.id      = id;
		availability = true;
	}
	
	public ParMaquinaProceso getAsa()
	{
		return asa;
	}
	
	public void setAsa(ParMaquinaProceso asa)
	{
		this.asa = asa;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setAvailability(boolean availability)
	{
		this.availability = availability;
	}
	
	public boolean isAvailable()
	{
		return availability;
	}
}
