package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.visual.rpc.DespleganteConexiones;

public class ProgramaConector{
	private DespleganteConexiones desplegante;
	private HashMap<ServerVersion,ArrayList<AsaId>> conexiones;   //las llaves que provee DespleganteConexiones
	
	//Variable that defines the next id's interface.
	private static int nextId;
	/**
	 * 
	 */
	public ProgramaConector(DespleganteConexiones desplegante){
		this.desplegante=desplegante;
		nextId = 0;
	}

	/**
	 * Inicializar tablas en programa conector
	 */
	public void inicializar(){
		conexiones=new HashMap<ServerVersion,ArrayList<AsaId>>();
	}

	/**
	 * Remueve tuplas visualizadas en la interfaz gr�fica registradas en tabla conexiones
	 */
	private void removerConexiones(){
		Collection<ArrayList<AsaId>> s = conexiones.values();
		Iterator<ArrayList<AsaId>> i=s.iterator();
		while(i.hasNext())
		{
			ArrayList<AsaId> serverAsas = i.next();
			for(int j = 0; j < serverAsas.size(); j++)
			{
				desplegante.removerServidor(Integer.parseInt(serverAsas.get(j).getId()));
				i.remove();
			}
		}
	}

	/**
	 * Al solicitar que se termine el proceso, por si se implementa como tal
	 */
	public void terminar() {
		removerConexiones();
		desplegante.finalizar();
	}
	
	/**
	 * This method adds the server to the connections table.
	 */
	public int registro(String server, String version, ParMaquinaProceso asa)
	{
		//Check if another server is registered with the same name and verison.
		ServerVersion serverVersion = getServerVersion(server, version);
		ArrayList<AsaId> serverAsas;
		
		if(conexiones.containsKey(serverVersion))
		{
			//Get the current asas.
			serverAsas = conexiones.get(serverVersion);			
		}
		else
		{
			serverVersion = new ServerVersion(server, version);
			serverAsas = new ArrayList<AsaId>();
		}
		int id = generateId(server, version);
		serverAsas.add(new AsaId(asa, String.valueOf(id)));
		conexiones.put(serverVersion, serverAsas);
		printInterfaces();
		return id;
	}
	
	/**
	 * This method removes the server from the connections table.
	 */
	public boolean deregistro(String server, String version, String id)
	{
		//Check if the server is registered.
		ServerVersion serverVersion = getServerVersion(server, version);
		
		//Exists.
		if(serverVersion != null)
		{
			//Get all the asas.
			ArrayList<AsaId> serverAsas = conexiones.get(serverVersion);
			boolean removed = false;
			
			//Remove the server from the array.
			for(int i = 0; i < serverAsas.size(); i++)
			{
				AsaId asaId = serverAsas.get(i);
				if(asaId.getId().equals(id))
				{
					serverAsas.remove(i);
					removed = true;
				}
			}
			printInterfaces();
			return removed;
		}
		//Doesn't exist.
		else
		{
			printInterfaces();
			return false;
		}
	}
	
	/**
	 * This method searchs for an available server to do a task.
	 */
	public AsaId busqueda(String server, String version)
	{
		//Check if the server exists.
		ServerVersion serverVersion = getServerVersion(server, version);
		
		//Exists.
		if(serverVersion != null)
		{
			//Get all the asas.
			ArrayList<AsaId> serverAsas = conexiones.get(serverVersion);
			
			//Return the next available server.
			for(int i = 0; i < serverAsas.size(); i ++)
			{
				AsaId asaId = serverAsas.get(i);
				
				if(asaId.isAvailable())
				{
					return asaId;
				}
			}
			
			//If there weren't any server available return null.
			return null;
		}
		//Doesn't exists.
		else
		{
			return null;
		}
	}
	
	public AsaId getAsaFromId(int id)
	{
		Collection<ArrayList<AsaId>> serverAsasArrayCollection = conexiones.values();
		Iterator<ArrayList<AsaId>> serverAsasArrayIterator = serverAsasArrayCollection.iterator();
		
		while(serverAsasArrayIterator.hasNext())
		{
			ArrayList<AsaId> serverAsasArray = serverAsasArrayIterator.next();
			
			for(int i = 0; i < serverAsasArray.size(); i++)
			{
				AsaId asa = serverAsasArray.get(i);
				
				//Trigger to return the found asa.
				if(Integer.parseInt(asa.getId()) == id)
				{
					return asa;
				}
			}
		}
		
		//If the id wasn't found return null to handle error.
		return null;
	}
	
	/**
	 * Method that creates the id for the server interface.
	 */
	public int generateId(String server, String version)
	{
		return nextId++;
	}
	
	/**
	 * Internal method to search and get the reference key from the HashMap of a server-version.
	 */
	private ServerVersion getServerVersion(String server, String version)
	{
		//Create the server-version from the parameters.
		ServerVersion serverVersion = new ServerVersion(server, version);
		
		//Get all the keys and check if any server is registered with that name and version.
		Set<ServerVersion> serverSet = conexiones.keySet();
		Iterator<ServerVersion> serverIterator = serverSet.iterator();
		ServerVersion serverVersionFromIterator = null;
		boolean found = false;
		
		while(serverIterator.hasNext())
		{
			serverVersionFromIterator = serverIterator.next();
			
			if(serverVersionFromIterator!= null && serverVersionFromIterator.equals(serverVersion))
			{
				found = true;
				break;
			}
		}
		
		if(found)
		{
			return serverVersionFromIterator;
		}
		else
		{
			return null;
		}
	}
	
	 public void printInterfaces()
	 {
		//Get all the keys.
		Set<ServerVersion> serverSet = conexiones.keySet();
		Iterator<ServerVersion> serverIterator = serverSet.iterator();
		ServerVersion serverVersionFromIterator = null;
		
		while(serverIterator.hasNext())
		{
			serverVersionFromIterator = serverIterator.next();
			
			if(serverVersionFromIterator!= null)
			{
				ArrayList<AsaId> serverAsas = conexiones.get(serverVersionFromIterator);
				
				for(int i = 0; i < serverAsas.size(); i++)
				{
					System.out.println("-------\n\nServer: " + serverVersionFromIterator.getServer()
							+ "\nVersion: " + serverVersionFromIterator.getVersion()
							+ "\nIP: " + serverAsas.get(i).getAsa().dameIP()
							+ "\nID: " + serverAsas.get(i).getAsa().dameID());
				}
			}
		}
	 }
}
