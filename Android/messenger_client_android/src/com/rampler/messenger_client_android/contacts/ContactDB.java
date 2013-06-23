package com.rampler.messenger_client_android.contacts;

import com.rampler.messenger_client_android.exceptions.UserNotFoundException;

import java.io.Serializable;
import java.util.LinkedList;

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

    public void deleteContact(int uid){ contacts.remove(uid); }
    public void deleteContact(String login) throws UserNotFoundException { contacts.remove(findContactIndex(login)); }
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

}
