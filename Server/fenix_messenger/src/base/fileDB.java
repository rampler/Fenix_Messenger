package base;

import dictionary.User;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 02.03.13
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class fileDB implements DB, Serializable{

    private HashMap<String, User> map = new HashMap<String, User>();

    public int howManyUsers(){ return map.size(); }
    public void addUser(String login, String pass, String name){ map.put(login, new User(login,pass,name)); }
    public void deleteUser(String login){ map.remove(login); }
    public User getUser(String login){ return map.get(login); }
    public boolean findUser(String login)
    {
        if(map.get(login) != null) return true;
        return false;
    }
    public User checkUser(String login, String pass)
    {
        User temp = map.get(login);
        if(temp != null && temp.getPass().equals(pass)) return temp;
        return null;
    }
}
