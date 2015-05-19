//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso
{
	//private Libreria library;
	
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		//lib=new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
		library=new LibreriaCliente(esc, message);  //luego con esta comentando la anterior, para subrutina servidor remota
		library.setIdProcess(dameID());
		
		start();
	}

	
	/**
	 * Programa Cliente
	 */
	public void run()
	{
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Salio de suspenderProceso");
		
		//Get the parameters and process the tasks.
		//DELETE.
		char[] fileNameDelete = new String(library.parameters.pop()).toCharArray();
		
		//READ.
		long startReadingPosition = Assembler.bytesToLong(library.parameters.pop());
		long bytesToRead          = Assembler.bytesToLong(library.parameters.pop());
		char[] bufferRead	      = new String(library.parameters.pop()).toCharArray();
		char[] fileNameRead       = new String(library.parameters.pop()).toCharArray();
		
		//WRITE.
		long bytesToWrite    = Assembler.bytesToLong(library.parameters.pop());
		char[] bufferToWrite = new String(library.parameters.pop()).toCharArray();
		char[] fileNameWrite = new String(library.parameters.pop()).toCharArray();
		
		//CREATE.
		char[] fileNameCreate = new String(library.parameters.pop()).toCharArray();
		
		//Call the methods.
		int result;
		
		//CREATE.
		result = library.create(fileNameCreate);
		switch(result)
		{
			case FileServerOperationManager.SUCCESS_CREATE_FILE:
			{
				imprimeln("File: " + new String(fileNameCreate) + " created succesfully.");
				break;
			}
			case FileServerOperationManager.ERROR_CREATE_FILE:
			{
				imprimeln("Error while creating File: " + new String(fileNameCreate) + ".");
				break;
			}
		}
		
		//WRITE.
		result = library.write(fileNameWrite, bufferToWrite, bytesToWrite);
		switch(result)
		{
			case FileServerOperationManager.SUCCESS_WRITE_FILE:
			{
				imprimeln("Text written succesfully in file: " + new String(fileNameWrite));
				break;
			}
			case FileServerOperationManager.ERROR_WRITE_FILE:
			{
				imprimeln("Error while writing in file: " + new String(fileNameWrite) + ".");
				break;
			}
		}
		
		//READ.
		result = library.read(fileNameRead, bufferRead, bytesToRead, startReadingPosition);
		switch(result)
		{
			case FileServerOperationManager.SUCCESS_READ_FILE:
			{
				imprimeln("Text read succesfully in file: " + new String(fileNameWrite));
				imprimeln("Result: " + new String(library.parameters.pop()));
				
				break;
			}
			case FileServerOperationManager.ERROR_READ_FILE:
			{
				imprimeln("Error while reading in file: " + new String(fileNameWrite) + ".");
				break;
			}
		}
		
		//DELETE.
		result = library.delete(fileNameDelete);
		switch(result)
		{
			case FileServerOperationManager.SUCCESS_DELETE_FILE:
			{
				imprimeln("File deleted succesfully: " + new String(fileNameDelete));
				break;
			}
			case FileServerOperationManager.ERROR_DELETE_FILE:
			{
				imprimeln("Error while deleting file: " + new String(fileNameDelete) + ".");
				break;
			}
		}
		
		imprimeln("Fin del cliente.");
	}
	
	public Libreria getLib()
	{
		return library;
	}
}
