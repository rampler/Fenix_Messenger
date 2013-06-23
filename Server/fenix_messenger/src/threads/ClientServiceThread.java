package threads;

import base.BaseManager;
import base.FileBase;
import base.mysqlDB;
import com.google.common.collect.Multimap;
import cryptography.Cryptographer;
import cryptography.moveSigns;
import dictionary.Message;
import dictionary.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class ClientServiceThread extends Thread{
	private Socket socket;
	private int id;
	private Multimap<String, Message> messagesBase;
	private Cryptographer cryptographer = new Cryptographer(new moveSigns());
    private BaseManager base = null;
    private FileBase fileBase = null;
	
	private User zalogowany;
	
	public ClientServiceThread(int _id, Socket _socket, Multimap<String, Message> _messagesBase, BaseManager base, FileBase fileBase)
	{
		id = _id;
		socket = _socket;
		messagesBase = _messagesBase;
        this.base = base;
        this.fileBase = fileBase;
	}
	
	public void run()
	{
		PrintWriter out = null;
	    Scanner in = null;
	    try
	    {
			out = new PrintWriter(socket.getOutputStream(), true);
	        in = new Scanner(socket.getInputStream());
	        out.println("done");
	        String command;
	        while (true) 
	        {
	        	command = in.nextLine();
	             if(command.equals("login"))
	             {
	             		String login = in.nextLine();
	             		String pass = in.nextLine();
	             		zalogowany = base.checkUser(login, pass);
	             		if(zalogowany != null ) 	
	             		{
	             			out.println("done");
	             			out.println(zalogowany.getName());
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+login+" - DONE");
	             		}
	             		else 
	             		{
	             			out.println("nope");
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+login+" - NOPE");
	             		}
	             }
	             else if(command.equals("logout"))
	             { 
	            	 System.out.println("SocketID: "+id+" command: "+command+" : "+zalogowany.getLogin());
	            	 zalogowany = null;
	             } 
	             else if(command.equals("register"))
	             {
	             		String loginNew = in.nextLine();
	             		String passNew = in.nextLine();
	             		String nameNew = in.nextLine();
	             		if(newUser(loginNew, passNew, nameNew)) 
	             		{
	             			out.println("done");
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+loginNew+" - DONE");
	             		}
	             		else 	
	             		{
	             			out.println("nope");
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+loginNew+" - NOPE");
	             		}
	        	}
	        	else if(command.equals("newmessage"))
	        	{
	             		String toUser = in.nextLine();
	             		String fromUser = in.nextLine();
	             		String messageText = cryptographer.cryptString(cryptographer.decryptString(in.nextLine(), base.getUser(fromUser).getPass()), base.getUser(toUser).getPass());
	             		String namefrom = in.nextLine();
	             		Date data = new Date();
	             		Message message = new Message(data, toUser, fromUser, messageText, namefrom);
	             		messagesBase.put(toUser, message);	
	             		System.out.println("SocketID: "+id+" command: "+command+" : form: "+fromUser+" - to: "+toUser);
	        	}
	        	else if(command.equals("isnew"))
	        	{
	        			String login = in.nextLine();
	        			Collection<Message> messages = messagesBase.get(login);
	        			int howmany = messages.size();
	        			String howMany = ""+howmany;
	        			out.println(howMany);
	        			for(Message i : messages)
	        			{
	        				String date = i.getDate().getTime()+"";
	        				out.println(i.getloginFrom());
	        				out.println(i.getNameFrom());
	        				out.println(date);
	        				out.println(i.getMessage());
	        			}
	        			messagesBase.removeAll(login);
	        			
	        	}
	        	else if(command.equals("finduser"))
	        	{
	             		String login = in.nextLine();
	             		if(base.findUser(login))
	             		{
	             			out.println("done");
	             			out.println(base.getUser(login).getName());
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+login+" - DONE");
	             		}
	             		else 
	             		{
	             			out.println("nope");
	             			System.out.println("SocketID: "+id+" command: "+command+" : "+login+" - NOPE");
	             		}
	        	}
	        }
	    }
	    catch(Exception e){ }
        out.close();
        in.close();
	}
	
	private boolean newUser(String login, String password, String name) throws IOException
	{
		if(!base.findUser(login))
		{
			base.addUser(login, password, name);
            if(fileBase != null) fileBase.saveBase();
			return true;
		}
		return false;
	}
}
