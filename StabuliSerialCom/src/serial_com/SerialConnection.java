package serial_com;

import java.io.*;

import gnu.io.*;

import java.util.*;

public class SerialConnection implements SerialPortEventListener
{
	protected SerialPort serialPort;
	
	protected static final String PORT_NAME = "COM3";//the port you want to use
	
	protected DataInputStream input; // gets the InputStream
	
	protected DataOutputStream output; //the output stream to the port
	
	protected static final int TIME_OUT = 2000;
	protected static final int DATA_RATE = 9600; //Baud rate, speed of the bits
	protected boolean connected;
	
	public void initialize()
	{
		try
		{
			CommPortIdentifier portID = CommPortIdentifier.getPortIdentifier(PORT_NAME);//The serial port
			serialPort = (SerialPort)portID.open("my_java_serial" + PORT_NAME,  TIME_OUT);
			System.out.println("Seral port found and opened");
			
			try // configure the port
			{
				serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				System.out.println("Serial port parameters are set: " + DATA_RATE);
			}
			catch(UnsupportedCommOperationException e)
			{
				System.out.println("Probably an unsupoorted Speed");
			}
			
			try //establish stream for reading from the port
			{
				input = new DataInputStream(serialPort.getInputStream());
				//input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				output = new DataOutputStream(serialPort.getOutputStream());
			}
			catch (IOException e)
			{
				System.out.println("couldn't get streams");
			}
			
			try
			{
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
			}
			catch (TooManyListenersException e)
			{
				System.out.println("couldn't add listener");
			}
			connected = true;
		}
		catch (Exception e)
		{
			System.out.println("Port in use: " + e);
		}
	}
	
	public void close()
	{
		if(serialPort != null) 
		{
			serialPort.removeEventListener();
			serialPort.close();
			connected = false;
		}
	}
	
	public boolean getConnected()
	{
		return connected;
	}

	protected String data_input = "";
	private char datum;					//datum is singular for data
	
	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
		if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				int byte_in = input.read();		//reads from the port one byte at a time
				if(byte_in > 0)					//if byte_in < 1, read error
					datum = (char)byte_in;		//here information from the robot can be parsed
				else
					datum = 126;				//sets datum to '~', arbitrary choice. 
			    data_input += datum;
			    if(' ' <= datum && datum <= '~' || datum == '\n')	//ignores all characters other than the ones include here
			    	System.out.print(datum);
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
			}
		}
	}
	
	
	public void write(String str) //sends str through the serial connection
	{
		try
		{
			for(int i = 0; i<str.length(); i++)
			{
				output.write((byte)str.charAt(i));
			}
			output.write((byte)(13));			//important return character, won't work without
		} catch (Exception IOException) 
		{
			System.out.println("Error");	//super informative error
		}
	}
	
	public void thread(int Time)	//used to wait(Time ms);
	{
		int time = Time/100;
		
		Thread t=new Thread() {
			public void run() {
				try {Thread.sleep(100);} catch (InterruptedException ie) {}
			}
		};
		for(int i = 0; i<time; i++)
			t.run();
	}
}
