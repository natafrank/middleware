//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.exclusion;

import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;

public class PanelMutualExclusion extends Panel
{
	private static final long serialVersionUID = 1L;

	private Button btnClient;
	private Button btnCoordinator;
	
	public PanelMutualExclusion()
	{
		btnClient 	   = new Button("Client");
		btnCoordinator = new Button("Coordinator");
		add(btnClient);
		add(btnCoordinator);
	}
	
 	public void agregarActionListener(ActionListener al)
 	{
	    btnClient.addActionListener(al);
	    btnCoordinator.addActionListener(al);
 	}
	
	public Button getBtnClient()
	{
		return btnClient;
	}
	
	public Button getBtnCoordinator()
	{
		return btnCoordinator;
	}
}
