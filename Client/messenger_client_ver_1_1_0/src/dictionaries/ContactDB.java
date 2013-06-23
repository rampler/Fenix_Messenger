package dictionaries;

import exceptions.UserNotFoundException;
import java.util.LinkedList;
import java.io.Serializable;

public class ContactDB implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<Contact> contacts;

    public ContactDB() { contacts = new LinkedList<Contact>(); }

    public void addContact(String login, String name)
    {
        Contact temp = new Contact(login, name);
        contacts.add(temp);
    }

    public Contact getContact(int uid) { return contacts.get(uid); }
    public Contact getContact(String login) throws UserNotFoundException { return contacts.get(findContactIndex(login)); }
    public LinkedList<Contact> getContactList(){ return contacts;}
    
    public int findContactIndex(String login) throws UserNotFoundException
    {
        int uid = 0;
        while(uid < contacts.size())
        {
            if(contacts.get(uid).getLogin().equals(login)) return uid;
            uid++;
        }
        throw new UserNotFoundException();
    }

    public void deleteContact(int uid){ contacts.remove(uid); }
}
