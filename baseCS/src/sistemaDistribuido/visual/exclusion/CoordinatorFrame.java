//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.visual.exclusion;

import sistemaDistribuido.sistema.exclusion.CoordinatorProcess;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class CoordinatorFrame extends ProcesoFrame
{
	private static final long serialVersionUID = 1L;
	private CoordinatorProcess process;

	public CoordinatorFrame(MicroNucleoFrame frameNucleo)
	{
		super(frameNucleo, "Coordinator");
		process = new CoordinatorProcess(this);
		fijarProceso(process);
	}
}
