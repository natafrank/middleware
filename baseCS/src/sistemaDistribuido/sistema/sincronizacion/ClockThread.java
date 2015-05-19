//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import java.awt.TextArea;

public abstract class ClockThread extends Thread
{	
	protected ClockProcess clockProcess;
	protected byte[] message;
	protected TextArea txtMonitor;
	
	public ClockThread(ClockProcess clockProcess)
	{
		this.clockProcess = clockProcess;
		txtMonitor = clockProcess.getClockFrame().getTxtMonitor();
	}
	
	public abstract void resolveMessage(byte [] message);
	
	public abstract void counterThreadReport(CounterThread counterThread);
}
