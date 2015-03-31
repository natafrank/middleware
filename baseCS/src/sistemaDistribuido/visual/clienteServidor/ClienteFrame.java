package sistemaDistribuido.visual.clienteServidor;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.FileServerOperationManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreatorClient;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ProcesoCliente;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServerOperationManager;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private final static int HEIGHT = 400;
	private final static int WIDTH  = 500;
	
	//Control variables.
	private ProcesoCliente proc;
	private FileServerOperationManager serverOperationManager;
	private String[] operations;
	
	//Components.
	private Choice choiceServer;
	private Choice choiceOperation;
	private Choice fileName;
	private Label lblWriter;
	private Label lblOffset;
	private Label lblLength;
	private TextField txtWriter;
	private TextField txtOffset;
	private TextField txtLength;
	private Button btnSolicitude;
	
	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		choiceServer = new Choice();
		//Add the servers to choice.
	    for(int i = 0; i < ServerOperationManager.SERVERS.length; i++)
	    {
	   	 	choiceServer.add(ServerOperationManager.SERVERS[i]);
	    }
	     
	    //Create components.
	    choiceOperation = new Choice();
		fileName        = new Choice();
		txtWriter       = new TextField();
		txtOffset       = new TextField();
		txtLength       = new TextField();
		lblWriter		= new Label("Text:");
		lblOffset		= new Label("Offset:");
		lblLength		= new Label("Length:");
		btnSolicitude   = new Button("Make Request");
		btnSolicitude.addActionListener(new ManejadorSolicitud());
		choiceOperation.setVisible(false);
		fileName.setVisible(false);
		lblLength.setVisible(false);
		lblOffset.setVisible(false);
		lblWriter.setVisible(false);
		txtWriter.setVisible(false);
		txtWriter.setColumns(40);
		txtOffset.setVisible(false);
		txtLength.setVisible(false);
		btnSolicitude.setVisible(false);
		
		//Add components to panel.
		p.add(choiceServer);
		p.add(choiceOperation);
		p.add(fileName);
		p.add(lblOffset);
		p.add(txtOffset);
		p.add(lblLength);
		p.add(txtLength);
		p.add(lblWriter);
		p.add(txtWriter);
		p.add(btnSolicitude);
		
		//Listener after select a server.
		choiceServer.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				String selectedServer = choiceServer.getSelectedItem();
				
				if(!selectedServer.equals("Choose a server"))
				{
					if(selectedServer.equals("File Server"))
					{
						serverOperationManager = new FileServerOperationManager();
					}
					
					//Clean and load the operations available for the server.
					choiceOperation.removeAll();
					operations = serverOperationManager.getOperations();
					for(int i =0; i < operations.length; i++)
					{
						choiceOperation.add(operations[i]);
					}
					//Set the choice operation visible and remove the option "Choose a server"
					//from the choice server.
					choiceOperation.setVisible(true);
					setSize(WIDTH, HEIGHT);
				}
				else
				{
					choiceOperation.setVisible(false);
					fileName.setVisible(false);
					lblLength.setVisible(false);
					lblOffset.setVisible(false);
					lblWriter.setVisible(false);
					txtWriter.setVisible(false);
					txtOffset.setVisible(false);
					txtLength.setVisible(false);
					btnSolicitude.setVisible(false);
				}
			}
		});
		
		//Listener Choice.
		choiceOperation.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				int operation = choiceOperation.getSelectedIndex();
				
				if(operation != 0)
				{
					//Clean and load the files.
					fileName.removeAll();
					String[] fileNames = serverOperationManager.getFileNames();
					for(int i =0; i < fileNames.length; i++)
					{
						fileName.add(fileNames[i]);
					}
					switch(operation)
					{
						case FileServerOperationManager.CREATE:
						{
							fileName.setVisible(true);
							lblLength.setVisible(false);
							lblOffset.setVisible(false);
							lblWriter.setVisible(false);
							txtWriter.setVisible(false);
							txtOffset.setVisible(false);
							txtLength.setVisible(false);
							btnSolicitude.setVisible(true);
							setSize(WIDTH + 20, HEIGHT);
							break;
						}
						case FileServerOperationManager.DELETE:
						{
							fileName.setVisible(true);
							lblLength.setVisible(false);
							lblOffset.setVisible(false);
							lblWriter.setVisible(false);
							txtWriter.setVisible(false);
							txtOffset.setVisible(false);
							txtLength.setVisible(false);
							btnSolicitude.setVisible(true);
							setSize(WIDTH + 20, HEIGHT);
							break;
						}
						case FileServerOperationManager.READ:
						{
							fileName.setVisible(true);
							lblLength.setVisible(true);
							lblOffset.setVisible(true);
							lblWriter.setVisible(false);
							txtWriter.setVisible(false);
							txtOffset.setVisible(true);
							txtLength.setVisible(true);
							btnSolicitude.setVisible(true);
							setSize(WIDTH + 150, HEIGHT);
							break;
						}
						case FileServerOperationManager.WRITE:
						{
							fileName.setVisible(true);
							lblLength.setVisible(false);
							lblOffset.setVisible(false);
							lblWriter.setVisible(true);
							txtWriter.setVisible(true);
							txtOffset.setVisible(false);
							txtLength.setVisible(false);
							btnSolicitude.setVisible(true);
							setSize(WIDTH + 350, HEIGHT);
							break;
						}
					}
				}
				else
				{
					fileName.setVisible(false);
					lblLength.setVisible(false);
					lblOffset.setVisible(false);
					lblWriter.setVisible(false);
					txtWriter.setVisible(false);
					txtOffset.setVisible(false);
					txtLength.setVisible(false);
					btnSolicitude.setVisible(false);
				}
			}
		});
		
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Make Request")){
				btnSolicitude.setEnabled(false);
				com=choiceOperation.getSelectedItem();
				imprimeln("Solicitud a enviar: "+com);
				
				MessageCreatorClient messageCreatorClient = null;
				int operationCode = choiceOperation.getSelectedIndex();
				switch(operationCode)
				{
					case FileServerOperationManager.CREATE:
					{
						messageCreatorClient = new MessageCreatorClient(
								(short)FileServerOperationManager.CREATE, 
								fileName.getSelectedItem());
						break;
					}
					case FileServerOperationManager.DELETE:
					{
						messageCreatorClient = new MessageCreatorClient(
								(short)FileServerOperationManager.DELETE, 
								fileName.getSelectedItem());
						break;
					}
					case FileServerOperationManager.READ:
					{
						messageCreatorClient = new MessageCreatorClient(
								(short)FileServerOperationManager.READ, 
								fileName.getSelectedItem(), Integer.parseInt(txtOffset.getText()),
								Integer.parseInt(txtLength.getText()));
						break;
					}
					case FileServerOperationManager.WRITE:
					{
						messageCreatorClient = new MessageCreatorClient(
								(short)FileServerOperationManager.WRITE, 
								fileName.getSelectedItem(), txtWriter.getText());
						break;
					}
				}
				
				//Create and check Mesasge.
				int messageCreationResult = messageCreatorClient.createMessage();
				if(messageCreationResult >= 0)
				{
					//Message created successfully.
					//Set the message to the process ready to send.
					proc.setMessage(messageCreatorClient.getMessage());
					
					//Resume the process.
					Nucleo.reanudarProceso(proc);
					btnSolicitude.setEnabled(true);
				}
				else
				{
					//Error while creating message.
					switch(messageCreationResult)
					{
						case MessageCreator.ERROR_MESSAGE_TOO_LONG:
						{
							imprimeln(MessageCreator.UIM_ERROR_MESSAGE_TOO_LONG);
							break;
						}
						case MessageCreator.ERROR_MESSAGE_CREATION:
						{
							imprimeln(MessageCreator.UIM_ERROR_MESSAGE_CREATION);
							break;
						}
					}
				}
			}
		}
	}
}
