package dictionary;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private String login;
	private String pass;
	private String name;
	
	public User(String _login, String _pass, String _name)
	{
		login = _login;
		pass = _pass;
		name = _name;
	}
	public String getLogin(){ return login; }
	public String getPass(){ return pass; }
	public String getName(){ return name; }
	
	public boolean changePass(String old_pass, String new_pass)
	{ 
		if(old_pass.equals(pass)) 
		{
			pass = new_pass;
			return true;
		}
		return false;
	}
	
	public void changeName(String _name){ name = _name; }
}
