package base;

import dictionary.User;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 03.03.13
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public interface DB {
    public int howManyUsers();
    public void addUser(String login, String pass, String name);
    public void deleteUser(String login);
    public User getUser(String login);
    public boolean findUser(String login);
    public User checkUser(String login, String pass);
}
