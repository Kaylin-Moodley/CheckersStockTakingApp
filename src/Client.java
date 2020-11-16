/*
Filename: Client.java         
Author: Kaylin Moodley
Created: 24/09/2020
Operating System: Windows 10
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client  
{
    //Variable Declaration
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input;// input stream from server
    private Socket socket; // socket to communicate with server
    private String prodName;
    private String prodType;
    private double prodPrice;
    private String sqlMessage;

    
    public void runClient() throws ClassNotFoundException
    {
        try 
        {
            Scanner scanInput=new Scanner(System.in).useDelimiter("\n");
  
            socket = new Socket("Localhost", 8000 );
            output = new ObjectOutputStream( socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream( socket.getInputStream());
            this.prodName="";
            this.prodType="";
            System.out.println("Welcome to Checkers Stock Taking App");
            while(true)
            {
                System.out.println("Enter Product Name:");
                this.prodName=scanInput.next();
                System.out.println("Enter Product Type:");
                this.prodType=scanInput.next();
                System.out.println("Enter Product Price:");
                this.prodPrice=scanInput.nextDouble();
                
                output.writeObject( this.prodName);
                output.writeObject( this.prodType);
                output.writeObject( this.prodPrice);
                output.flush(); 
                if(this.prodName.equals("stop")||this.prodType.equals("stop"))
                {
                    socket.close();
                    output.close();
                    input.close();  
                    System.out.println("Exiting Checkers Stock Taking App, Good Bye!");
                    break;
                }
      
                this.sqlMessage= (String) input.readObject();
                System.out.println(sqlMessage);
            }
        }
        catch ( IOException ioe )
        {
                ioe.printStackTrace();
        } 
     } 
    
    public static void main(String[] args) throws ClassNotFoundException 
    {
	Client client=new Client();
	client.runClient();
    }
}
