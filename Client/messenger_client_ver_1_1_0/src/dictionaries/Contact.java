package dictionaries;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Contact implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String login;
    private String name;
    private LinkedList<ArchiveMessage> archive = new LinkedList<ArchiveMessage>();

    public Contact(String _login, String _name)
    {
        login = _login;
        name = _name;
    }
    public String getLogin(){ return login; }
    public String getName(){ return name; }
    public Queue<ArchiveMessage> getArchive() { return archive; }
    public void addArchiveMessage(String message, long date, String visiblename)
    {
        if(archive.size() == 50) archive.removeFirst();
        ArchiveMessage temp = new ArchiveMessage(message, date, visiblename);
        archive.addLast(temp);
    }
}
