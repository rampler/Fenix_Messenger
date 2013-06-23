package threads;

import gui.ConversationWindow;

import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import cryptography.Cryptographer;
import cryptography.moveSigns;
import dictionaries.Contact;
import dictionaries.ContactBrowser;
import dictionaries.ContactDB;

public class IncomingMessagesThread extends Thread{
	private String login, username, key;
	private ContactBrowser contactBrowser;
	private PrintWriter out;
    private Scanner in;
    private LinkedList<ConversationWindow> windows;
    private Cryptographer cryptographer = new Cryptographer(new moveSigns());
	
	public IncomingMessagesThread(String _login, String _username, String _key, ContactBrowser _contactBrowser, Scanner _in, PrintWriter _out, LinkedList<ConversationWindow> _windows)
	{
		login = _login;
		username = _username;
		contactBrowser = _contactBrowser;
		key = _key;
		in = _in;
		out = _out;
		windows = _windows;
	}

	public void run()
	{
        boolean logout = false;
		while(!logout)
		{
				out.println("isnew");
				out.println(login);
				int howmany = Integer.parseInt(in.nextLine());
				ContactDB base = contactBrowser.getBase();
				for(int i=0; i<howmany; i++)
				{
					String fromLogin = in.nextLine();
					String fromName = in.nextLine();
					String dateString = in.nextLine();
					String message = cryptographer.decryptString(in.nextLine(), key);
					long date = Long.parseLong(dateString);
					try 
					{ 
						Contact contact = base.getContact(fromLogin);
						if(contact != null) 
						{
							base.getContact(fromLogin).addArchiveMessage(message, date, fromName+'('+fromLogin+')'); 
							contactBrowser.saveBase();
							boolean found = false;
							for(ConversationWindow window : windows)
							{
								if(window.getTitle().equals(fromName+'('+fromLogin+')'))
								{
									found = true;
									Date date2 = new Date(date);
									String minutes = ""+date2.getMinutes();
									if(minutes.length() == 1) minutes = "0"+minutes;
									window.getTextPane().setText(window.getTextPane().getText()+date2.getHours()+':'+minutes+" - "+fromName+'('+fromLogin+')'+":"+'\t'+message+'\n');
								}
							}
							if(!found)
							{
								ConversationWindow conversationWindow = new ConversationWindow(username, login, key, contact, contactBrowser, out, windows);
								conversationWindow.setTitle(fromName+'('+fromLogin+')');
								conversationWindow.setVisible(true);
								conversationWindow.toFront();
								windows.add(conversationWindow);
							}
						}
					} 
					catch (Exception e) 
					{  
						boolean found = false;
						for(ConversationWindow window : windows)
						{
							if(window.getTitle().equals(fromName+'('+fromLogin+')'))
							{
								found = true;
								Date date2 = new Date(date);
								String minutes = ""+date2.getMinutes();
								if(minutes.length() == 1) minutes = "0"+minutes;
								window.getTextPane().setText(window.getTextPane().getText()+date2.getHours()+':'+minutes+" - "+fromName+'('+fromLogin+')'+":"+'\t'+message+'\n');
							}
						}
						if(!found)
						{
							Contact nn = new Contact(fromLogin,fromName);
							ConversationWindow conversationWindow;
							try 
							{
								conversationWindow = new ConversationWindow(username, login, key, nn, new ContactBrowser(login, false), out, windows);
								conversationWindow.setTitle(fromName+'('+fromLogin+')');
								conversationWindow.setVisible(true);
								conversationWindow.toFront();
								Date date2 = new Date(date);
								String minutes = ""+date2.getMinutes();
								if(minutes.length() == 1) minutes = "0"+minutes;
								conversationWindow.getTextPane().setText(conversationWindow.getTextPane().getText()+date2.getHours()+':'+minutes+" - "+fromName+'('+fromLogin+')'+":"+'\t'+message+'\n');
								windows.add(conversationWindow);
							} 
							catch (Exception e1) { JOptionPane.showMessageDialog(null, "Error!"); }
							
						}
					}
				}
				try { sleep(1000); } 
				catch (Exception e){ JOptionPane.showMessageDialog(null, "Error!"); }
		}
	}
}
