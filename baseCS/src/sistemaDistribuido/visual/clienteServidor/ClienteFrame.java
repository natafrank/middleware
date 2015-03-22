package sistemaDistribuido.visual.clienteServidor;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;
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
	private final static int HEIGHT = 500;
	private final static int WIDTH  = 700;
	
	//Control variables.
	private ProcesoCliente proc;
	private ServerOperationManager serverOperationManager;
	private String[] operations;
	
	//Components.
	private Choice choiceServer;
	private Choice choiceOperation;
	private Choice choiceParameter1, choiceParameter2;
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
	    choiceOperation  = new Choice();
		choiceParameter1 = new Choice();
		choiceParameter2 = new Choice();
		btnSolicitude    = new Button("Solicitar");
		btnSolicitude.addActionListener(new ManejadorSolicitud());
		choiceOperation.setVisible(false);
		choiceParameter1.setVisible(false);
		choiceParameter2.setVisible(false);
		btnSolicitude.setVisible(false);
		
		//Add components to panel.
		p.add(choiceServer);
		p.add(new Label("Operation:"));
		p.add(choiceOperation);
		p.add(new Label("Data:"));
		p.add(choiceParameter1);
		p.add(choiceParameter2);
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
						serverOperationManager = new FileServerOperationManager(
								MessageCreator.ID_FILE_SERVER);
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
			}
		});
		
		//Listener Choice.
		choiceOperation.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				String operation = choiceOperation.getSelectedItem();
				
				if(!operation.equals("Choose an operation"))
				{
					serverOperationManager.setSelectedOperation(operation);
					if(serverOperationManager.hasParametersEnabled())
					{
						//Check parameter1.
						if(serverOperationManager.isParameter1Enabled())
						{
							choiceParameter1.removeAll();
							String[] firstParameters = serverOperationManager.getFirstParameters();
							for(int i = 0; i < firstParameters.length; i++)
							{
								choiceParameter1.add(firstParameters[i]);
							}
							choiceParameter1.setVisible(true);
							setSize(WIDTH + 200, HEIGHT + 100);
							
							//Check parameter2.
							if(serverOperationManager.isParameter2Enabled())
							{
								choiceParameter2.removeAll();
								String[] secondParameters = serverOperationManager
										.getSecondParameters();
								for(int i = 0; i < secondParameters.length; i++)
								{
									choiceParameter2.add(secondParameters[i]);
								}
								choiceParameter2.setVisible(true);
								setSize(WIDTH + 300, HEIGHT + 200);
							}
							else
							{
								choiceParameter2.setVisible(false);
							}
						}
						else
						{
							choiceParameter1.setVisible(false);
							setSize(WIDTH, HEIGHT);
						}
					}
					else
					{
						choiceParameter1.setVisible(false);
						choiceParameter2.setVisible(false);
						setSize(WIDTH, HEIGHT);
					}
					
					btnSolicitude.setVisible(true);
				}
			}
		});
		
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				btnSolicitude.setEnabled(false);
				com=choiceOperation.getSelectedItem();
				imprimeln("Solicitud a enviar: "+com);
				//proc.
				
				//Set the message.
				MessageCreatorClient messageCreatorClient = new MessageCreatorClient(Integer
						.parseInt(dameIdProceso()), serverOperationManager.getIdServer(), 
						(short)choiceOperation.getSelectedIndex(), choiceParameter1.getSelectedItem());
				
				//Trigger to check if has extra, and send it if it has.
				if(choiceParameter2.isVisible())
				{
					messageCreatorClient.setExtraData(choiceParameter2.getSelectedItem());
				}
				
				//Create and check Mesasge.
				int messageCreationResult = messageCreatorClient.createMessage();
				imprimeln("MI Mensaje a enviar: " + new String(messageCreatorClient.getMessage()));
				if(messageCreationResult >= 0)
				{
					//Message created successfully.
					//Set the message to the process ready to send.
					proc.setMessage(messageCreatorClient.getMessage());
					proc.setMessageCreatorClient(messageCreatorClient);
					
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
					}
				}
			}
		}
	}
}
