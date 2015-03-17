package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class FileServerOperationManager extends ServerOperationManager
{
	public FileServerOperationManager(int idServer)
	{
		super(idServer);
	}

	//OPERATIONS FOR EVERY SERVER.
	//FILE SERVER.
	private final static String[] OPERATIONS_FILE_SERVER =
	{
		"Choose an operation",
		"Create",
		"Delete",
		"Read",
		"Write"
	};
	
	private final static String[] PARAMETER1_FILE_SERVER = 
	{
		"hola.txt",
		"prueba.txt"
	};
	
	private final static String[] PARAMETER2_FILE_SERVER =
	{
		"Texto de prueba para archivos.",
		"Otro mensaje para escribir dentro de un archivo.",
		"5",
		"10"
	};

	@Override
	public String[] getOperations()
	{
		return OPERATIONS_FILE_SERVER;
	}

	@Override
	public void setSelectedOperation(String selectedOperation)
	{
		//Set the operation.
		this.selectedOperation = selectedOperation;
		
		if(selectedOperation.equals("Create"))
		{
			setParametersEnabled(true);
			setParameter1Enabled(true);
			setParameter2Enabled(false);
		}
		else if(selectedOperation.equals("Delete"))
		{
			setParametersEnabled(true);
			setParameter1Enabled(true);
			setParameter2Enabled(false);
		}
		else if(selectedOperation.equals("Read"))
		{
			setParametersEnabled(true);
			setParameter1Enabled(true);
			setParameter2Enabled(true);
		}
		else if(selectedOperation.equals("Write"))
		{
			setParametersEnabled(true);
			setParameter1Enabled(true);
			setParameter2Enabled(true);
		}
	}

	@Override
	public String[] getFirstParameters()
	{
		return PARAMETER1_FILE_SERVER;
	}

	@Override
	public String[] getSecondParameters()
	{
		if(selectedOperation.equals("Write"))
		{
			return new String[]{ PARAMETER2_FILE_SERVER[0], PARAMETER2_FILE_SERVER[1] };
		}
		else if(selectedOperation.equals("Read"))
		{
			return new String[]{ PARAMETER2_FILE_SERVER[2], PARAMETER2_FILE_SERVER[3] };
		}
		
		//Return null in case of error.
		return null;
	}
}
