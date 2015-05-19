//Francisco Natanael Ortiz Martínez.
package sistemaDistribuido.sistema.sincronizacion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.MessageReader;

public class ClientClockThread extends ClockThread
{
	//Variables to handle the algorithm.
	private long n;
	private int id;
	
	public ClientClockThread(ClockProcess clockProcess)
	{
		super(clockProcess);
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
			case MessageCreatorClock.SIGNAL_START_CLOCK:
			{
				txtMonitor.append("Start clock signal received.\nSending ip to enter the group.\n");
				
				//Get server's ip.
				short ipSize = MessageReader.readShortFromMessage(message, MessageCreatorClock
						.INDEX_IP_SIZE);
				String ip = MessageReader.readStringFromMessage(message, MessageCreatorClock
						.INDEX_IP, ipSize);
				
				txtMonitor.append("Server's ip: " + ip + "\n");
				
				//Reconfigure the socket to send packets to server.
				try
				{
					txtMonitor.append("Reconfiguring socket.\n");
					DatagramSocket socket = new DatagramSocket(ClockProcess.PORT, 
							InetAddress.getByName(ip));
					clockProcess.setSocket(socket);
					txtMonitor.append("Socket configured correctly.\n");
					
					//Get this machine's ip.
					ip 	   = InetAddress.getLocalHost().getHostName();
					ipSize = (short) ip.length();
					
					//Send the answer to the server.
					byte[] answer = new byte[ClockProcess.MESSAGE_SIZE];
					MessageCreatorClock.setInt(MessageCreatorClock.SIGNAL_ENTER_GROUP, answer,
							MessageCreatorClock.INDEX_SIGNAL);
					MessageCreatorClock.setShort(ipSize, answer, MessageCreatorClock.INDEX_IP_SIZE);
					MessageCreatorClock.setString(ip, answer, MessageCreatorClock.INDEX_IP);
					
					DatagramPacket packet = new DatagramPacket(answer, answer.length);
					
					txtMonitor.append("Sending request to server.\n");
					socket.send(packet);
					txtMonitor.append("Request sent.\n");
				}
				catch (SocketException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error while configuring socket.\n");
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error Unknown Host.\n");
				}
				catch (IOException e)
				{
					e.printStackTrace();
					txtMonitor.append("Error while sending packet.\n");
				}
				
				break;
			}
			
			case MessageCreatorClock.SIGNAL_REAL_START:
			{
				txtMonitor.append("Real start clock signal received. Starting clock.\n");
				id = MessageReader.readIntFromMessage(message, MessageCreatorClock.INDEX_GET_ID);
				CounterThread counterThread = clockProcess.getClockFrame().getCounterThread();
				counterThread.start();
				txtMonitor.append("Clock started.\nId assigned: " + id + "\n");
			}
			
			case MessageCreatorClock.SIGNAL_REPORT_TIME:
			{
				//Get the n from server and send the difference.
				long nFromServer = MessageReader.readLongFromMessage(message, 
						MessageCreatorClock.INDEX_TIME);
				
				byte[] message = new byte[ClockProcess.MESSAGE_SIZE];
				MessageCreatorClock.setInt(MessageCreatorClock.SIGNAL_REPORT_TIME, 
						message, MessageCreatorClock.SIGNAL_REPORT_TIME);
				MessageCreatorClock.setLong(n - nFromServer, message, 
						MessageCreatorClock.INDEX_TIME);
				MessageCreatorClock.setInt(id, message, MessageCreatorClock.INDEX_CLIENT);
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
		n = counterThread.getN();
	}
}
