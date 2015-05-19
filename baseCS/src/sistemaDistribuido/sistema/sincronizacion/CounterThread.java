//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import sistemaDistribuido.visual.sincronizacion.ClockFrame;

//This class will manage all the updates in the UI.
public class CounterThread extends Thread
{
	private final static int SECOND = 1000;
	
	private ClockFrame clockFrame;
	private int time;
	private int h;
	private long n;
	private double p;
	private double d;
	private double nextSynchronization;
	
	boolean clockEnabled;
	
	public CounterThread(ClockFrame clockFrame, int h, int p, int d, int time)
	{
		this.clockFrame = clockFrame;
		this.time = 0;
		this.h = h;//interruptions per second.
		this.p = p;//error clock.
		this.d = d;//max distortion.
		this.time = time;//seconds.
		n = 0;
		clockEnabled = true;
	}
	
	@Override
	public void run()
	{
		while(clockEnabled)
		{
			try
			{
				sleep(n += SECOND / h);
				
				//Update the UI.
				clockFrame.getTxtN().setText(String.valueOf(n));
				clockFrame.getTxtNextSincronization().setText(String.valueOf(d/(2*p)));
				
				//Trigger to add a second.
				if((n % 1000) == 0)
				{
					time += 1;
					clockFrame.getTxtTime().setText(time + "Seconds");
				}
				
				//Send the update to the ClockThread.
				clockFrame.getClockProcess().getClockThread().counterThreadReport(this);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	//Getters and Setters.
	public int getTime()
	{
		return time;
	}
	public void setTime(int time)
	{
		this.time = time;
	}
	public int getH()
	{
		return h;
	}
	public void setH(int h)
	{
		this.h = h;
	}
	public long getN()
	{
		return n;
	}
	public void setN(long n)
	{
		this.n = n;
	}
	public double getP()
	{
		return p;
	}
	public void setP(double p)
	{
		this.p = p;
	}
	public double getD()
	{
		return d;
	}
	public void setD(double d)
	{
		this.d = d;
	}
	public double getNextSynchronization()
	{
		return nextSynchronization;
	}
	public void setNextSynchronization(double nextSynchronization)
	{
		this.nextSynchronization = nextSynchronization;
	}
}
