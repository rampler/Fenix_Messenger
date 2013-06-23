package com.rampler.messenger_client_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import com.rampler.messenger_client_android.contacts.ArchiveMessage;
import com.rampler.messenger_client_android.contacts.Contact;
import com.rampler.messenger_client_android.contacts.ContactBrowser;
import com.rampler.messenger_client_android.dictionary.ConnectionCommands;
import com.rampler.messenger_client_android.exceptions.UserNotFoundException;
import com.rampler.messenger_client_android.threads.IncomingMessageThread;
import com.rampler.messenger_client_android.threads.TestService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Fenix extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ViewFlipper flipper = null;
    private ListView contacts = null;
    private ListView messagesLV = null;

    private Button singinBtn = null;
    private Button registerBtn = null;
    private Button serverBtn = null;
    private Button backBtn = null;
    private Button regregisterBtn = null;
    private Button regbackBtn = null;
    private Button adduserBtn = null;
    private Button mesMessageBtn = null;

    private EditText loginET = null;
    private EditText passET = null;
    private EditText adressET = null;
    private EditText regloginET = null;
    private EditText regpassET = null;
    private EditText regpassconET = null;
    private EditText regnameET = null;
    private EditText adduserET = null;
    private EditText mesMessageET = null;

    private ConnectionCommands connectionCommands = null;
    private ContactBrowser contactBrowser = null;

    private String messagesActiveUser = "";
    private Intent intent = null;

    private void clearRegistration()
    {
        regpassconET.setText("");
        regpassET.setText("");
        regnameET.setText("");
        regloginET.setText("");
    }

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
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), rowsMap, R.layout.row, from, to);
        contacts.setAdapter(adapter);
    }

    private void updateMessagesList() throws UserNotFoundException
    {
        String[] from = new String[] {"rowmsgMessage", "rowmsgDate" };
        int[] to = new int[] { R.id.rowmsgMessage, R.id.rowmsgDate };
        List<HashMap<String, String>> rowsMap = new ArrayList<HashMap<String, String>>();
        for(ArchiveMessage i : contactBrowser.getBase().getContact(messagesActiveUser).getArchive())
        {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("rowmsgMessage",i.getName()+": "+i.getMessage());
            Date data = new Date(i.getDate());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("rowmsgDate",df.format(data));
            rowsMap.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), rowsMap, R.layout.rowmsg, from, to);
        messagesLV.setAdapter(adapter);
    }

    /**
     *  Views in flipper:
     *  0 - login layout
     *  1 - register layout
     *  2 - server layout
     *  3 - user list layout
     *  4 - messages layout
     *  5 - add user layout
     */

    //Back button reaction
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(flipper.getDisplayedChild() == 1)
            {
                flipper.setDisplayedChild(0);
                clearRegistration();
            }
            else if(flipper.getDisplayedChild() == 2) flipper.setDisplayedChild(0);
            else if(flipper.getDisplayedChild() == 5)
            {
                flipper.setDisplayedChild(3);
                adduserET.setText("");
            }
            else if(flipper.getDisplayedChild() == 4) flipper.setDisplayedChild(3);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.contextmenu_delete:
                contactBrowser.getBase().deleteContact(Integer.parseInt(info.id+""));
                updateContactsList();
                try{ contactBrowser.saveBase();} catch (IOException e){ Toast.makeText(getApplicationContext(),"Base saving error!",Toast.LENGTH_SHORT).show(); }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(flipper.getDisplayedChild() == 3)
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.layout.menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_add:
                flipper.setDisplayedChild(5);
                return true;

            case R.id.menu_logout:
                connectionCommands.logout();
                flipper.setDisplayedChild(0);
                //TODO sgetApplicationContext().stopService(intent);
                return true;

            case R.id.menu_quit:
                connectionCommands.logout();
                flipper.setDisplayedChild(0);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        connectionCommands = new ConnectionCommands();
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        contacts = (ListView) findViewById(R.id.listView);
        registerForContextMenu(contacts);
        messagesLV = (ListView) findViewById(R.id.messagesLV);

        //LoginLayout
        singinBtn = (Button) findViewById(R.id.singin);
        registerBtn = (Button) findViewById(R.id.register);
        serverBtn = (Button) findViewById(R.id.server);
        loginET = (EditText) findViewById(R.id.login);
        passET = (EditText) findViewById(R.id.password);

        //ServerLayout
        backBtn = (Button) findViewById(R.id.back);
        adressET = (EditText) findViewById(R.id.adress);

        //RegisterLayout
        regregisterBtn = (Button) findViewById(R.id.regregister);
        regloginET = (EditText) findViewById(R.id.reglogin);
        regpassET = (EditText) findViewById(R.id.regpass);
        regpassconET = (EditText) findViewById(R.id.regpasscon);
        regnameET = (EditText) findViewById(R.id.regname);
        regbackBtn = (Button) findViewById(R.id.regback);

        //AddUserLayout
        adduserBtn = (Button) findViewById(R.id.adduserbtn);
        adduserET = (EditText) findViewById(R.id.adduserET);

        //MessagesLayout
        mesMessageBtn = (Button) findViewById(R.id.sendBtn);
        mesMessageET = (EditText) findViewById(R.id.messageET);

        //Listeners
        singinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(connectionCommands.connect(adressET.getText().toString()))
                    {
                        if(connectionCommands.login(loginET.getText().toString(),passET.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(),"Logged in!", Toast.LENGTH_SHORT).show();
                            flipper.setDisplayedChild(3);
                            contactBrowser = new ContactBrowser(connectionCommands.getUser_login());
                            updateContactsList();

                            //TODO SERVICE
                            //intent = new Intent(getApplicationContext(), TestService.class);
                            //getApplicationContext().startService(intent);
                        }
                        else Toast.makeText(getApplicationContext(),"Bad login or/and password!",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(),"Server full!",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){ Toast.makeText(getApplicationContext(),"Connection refused!",Toast.LENGTH_SHORT).show(); }
            }
        });

        mesMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionCommands.newmessage(messagesActiveUser, mesMessageET.getText().toString());
                try
                {
                    contactBrowser.getBase().getContact(messagesActiveUser).addArchiveMessage(mesMessageET.getText().toString(),new Date().getTime(), connectionCommands.getUser_name());
                    updateMessagesList();
                    updateContactsList();
                    contactBrowser.saveBase();
                }
                catch(UserNotFoundException e){ Toast.makeText(getApplicationContext(), "Archive Loading Error!", Toast.LENGTH_SHORT).show(); }
                catch (IOException e){ Toast.makeText(getApplicationContext(), "Base saving error!", Toast.LENGTH_SHORT).show(); }
                mesMessageET.setText("");
            }
        });

        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView rowLogin = (TextView) view.findViewById(R.id.rowLogin);
                TextView rowName = (TextView) view.findViewById(R.id.rowName);
                TextView messagesName = (TextView) findViewById(R.id.messagesName);
                messagesName.setText(rowName.getText().toString());
                messagesActiveUser = rowLogin.getText().toString();
                try{ updateMessagesList(); } catch(UserNotFoundException e){ Toast.makeText(getApplicationContext(),"Archive Loading Error!", Toast.LENGTH_SHORT).show(); }
                flipper.setDisplayedChild(4);
            }
        });

        adduserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String foundUserName = connectionCommands.finduser(adduserET.getText().toString());
                    contactBrowser.getBase().addContact(adduserET.getText().toString(),foundUserName);
                    updateContactsList();
                    contactBrowser.saveBase();
                    Toast.makeText(getApplicationContext(),"User added!",Toast.LENGTH_SHORT).show();
                    flipper.setDisplayedChild(3);
                    adduserET.setText("");
                }
                catch(UserNotFoundException e){
                    Toast.makeText(getApplicationContext(),"User not found!",Toast.LENGTH_SHORT).show();
                    flipper.setDisplayedChild(3);
                    adduserET.setText("");
                }
                catch(IOException e){ Toast.makeText(getApplicationContext(),"Saving Contact Base Error!",Toast.LENGTH_SHORT).show(); }
            }
        });

        regregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(regpassET.getText().toString().equals(regpassconET.getText().toString()))
                    {
                        if(connectionCommands.connect(adressET.getText().toString()))
                        {
                            if(connectionCommands.register(regloginET.getText().toString(),regpassET.getText().toString(),regnameET.getText().toString()))
                            {
                                Toast.makeText(getApplicationContext(), "Account created!", Toast.LENGTH_SHORT).show();
                                flipper.setDisplayedChild(0);
                                clearRegistration();
                            }
                            else Toast.makeText(getApplicationContext(),"User already exist!",Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getApplicationContext(),"Server full!",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(),"Passwords don't match!",Toast.LENGTH_SHORT).show();
                }
                catch(IOException e){ Toast.makeText(getApplicationContext(),"Connection refused!",Toast.LENGTH_SHORT).show(); }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.setDisplayedChild(1);
            }
        });

        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.setDisplayedChild(2);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.setDisplayedChild(0);
            }
        });

        regbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.setDisplayedChild(0);
                clearRegistration();
            }
        });
    }
}
