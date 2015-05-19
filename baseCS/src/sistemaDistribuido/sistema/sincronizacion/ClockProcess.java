//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import java.awt.TextArea;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import sistemaDistribuido.visual.sincronizacion.ClockFrame;

public class ClockProcess extends Thread
{
	public final static int SERVER_CLOCK = 1;
	public final static int CLIENT_CLOCK = 2;
	
	public final static int MESSAGE_SIZE = 64;
	
	//Communication variables.
	private DatagramSocket socket;
	private DatagramPacket receivingPacket;
	
	public final static int PORT = 1234;
	private byte[] message;
	
	private ClockFrame frame;
	private ClockThread clockThread;
	
	private boolean waitingForMessage;
	
	private TextArea txtMonitor;
	
	//Constructor for the process.
	public ClockProcess(ClockFrame frame, int clockOption)
	{
		this.frame         = frame;
		waitingForMessage  = true;
		txtMonitor         = frame.getTxtMonitor();
		
		//Initialize the communication variables.
		switch(clockOption)
		{
			case SERVER_CLOCK:
			{
				//Get the broadcast to get all the machines conneceted.
				InetAddress broadcast = getBroadcast();
				txtMonitor.append("Broadcast ip: " + broadcast + "\n");
				
				//Initialize the socket.
				try
				{
					socket = new DatagramSocket(PORT, broadcast);
					txtMonitor.append("Socket initialized.\n");
					message = new byte[MESSAGE_SIZE];
					clockThread = new ServerClockThread(this);
				}
				catch (SocketException e)
				{
					e.printStackTrace();
					txtMonitor.append("Couldn't initialize the socket./n");
				}
				
				break;
			}
			case CLIENT_CLOCK:
			{
				//Initialize the socket.
				try
				{
					socket = new DatagramSocket(PORT);
					txtMonitor.append("Socket initialized.\n");
					message = new byte[MESSAGE_SIZE];
					receivingPacket = new DatagramPacket(message, message.length);
					txtMonitor.append("Packet initialized.\nClient ready to receive signal"
							+ " to start.\n");
					clockThread = new ClientClockThread(this);
					
				}
				catch (SocketException e)
				{
					e.printStackTrace();
					txtMonitor.append("Couldn't initialize the socket./n");
				}
				
				break;
			}
			default:
			{
				txtMonitor.append("Error invalid option.\n");
			}
		}
	}
	
	//Handler to receive messages.
	@Override
	public void run()
	{
		while(isWaitingForMessage())
		{
			try
			{
				socket.receive(receivingPacket);
				
				//Handle the task.
				clockThread.resolveMessage(receivingPacket.getData());
			}
			catch (IOException e)
			{
				e.printStackTrace();
				frame.getTxtMonitor().append("Error while receiving the message./n");
			}
		}
	}
	
	//Start all the clocks in the group.
	//Only the server can access this method.
	public void startClocks()
	{
		//Prepare the server to receive the clients' answer.
		start();
		txtMonitor.append("Server is ready to receive answer from clients.");
		
		try
		{
			//Get the ip.
			String ip = InetAddress.getLocalHost().getHostName();
			
			//Create the message.
			MessageCreatorClock.setInt(MessageCreatorClock.SIGNAL_START_CLOCK, message, 
					MessageCreatorClock.INDEX_SIGNAL);
			MessageCreatorClock.setShort((short) ip.length(), message, MessageCreatorClock
					.INDEX_IP_SIZE);
			MessageCreatorClock.setString(ip, message, MessageCreatorClock.INDEX_IP);
			
			//Send the packet.
			DatagramPacket packet = new DatagramPacket(message, message.length);
			txtMonitor.append("Packet initialized.\nServer ready to start clocks.\n");
			socket.send(packet);
			txtMonitor.append("Packet sent.");
		}
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			txtMonitor.append("Error while sending packet.");
		}	
	}
	
	public boolean isWaitingForMessage()
	{
		return waitingForMessage;
	}

	//Method to get the broadcast.
	public static InetAddress getBroadcast()
	{	
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			while(interfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = interfaces.nextElement();
				
				//If is a loopback, then get next element.
				if(networkInterface.isLoopback())
				{
					continue;
				}
				
				List<InterfaceAddress> interfaceAdresses = networkInterface.getInterfaceAddresses();
				
				for(int i = 0; i < interfaceAdresses.size(); i++)
				{
					InetAddress broadcast = interfaceAdresses.get(i).getBroadcast();
					
					//Trigger to return the broadcast.
					if(broadcast != null)
					{
						return broadcast;
					}
				}
			}
		} 
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Getters.
	public ClockFrame getClockFrame()
	{
		return frame;
	}
	
	public DatagramSocket getSocket()
	{
		return socket;
	}
	
	public ClockThread getClockThread()
	{
		return clockThread;
	}
	
	//Setters.
	public void setSocket(DatagramSocket socket)
	{
		this.socket = socket;
	}
}
