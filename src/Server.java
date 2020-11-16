/*
Filename: Client.java         
Author: Kaylin Moodley
Created: 24/09/2020
Operating System: Windows 10
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server 
{
    //Variable Declaration
    private ServerSocket server;
    private Socket socket;
    private ObjectInputStream input;// input stream from client
    private ObjectOutputStream output; // output stream to client
    private String prodName;
    private String prodType;
    private double prodPrice;
   
    
    public static com.mysql.jdbc.Connection getConnection() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/checkersproducts","root","");
        return conn;
    }

    public void runServer() throws IOException, Exception
    {
        try
        {   
            server =new ServerSocket(8000);//run on port 8000
            System.out.println("Server is starting...");
            socket=server.accept();
            this.prodName="";
            this.prodType="";
            input = new ObjectInputStream( socket.getInputStream()); 
            output = new ObjectOutputStream( socket.getOutputStream());
            output.flush(); 
            
            while(true)
            {

                this.prodName= (String) input.readObject();
                this.prodType= (String) input.readObject();
                this.prodPrice= (Double) input.readObject();
                
                if(this.prodName.equals("stop")||this.prodType.equals("stop"))
                {
                    output.close();
                    input.close();
                    socket.close();//close socket
                    server.close();//close serveSocker
                    System.out.println("Server stopping....");
                    break;
                }
                
                System.out.println("Server received Product Details: "+this.prodName+" "+this.prodType+" "+this.prodPrice); 
            
                Connection conn=getConnection();
                System.out.println("Connected to the database checkersproducts");
                String sql="INSERT INTO products(prodName,prodType,prodPrice) VALUES(?,?,?)";
                PreparedStatement ps=conn.prepareStatement(sql);

                ps.setString(1,this.prodName);
                ps.setString(2,this.prodType);
                ps.setDouble(3,this.prodPrice);
                
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0)
                {
                System.out.println("Products has been added to database"); 
                    output.writeObject("Server says: Product has been added" );
                    output.flush();
                }
                else
                {
                    output.writeObject("Server says: Error adding product to database" );
                    output.flush();
                }
            }
           
        }
        catch(ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        }   
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws IOException, Exception
    {
        Server serverSide = new Server();
        serverSide.runServer();
    }
}
