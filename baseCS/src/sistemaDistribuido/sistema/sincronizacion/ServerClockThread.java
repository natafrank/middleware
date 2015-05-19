//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;

public class ServerClockThread extends ClockThread
{
	//Sockets for each client.
	private ArrayList<DatagramSocket> sockets;
	
	//Number of computers to sync.
	private final static int COMPUTERS_TO_SYNC = 2;
	
	//Error margin to start the synchronization algorithm.
	private final static int MARGIN_START_SYNC = 1000;
	
	//Counter to create id's for the clients.
	private static int idCounter = 1;
	
	//Variables to control the Berkeley's algorithm.
	private long n;
	private double d;
	private double p;
	private long nFromClient1;
	private long nFromClient2;
	private double nextSync;
	
	public ServerClockThread(ClockProcess clockProcess)
	{
		super(clockProcess);
		sockets = new ArrayList<DatagramSocket>();
	}

	@Override
	public void resolveMessage(byte [] message)
	{
		this. message = message;
		start();
	}

	@Override
	public void run()
	{
		//Open the message received and do the respective task.
		int signal = MessageReader.readIntFromMessage(message, MessageCreatorClock.INDEX_SIGNAL);
		
		switch(signal)
		{
			case MessageCreatorClock.SIGNAL_ENTER_GROUP:
			{
				short ipSize = MessageReader.readShortFromMessage(message, MessageCreatorClock
						.INDEX_IP_SIZE);
				String ip = MessageReader.readStringFromMessage(message, MessageCreatorClock
						.INDEX_IP, ipSize);
				try
				{
					txtMonitor.append("Adding client socket...\n");
					sockets.add(new DatagramSocket(ClockProcess.PORT, InetAddress.getByName(ip)));
					txtMonitor.append("Socket added.\n");
					
					//If the number of computers to sync has been reached, send the signal to start
					//the clocks.
					if(sockets.size() >= COMPUTERS_TO_SYNC)
					{
						txtMonitor.append("Group completed. Sending messages to start.\n");
						byte[] newMessage = new byte[ClockProcess.MESSAGE_SIZE];
						MessageCreatorClock.setInt(MessageCreatorClock.SIGNAL_REAL_START, newMessage
								, MessageCreatorClock.INDEX_SIGNAL);
						MessageCreatorClock.setInt(idCounter, newMessage, 
								MessageCreatorClock.INDEX_GET_ID);
						DatagramPacket newPacket = new DatagramPacket(newMessage, newMessage.length);
						
						//Send the packet to each client.
						int socketsSize = sockets.size();
						for(int i = 0; i < socketsSize; i++)
						{
							txtMonitor.append("Sending packet...\n");
							sockets.get(i).send(newPacket);
							txtMonitor.append("Packet sent.\n");
							idCounter++;
						}
						
						//Start the clock.
						clockProcess.getClockFrame().getCounterThread().start();
						txtMonitor.append("Clock started\n");
					}
					else
					{
						int computersToWait = COMPUTERS_TO_SYNC - sockets.size();
						txtMonitor.append("Waiting for " + computersToWait + " more computers.\n");
					}
				}
				catch (SocketException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error while adding socket.\n");
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error while adding socket.\n");
				}
				catch (IOException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error while sending packet.\n");
				}
				
				break;
			}
			
			case MessageCreatorClock.SIGNAL_REPORT_TIME:
			{
				int id = MessageReader.readIntFromMessage(message, MessageCreatorClock
						.INDEX_GET_ID);
				long auxiliarN = MessageReader.readLongFromMessage(message, 
						MessageCreatorClock.INDEX_TIME);
				
				//Assign to the corresponding n.
				if(id == 1)
				{
					nFromClient1 = auxiliarN;
				}
				else if(id == 2)
				{
					nFromClient2 = auxiliarN;
				}
				
				if(nFromClient1 > nFromClient2)
				{
					if((nFromClient1 - nFromClient2) >= (nextSync - MARGIN_START_SYNC))
					{
						//Start the sync algorithm.
					}
				}
				else
				{
					if((nFromClient2 - nFromClient1) >= (nextSync - MARGIN_START_SYNC))
					{
						//Start the sync algorithm.
					}
				}
			}
			
			default:
			{
				txtMonitor.append("Wrong signal. There's nothing to do.\n");
			}
		}
	}

	@Override
	public void counterThreadReport(CounterThread counterThread)
	{
		//Send n to the clients.
		n = counterThread.getN();
		d = counterThread.getD();
		p = counterThread.getP();
		
		System.out.println("N: " + n + "\nP: " + p + "\nD: " + d);
		
		nextSync = counterThread.getNextSynchronization();
		
		byte[] message = new byte[ClockProcess.MESSAGE_SIZE];
		
		MessageCreatorClock.setInt(MessageCreatorClock.SIGNAL_REPORT_TIME, message, 
				MessageCreatorClock.INDEX_SIGNAL);
		MessageCreatorClock.setLong(n, message, MessageCreatorClock.INDEX_TIME);
		DatagramPacket packet = new DatagramPacket(message, message.length);
		
		try
		{
			int socketsSize = sockets.size();
			for(int i = 0; i < socketsSize; i++)
			{
				txtMonitor.append("Sending N to clients...\n");
				sockets.get(i).send(packet);
				txtMonitor.append("Packet sent.\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			txtMonitor.append("Error while sending packet.\n");
		}
	}
}
