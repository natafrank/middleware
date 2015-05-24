//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.exclusion;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;

public class MutualExclusionFrame extends MicroNucleoFrame
{
	private static final long serialVersionUID = 1L;
	
	private final static int HEIGHT = 400;
	private final static int WIDTH  = 800;
	
	private PanelMutualExclusion panel;
	
	public MutualExclusionFrame()
	{
		setTitle("Coordinator");
		add("South", construirPanelSur());
		setSize(WIDTH, HEIGHT);
	}
	
	@Override
	protected Panel construirPanelSur()
	{
		panel = new PanelMutualExclusion();
		panel.agregarActionListener(new ManejadorBotones());
		return panel;
	}

	class ManejadorBotones implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String com=e.getActionCommand();
			if (com.equals("Client"))
			{
				levantarProcesoFrame(new ClientFrame(MutualExclusionFrame.this));
			}
			if(com.equals("Coordinator"))
			{
				levantarProcesoFrame(new CoordinatorFrame(MutualExclusionFrame.this));
			}
		}
	}

	public static void main(String args[])
	{
		MutualExclusionFrame frame = new MutualExclusionFrame();
		frame.setVisible(true);
		frame.imprimeln("Ventana del micronucleo iniciada.");
		Nucleo.iniciarSistema(frame,2001,2002,frame);
	}
}
