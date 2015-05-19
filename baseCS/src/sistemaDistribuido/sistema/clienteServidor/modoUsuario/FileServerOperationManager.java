//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

public class FileServerOperationManager extends ServerOperationManager
{


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
	
	public final static int CREATE = 1;
	public final static int DELETE = 2;
	public final static int READ   = 3;
	public final static int WRITE  = 4;
	public final static String CHARACTER_ENCODING = "UTF-8";
	
	public final static int ANSWER_LENGTH = 12;
	
	//SUCCESS CODES.
	public final static int SUCCESS_CREATE_FILE = 1;
	public final static int SUCCESS_READ_FILE   = 2;
	public final static int SUCCESS_WRITE_FILE  = 3;
	public final static int SUCCESS_DELETE_FILE = 4;
	
	//ERROR CODES.
	public final static int ERROR_CREATE_FILE = -1;
	public final static int ERROR_READ_FILE   = -2;
	public final static int ERROR_WRITE_FILE  = -3;
	public final static int ERROR_DELETE_FILE = -4;
	
	//UIM.
	public final static String UIM_FILE_NOT_FOUND       = "File not found exception.";
	public final static String UIM_IO_ERROR				= "Input/Output Error";
	public final static String UIM_UNSUPPORTED_ENCODING = "Unsoppurted Encoding.";
	public final static String UIM_FILE_CREATED_SUCCESS = "File created success.";
	public final static String UIM_FILE_CREATED_FAILURE = "File created failure.";
	public final static String UIM_FILE_DELETED_SUCCESS = "File deleted success.";
	public final static String UIM_FILE_DELETED_FAILURE = "File deleted failure.";
	public final static String UIM_FILE_READ_SUCCESS    = "File reading success.";
	public final static String UIM_FILE_READ_FAILURE    = "File reading failure.";
	public final static String UIM_FILE_WRITE_SUCCESS   = "File writing success.";
	public final static String UIM_FILE_WRITE_FAILURE   = "File writing failure.";
	
	private final static String[] FILE_NAMES = 
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

	public String[] getFileNames()
	{
		return FILE_NAMES;
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

	@Override
	public String[] getFirstParameters()
	{
		return null;
	}
	
	public static void cleanBuffer(byte[] buffer)
	{
		for(int i = 0; i < buffer.length; i++)
		{
			buffer[i] = 0;
		}
	}
}
