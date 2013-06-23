package dictionary;

import java.util.Date;

public class Message{

	final private Date data;
	final private String login_to;
	final private String login_from;
	final private String name_from;
	final private String message;
	
	public Message(Date _data, String _login_to, String _login_from, String _message, String _name_from)
	{
		data = _data;
		login_to = _login_to;
		login_from = _login_from;
		message = _message;
		name_from = _name_from;
	}
	
	public Date getDate(){ return data; }
	public String getloginTo(){ return login_to; }
	public String getloginFrom(){ return login_from; }
	public String getNameFrom(){ return name_from; }
	public String getMessage(){ return message; }
}
