//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.sincronizacion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import sistemaDistribuido.sistema.sincronizacion.ClockProcess;
import sistemaDistribuido.sistema.sincronizacion.CounterThread;

public class ClockFrame extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	//MODIFICAR TAMAÑO Y COLOCAR NOPMBRE A TODOS LOS ARCHIVOS.
	private final static int HEIGHT = 290;
	private final static int WIDTH  = 520;
	private final static int TEXT_FIELD_COLUMNS = 5;
	
	//Components of the UI.
	private Panel panel;
	private TextArea txtMonitor;
	private TextField txtTime;
	private TextField txtH;
	private TextField txtN;
	private TextField txtP;
	private TextField txtD;
	private TextField txtNextSynchronization;
	private Choice choiceServerClient;
	private Button btnPrepare;
	private Button btnStart;
	
	private ClockProcess process = null;
	private CounterThread counterThread;
	
	//Constructor.
	public ClockFrame()
	{
		super("Clock Synchronization");
		setSize(WIDTH, HEIGHT);
		add(createPanel());
	}
	
	//Panel creation.
	public Panel createPanel()
	{
		//Create the components.
		panel			       = new Panel();
		txtMonitor 			   = new TextArea();
		txtTime 			   = new TextField();
		txtH 				   = new TextField();
		txtN 				   = new TextField();
		txtP 				   = new TextField();
		txtD				   = new TextField();
		txtNextSynchronization = new TextField();
		choiceServerClient	   = new Choice();
		btnPrepare			   = new Button("Prepare");
		btnStart			   = new Button("Start");
		
		//Set width to text fields.
		txtTime.setColumns(TEXT_FIELD_COLUMNS);
		txtH.setColumns(TEXT_FIELD_COLUMNS);
		txtN.setColumns(TEXT_FIELD_COLUMNS);
		txtP.setColumns(TEXT_FIELD_COLUMNS);
		txtD.setColumns(TEXT_FIELD_COLUMNS);
		txtNextSynchronization.setColumns(TEXT_FIELD_COLUMNS);
		
		//Add the elements to the choice.
		choiceServerClient.add("Server");
		choiceServerClient.add("Client");
		
		btnStart.setEnabled(false);
		
		//Add the components to the panel.
		panel.add(txtMonitor);
		panel.add(new Label("Time"));
		panel.add(txtTime);
		panel.add(new Label("H"));
		panel.add(txtH);
		panel.add(new Label("N"));
		panel.add(txtN);
		panel.add(new Label("P"));
		panel.add(txtP);
		panel.add(new Label("D"));
		panel.add(txtD);
		panel.add(new Label("Next Sincronization"));
		panel.add(txtNextSynchronization);
		panel.add(new Label("Server/Client: "));
		panel.add(choiceServerClient);
		panel.add(btnPrepare);
		panel.add(btnStart);
		
		//Add the lister to the buttons.
		btnPrepare.addActionListener(this);
		btnStart.addActionListener(this);
		
		return panel;
	}
	
	//Handle actions over the UI.
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		
		if(action.equals("Prepare"))
		{
			txtMonitor.append("Preparing process...\n");
			
			//Prepare the process to receive messages.
			if(choiceServerClient.getSelectedItem().equals("Server"))
			{
				process = new ClockProcess(this, ClockProcess.SERVER_CLOCK);
				btnPrepare.setEnabled(false);
				btnStart.setEnabled(true);
				txtP.setText("0");
				txtP.setEnabled(false);
			}
			else if(choiceServerClient.getSelectedItem().equals("Client"))
			{
				process = new ClockProcess(this, ClockProcess.CLIENT_CLOCK);
				txtMonitor.append("Only a server process can start the clocks.\n");
				
				//disables the components that a client can't handle.
				//txtD.setEnabled(false);
			}
			
			if(process != null)
			{
				txtMonitor.append("Process prepared.\n");
				
				//Create the respective thread to handle the UI.
				counterThread = new CounterThread(this, Integer.parseInt(txtH.getText()), 
						Integer.parseInt(txtP.getText()),
						Integer.parseInt(txtD.getText()), Integer.parseInt(txtTime.getText()));
			}
			else
			{
				txtMonitor.append("Process prepared.\n");
			}
		}
		else if(action.equals("Start"))
		{
			//Start the clocks.
			if(process != null)
			{
				process.startClocks();
			}
			else
			{
				txtMonitor.append("Can't start clocks non server prepared.\n");
			}
		}
	}
	
	//Getters.
	public TextArea getTxtMonitor()
	{
		return txtMonitor;
	}

	public TextField getTxtTime()
	{
		return txtTime;
	}

	public TextField getTxtH()
	{
		return txtH;
	}

	public TextField getTxtN()
	{
		return txtN;
	}

	public TextField getTxtP()
	{
		return txtP;
	}

	public TextField getTxtD()
	{
		return txtD;
	}

	public TextField getTxtNextSincronization()
	{
		return txtNextSynchronization;
	}
	
	public CounterThread getCounterThread()
	{
		return counterThread;
	}
	
	public ClockProcess getClockProcess()
	{
		return process;
	}

	/*-------------------------- WINDOW LISTENER INTERFACE METHODS ----------------------------*/
	@Override
	public void windowClosing(WindowEvent e){}

	@Override
	public void windowOpened(WindowEvent e){}
	
	@Override
	public void windowClosed(WindowEvent e)
	{	
		//Close the clock and exit from the group.
		dispose();
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e){}

	@Override
	public void windowDeiconified(WindowEvent e){}

	@Override
	public void windowActivated(WindowEvent e){}

	@Override
	public void windowDeactivated(WindowEvent e){}

	/*-------------------------- WINDOW LISTENER INTERFACE METHODS ----------------------------*/
	
	public static void main(String args[])
	{
		ClockFrame frame = new ClockFrame();
		frame.setVisible(true);
	}
}
