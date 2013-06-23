package base;

import dictionary.User;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 03.03.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class BaseManager {
    private DB base;

    public BaseManager(DB base){ this.base = base; }
    public int howManyUsers(){ return base.howManyUsers(); }
    public void addUser(String login, String pass, String name){ base.addUser(login, pass, name); }
    public void deleteUser(String login){ base.deleteUser(login); }
    public User getUser(String login){ return base.getUser(login); }
    public boolean findUser(String login){ return base.findUser(login); }
    public User checkUser(String login, String pass){ return base.checkUser(login, pass); }
}
