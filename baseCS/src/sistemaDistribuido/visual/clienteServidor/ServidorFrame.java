package sistemaDistribuido.visual.clienteServidor;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ProcesoServidor;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ServidorFrame extends ProcesoFrame{
  private static final long serialVersionUID=1;
  private ProcesoServidor proc;

  public ServidorFrame(MicroNucleoFrame frameNucleo, String serverName){
    super(frameNucleo, serverName);
    proc=new ProcesoServidor(this, serverName);
    fijarProceso(proc);
  }
}