//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.clienteServidor;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Panel;
import java.awt.event.ActionListener;

public class PanelClienteServidor extends Panel{
  private static final long serialVersionUID=1;
  private Button botonCliente,botonServidor;
  private Choice choiceServer;
  private final String[] servers = {"Choose a server" ,"File Server"};

  public PanelClienteServidor(){
     botonCliente=new Button("Cliente");
     botonServidor=new Button("Iniciar Servidor");
     choiceServer = new Choice();
     
     //Add the servers to choice.
     for(int i = 0; i < servers.length; i++)
     {
    	 choiceServer.add(servers[i]);
     }
     
     add(botonCliente);
     add(botonServidor);
     add(choiceServer);
  }
  
  public Button dameBotonCliente(){
    return botonCliente;
  }
  
  public Button dameBotonServidor(){
    return botonServidor;
  }
  
  public void agregarActionListener(ActionListener al){
    botonCliente.addActionListener(al);
    botonServidor.addActionListener(al);
  }
  
  public Choice getChoiceServe()
  {
	  return choiceServer;
  }
}