//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.exclusion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageCreator;
import sistemaDistribuido.sistema.exclusion.ClientProcess;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;


public class ClientFrame extends ProcesoFrame
{
	private static final long serialVersionUID = 1L;
	
	private final static int HEIGHT = 350;
	private final static int WIDTH  = 600;

	private ClientProcess process;
	
	//UI.
	private Choice criticalSection;
	private Label lblSeconds;
	private TextField txtSeconds;
	private Button btnStart;
	private Button btnInterrupt;
	
	public ClientFrame(MicroNucleoFrame frameNucleo)
	{
		super(frameNucleo, "Client");
		add("South", buildPanel());
		validate();
		process = new ClientProcess(this);
		fijarProceso(process);
		setSize(WIDTH, HEIGHT);
	}
	
	public Panel buildPanel()
	{
		Panel panel = new Panel();
		
		lblSeconds      = new Label("Seconds");
		txtSeconds	    = new TextField();
		btnStart 	  	= new Button("Start");
		btnInterrupt    = new Button("Interrupt");
		criticalSection = new Choice();
		criticalSection.add("Critical Section File Server");
		criticalSection.add("Critical Section HTTP Server");
		
		final int textFieldColumns = 8;
		txtSeconds.setColumns(textFieldColumns);
		
		SolicitudeHandler solicitudeHandler = new SolicitudeHandler();
		btnStart.addActionListener(solicitudeHandler);
		btnInterrupt.addActionListener(solicitudeHandler);
		
		panel.add(criticalSection);
		panel.add(lblSeconds);
		panel.add(txtSeconds);
		panel.add(btnStart);
		panel.add(btnInterrupt);
		
		return panel;
	}
	
	class SolicitudeHandler implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String action = e.getActionCommand();
			
			if(action.equals("Start"))
			{
				imprimeln("Start");
				
				if(criticalSection.getSelectedItem().equals("Critical Section File Server"))
				{
					process.setCriticalSectionRequested(MessageCreator
							.CRITICAL_SECTION_FILE_SERVER);
				}
				else if(criticalSection.getSelectedItem().equals("Critical Section HTTP Server"))
				{
					process.setCriticalSectionRequested(MessageCreator
							.CRITICAL_SECTION_HTTP_SERVER);
				}
				process.setSecondsToUseCriticalSection(Integer.parseInt(txtSeconds.getText()));
				Nucleo.reanudarProceso(process);
			}
			else if(action.equals("Interrupt"))
			{
				imprimeln("Interrupt");
				process.setInterrupted(true);
			}
		}
	}
}
