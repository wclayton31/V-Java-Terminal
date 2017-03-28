package serial_com;

import java.util.Scanner;

public class ConnectionTerminal 
{
	public static void main(String args[])
	{
		SerialConnection serial = new SerialConnection();
		Scanner scan = new Scanner(System.in);
		
		serial.initialize();	//connect to the controller
		serial.thread(500); 	//wait .5s to get connected
		
		boolean using = true;	//continue using the terminal until false
		
		while(using)
		{
			String output = scan.nextLine();	//get input from the user to be outputed to 
												//the controller
			if(output.charAt(0) != '*')			//if the first character is the cancel key
				serial.write(output);			//then exit the loop
			else
				using = false;
		}
		
		scan.close();				//close out the utilities
		serial.close();
	}
}
