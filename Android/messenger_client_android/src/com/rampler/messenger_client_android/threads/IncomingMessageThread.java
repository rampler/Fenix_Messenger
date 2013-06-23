package com.rampler.messenger_client_android.threads;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.rampler.messenger_client_android.R;
import com.rampler.messenger_client_android.contacts.ArchiveMessage;
import com.rampler.messenger_client_android.contacts.Contact;
import com.rampler.messenger_client_android.contacts.ContactBrowser;
import com.rampler.messenger_client_android.contacts.ContactDB;
import com.rampler.messenger_client_android.dictionary.ConnectionCommands;
import com.rampler.messenger_client_android.dictionary.Message;
import com.rampler.messenger_client_android.exceptions.UserNotFoundException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 04.03.13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class IncomingMessageThread extends Thread{
    private ContactBrowser contactBrowser = null;
    private ConnectionCommands connectionCommands = null;
    private ListView contacts = null;
    private Context applicationContext = null;

    private void updateContactsList()
    {
        String[] from = new String[] {"rowName", "rowDate", "rowMessage","rowLogin"};
        int[] to = new int[] { R.id.rowName, R.id.rowDate, R.id.rowMessage, R.id.rowLogin};
        List<HashMap<String, String>> rowsMap = new ArrayList<HashMap<String, String>>();
        for(Contact i : contactBrowser.getBase().getContactList())
        {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("rowName",i.getName()+" ("+i.getLogin()+")");
            map.put("rowLogin",i.getLogin());
            LinkedList<ArchiveMessage> archiveMessages = i.getArchive();
            if(archiveMessages.size() != 0)
            {
                Date data = new Date(archiveMessages.getLast().getDate());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("rowDate",df.format(data));
                map.put("rowMessage", archiveMessages.getLast().getMessage());
            }
            else
            {
                map.put("rowDate","");
                map.put("rowMessage","");
            }
            rowsMap.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(applicationContext, rowsMap, R.layout.row, from, to);
        contacts.setAdapter(adapter);
    }

    public IncomingMessageThread(Context applicationContext, ListView contacts, ContactBrowser contactBrowser, ConnectionCommands connectionCommands)
    {
        this.applicationContext = applicationContext;
        this.contactBrowser = contactBrowser;
        this.connectionCommands = connectionCommands;
        this.contacts = contacts;
    }

    public void run()
    {
        while(true)
        {
            //Toast.makeText(applicationContext,"Check",Toast.LENGTH_SHORT).show();
            /*LinkedList<Message> messages = connectionCommands.newmessages();
            ContactDB base = contactBrowser.getBase();
            for(Message msg : messages)
            {
                try
                {
                    Contact contact = base.getContact(msg.getloginFrom());
                    contact.addArchiveMessage(msg.getMessage(),msg.getDate(),msg.getNameFrom()+"("+msg.getloginFrom()+")");
                    updateContactsList();
                    contactBrowser.saveBase();
                }
                catch(UserNotFoundException e)
                {
                    //TODO Wiadomość od nieznajomego
                    Toast.makeText(applicationContext, "Message from unknown user: "+msg.getNameFrom()+"("+msg.getloginFrom()+") blocked! Add user first.",Toast.LENGTH_SHORT).show();
                }
                catch(IOException e){ Toast.makeText(applicationContext, "Base Saving Error!", Toast.LENGTH_SHORT).show(); }
            }     */
            try{ sleep(3000); }
            catch(InterruptedException e){ Toast.makeText(applicationContext, "Timer error! Incoming messages not work!", Toast.LENGTH_SHORT).show(); }
        }
    }
}
