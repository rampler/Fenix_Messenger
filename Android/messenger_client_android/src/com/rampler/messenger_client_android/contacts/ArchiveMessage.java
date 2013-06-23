package com.rampler.messenger_client_android.contacts;

import java.io.Serializable;

public class ArchiveMessage implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
    private long date;
    private String name;

    public ArchiveMessage(String _message, long _date, String _name)
    {
        message = _message;
        date = _date;
        name = _name;
    }

    public String getMessage(){ return message; }
    public long getDate(){ return date; }
    public String getName(){ return name; }
}
