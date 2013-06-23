package dictionaries;

import java.io.*;

public class ContactBrowser {
    private ContactDB base;
    private String path;

    public ContactBrowser(String login, boolean exist) throws ClassNotFoundException, IOException
    { 
    	if(!exist) 
    	{
    		base = new ContactDB(); 
    		path = login+".mcmb";
    	}
    	else
    	{ 
        	path = login+".mcmb";
        	base = loadBase(path); 
        }
    }

    public ContactDB loadBase(String path) throws IOException, ClassNotFoundException
    {
        ContactDB temp;
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream inputStream = new ObjectInputStream(file);
        try { temp = (ContactDB) inputStream.readObject(); }
        finally{ inputStream.close(); }
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
