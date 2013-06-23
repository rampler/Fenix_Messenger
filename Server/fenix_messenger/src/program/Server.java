package program;

import base.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dictionary.Message;
import threads.ClientServiceThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static BaseManager base = null;
    private static FileBase fileBase = null;
	private static int howMany;
	private static Multimap<String, Message> messagesBase = ArrayListMultimap.create();
	private final static int socket_size = 3;
	
	public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Fenix Messenger Server ver. 1.2.0");
        System.out.print("Baza MySQL(mysql), czy plikowa(file): ");
        String basetext = in.nextLine();
        if(basetext.equals("mysql")) base = new BaseManager(new mysqlDB());
        else
        {
            try
            {
                fileBase = new FileBase("base.fxdb");
                base = new BaseManager(fileBase.getBase());
            }
            catch(Exception e){ e.printStackTrace(); }
        }
        howMany = howManyUsers();

        //Starting Server
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        Thread[] Pool = new Thread[socket_size];
        System.out.println("Users in base: "+howMany);
        System.out.println();
        System.out.println("Server status: waiting for connections");
        try 
        { 
        	serverSocket = new ServerSocket(1992); 
            int id=0;
            while (true) 
            {
            	for(int i=0; i<socket_size; i++)
            	{
            		if (Pool[i] == null || !Pool[i].isAlive())
            		{
            			clientSocket = serverSocket.accept();
                    	System.out.println("New connection - id: "+id+" adress: "+clientSocket.getInetAddress());
                    	Pool[i] = new ClientServiceThread(id, clientSocket, messagesBase, base, fileBase);
                    	Pool[i].start();
                    	id++;
            		}
            	}
            	clientSocket = serverSocket.accept();
            	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            	out.println("full");
            	out.close();
            	clientSocket = null;
            }
        } 
        catch (IOException e){ System.out.println("Port(1992) busy!"); }

        clientSocket.close();
        serverSocket.close();
    }
	
	private static int howManyUsers()
	{
		System.out.print("Connection to database...");
		int usrs = base.howManyUsers();
		System.out.print("DONE!");
		System.out.println();
		return usrs;
	}
}
