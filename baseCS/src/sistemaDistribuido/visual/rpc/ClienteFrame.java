package sistemaDistribuido.visual.rpc;

import java.awt.Button;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Assembler;
import sistemaDistribuido.sistema.rpc.modoUsuario.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private ProcesoCliente proc;

	//Choices for file names.
	private Choice fileNameCreate;
	private Choice fileNameRead;
	private Choice fileNameWrite;
	private Choice fileNameDelete;
	
	//Choices for read operation.
	private Choice bytesToRead;
	private Choice startReadingPosition;
	
	//Choices for write operation.
	private Choice bufferToWrite;
	private Choice bytesToWrite;

	private Button botonSolicitud;

	public ClienteFrame(RPCFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
		this.setSize(1000, 400);
	}

	public Panel construirPanelSolicitud(){
		Panel pSolicitud,pcodop1,pcodop2,pcodop3,pcodop4,pboton;
		pSolicitud=new Panel();
		pcodop1=new Panel();
		pcodop2=new Panel();
		pcodop3=new Panel();
		pcodop4=new Panel();
		pboton=new Panel();
		fileNameCreate       = new Choice();
		fileNameRead         = new Choice();
		fileNameWrite        = new Choice();
		fileNameDelete       = new Choice();
		bytesToRead  	     = new Choice();
		bytesToWrite  		 = new Choice();
		bufferToWrite  		 = new Choice();
		startReadingPosition = new Choice();
		
		//Initialize the choices. (Generic and hardcoded options just to test)
		//CREATE, READ, WRITE, DELETE.
		String file1 = "hello.txt", file2 = "another.txt";
		fileNameCreate.add(file1);
		fileNameCreate.add(file2);
		fileNameRead.add(file1);
		fileNameRead.add(file2);
		fileNameWrite.add(file1);
		fileNameWrite.add(file2);
		fileNameDelete.add(file1);
		fileNameDelete.add(file2);
		
		//READ
		bytesToRead.add("5");
		bytesToRead.add("10");
		startReadingPosition.add("0");
		startReadingPosition.add("5");
		
		//WRITE
		bytesToWrite.add("10");
		bytesToWrite.add("20");
		bufferToWrite.add("This is a simple text which main porpouse is to have more than 20"
				+ " characters.");
		bufferToWrite.add("This is pretended to be shorter than the other.");
		
		pSolicitud.setLayout(new GridLayout(5,1));

		pcodop1.add(new Label("CREATE >> "));
		pcodop1.add(new Label("File Name:"));
		pcodop1.add(fileNameCreate);

		pcodop3.add(new Label("READ >> "));
		pcodop3.add(new Label("File Name:"));
		pcodop3.add(fileNameRead);
		pcodop3.add(new Label("Bytes to Read:"));
		pcodop3.add(bytesToRead);
		pcodop3.add(new Label("Start Reading Position:"));
		pcodop3.add(startReadingPosition);

		pcodop2.add(new Label("WRITE >> "));
		pcodop2.add(new Label("File Name:"));
		pcodop2.add(fileNameWrite);
		pcodop2.add(new Label("Buffer to Write:"));
		pcodop2.add(bufferToWrite);
		pcodop2.add(new Label("Bytes to Write:"));
		pcodop2.add(bytesToWrite);

		pcodop4.add(new Label("DELETE >> "));
		pcodop4.add(new Label("File Name:"));
		pcodop4.add(fileNameDelete);

		botonSolicitud=new Button("Solicitar");
		pboton.add(botonSolicitud);
		botonSolicitud.addActionListener(new ManejadorSolicitud());

		pSolicitud.add(pcodop1);
		pSolicitud.add(pcodop2);
		pSolicitud.add(pcodop3);
		pSolicitud.add(pcodop4);
		pSolicitud.add(pboton);

		return pSolicitud;
	}

	class ManejadorSolicitud implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String com=e.getActionCommand();
			if (com.equals("Solicitar"))
			{
				botonSolicitud.setEnabled(false);
				
				//Add the parameters to the stack.
				Stack<byte[]> parameters = proc.getLib().getParameters();
				
				//CREATE.
				parameters.push(fileNameCreate.getSelectedItem().getBytes());
				
				//WRITE.
				parameters.push(fileNameWrite.getSelectedItem().getBytes());
				parameters.push(bufferToWrite.getSelectedItem().getBytes());
				parameters.push(Assembler.longToBytes(Long.valueOf(bytesToWrite.getSelectedItem())));
				
				//READ.
				parameters.push(fileNameRead.getSelectedItem().getBytes());
				long bytesToReadSelection = Long.valueOf(bytesToRead.getSelectedItem());
				char[] bufferRead = new char[(int) bytesToReadSelection];
				parameters.push(new String(bufferRead).getBytes());
				parameters.push(Assembler.longToBytes(bytesToReadSelection));
				parameters.push(Assembler.longToBytes
						(Long.valueOf(startReadingPosition.getSelectedItem())));
				
				//DELETE.
				parameters.push(fileNameDelete.getSelectedItem().getBytes());
				
				Nucleo.reanudarProceso(proc);
			}
		}
	}
}
