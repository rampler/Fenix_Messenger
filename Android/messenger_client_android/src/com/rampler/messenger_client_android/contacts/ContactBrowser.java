package com.rampler.messenger_client_android.contacts;

import java.io.*;

public class ContactBrowser {
    private ContactDB base;
    private String path;

    public ContactBrowser(String login) throws ClassNotFoundException, IOException
    {
        	path = "/mnt/sdcard/"+login+".fxa";
        	base = loadBase(path);
    }

    public ContactDB loadBase(String path) throws IOException, ClassNotFoundException
    {
        ContactDB temp = null;
        File checkfile = new File(path);
        if(checkfile.exists() && checkfile.isFile())
        {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream inputStream = new ObjectInputStream(file);
            try { temp = (ContactDB) inputStream.readObject(); }
            finally{ inputStream.close(); }
        }
        else temp = new ContactDB();
        return temp;
    }

    public void saveBase() throws IOException
    {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream outputStream = new ObjectOutputStream(file);
        try{ outputStream.writeObject(base); }
        finally{ outputStream.close(); }
    }
    
    public ContactDB getBase(){ return base; }

}
