//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.util.Escribano;

public class LibreriaServidor extends Libreria{

	/**
	 * 
	 */
	public LibreriaServidor(Escribano esc, byte[] message){
		super(esc, message);
	}

	@Override
	protected void create()
	{
		//Get the parameters.
		String fileName = new String(parameters.pop());
		short fileNameSize = Assembler.bytesToShort(parameters.pop());
		imprimeln("To create file: " + fileName + ", size name: " + fileNameSize);
		
		//Create task.
		try
		{
			File file = new File(fileName);
			file.createNewFile();
			parameters.push(Assembler.intToBytes(FileServerOperationManager.SUCCESS_CREATE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_CREATED_SUCCESS);
		} 
		catch (IOException e)
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_CREATE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_CREATED_FAILURE);
			e.printStackTrace();
		}
	}

	@Override
	protected void read()
	{
		//Get the parameters.
		long startingPosition = Assembler.bytesToLong(parameters.pop());
		long bytesToRead      = Assembler.bytesToLong(parameters.pop());
		String fileName       = new String(parameters.pop());
		short fileNameSize    = Assembler.bytesToShort(parameters.pop());
		
		imprimeln("To read file: " + fileName + ", size name: " + fileNameSize + ".\nRead " + 
				bytesToRead + " characters.\nStarting in position: " + startingPosition);
		
		//Read task.
		File file = new File(fileName);
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.skip(startingPosition);
			char[] buffer = new char[(int) bytesToRead];
			reader.read(buffer, 0, (int) bytesToRead);
			reader.close();
			
			parameters.push(new String(buffer).getBytes());
			parameters.push(Assembler.intToBytes(buffer.length));
			parameters.push(Assembler.intToBytes(FileServerOperationManager.SUCCESS_READ_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_READ_SUCCESS);
		} 
		catch (FileNotFoundException e)
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_READ_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_READ_FAILURE);
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_READ_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_READ_FAILURE);
			e.printStackTrace();
		}
	}

	@Override
	protected void write()
	{
		//Get the parameters.
		long bytesToWrite       = Assembler.bytesToLong(parameters.pop());
		String bufferToWrite    = new String(parameters.pop());
		int bufferToWriteLength = Assembler.bytesToInt(parameters.pop());
		String fileName         = new String(parameters.pop());
		short fileNameSize      = Assembler.bytesToShort(parameters.pop());
		
		imprimeln("To write in file: " + fileName + ", size name: " + fileNameSize + ".\n"
				+ "Write: " + bytesToWrite + " bytes.\nFrom buffer: " + bufferToWrite + " buffer "
						+ "size: " + bufferToWriteLength);
		
		//Write task.
		File file = new File(fileName);
		
		try
		{
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(bufferToWrite, 0, (int) bytesToWrite);
			writer.close();
			
			parameters.push(Assembler.intToBytes(FileServerOperationManager.SUCCESS_WRITE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_WRITE_SUCCESS);
		} 
		catch (IOException e)
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_WRITE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_WRITE_FAILURE);
			e.printStackTrace();
		}
		
	}

	@Override
	protected void delete()
	{
		//Get the parameters.
		String fileName = new String(parameters.pop());
		short fileNameSize = Assembler.bytesToShort(parameters.pop());
		imprimeln("To create file: " + fileName + ", size name: " + fileNameSize);
		
		//Delete task.
		File file = new File(fileName);
		
		if(file.delete())
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.SUCCESS_DELETE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_DELETED_SUCCESS);
		}
		else
		{
			parameters.push(Assembler.intToBytes(FileServerOperationManager.ERROR_DELETE_FILE));
			imprimeln(FileServerOperationManager.UIM_FILE_DELETED_FAILURE);
		}
	}

}