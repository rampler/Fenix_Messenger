package com.rampler.messenger_client_android.dictionary;

import com.rampler.messenger_client_android.cryptography.Cryptographer;
import com.rampler.messenger_client_android.cryptography.moveSigns;
import com.rampler.messenger_client_android.exceptions.UserNotFoundException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 23.02.13
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionCommands {

    private Socket socket = null;
    private Scanner in = null;
    private PrintWriter out = null;
    private String user_name = "";
    private String user_login = "";
    private String password_hash = "";
    private String adress = "";
    private boolean isLogin = false;

    public String getUser_name(){ return user_name; }
    public String getUser_login(){ return user_login; }
    public String getPassword_hash(){ return password_hash; }
    public boolean isLogin(){ return isLogin; }

    public String md5(String password)
    {
        StringBuffer sb = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return sb.toString();
    }

    public boolean connect(String adress)  throws IOException
    {
        if(socket == null && this.adress != adress)
        {
            socket = new Socket(adress, 1992);
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.adress = adress;
            if(in.nextLine().equals("done")) return true;
            else
            {
                this.adress = "";
                socket = null;
                in = null;
                out = null;
                return false;
            }
        }
        else return true;
    }

    public boolean login(String login, String password)
    {
        out.println("login");
        out.println(login);
        out.println(md5(password));
        if(in.nextLine().equals("done"))
        {
            user_name = in.nextLine();
            user_login = login;
            password_hash = md5(password);
            isLogin = true;
            return true;
        }
        return false;
    }

    public void logout()
    {
        out.println("logout");
        user_login = "";
        user_name = "";
        password_hash = "";
        isLogin = false;
    }

    public boolean register(String login, String password, String name)
    {
        out.println("register");
        out.println(login);
        out.println(md5(password));
        out.println(name);
        if(in.nextLine().equals("done")) return true;
        return false;
    }

    public void newmessage(String toUser, String message)
    {
        Cryptographer cryptographer = new Cryptographer(new moveSigns());
        message = cryptographer.cryptString(message,password_hash);
        out.println("newmessage");
        out.println(toUser);
        out.println(user_login);
        out.println(message);
        out.println(user_name);
    }

    public String finduser(String login) throws UserNotFoundException
    {
        out.println("finduser");
        out.println(login);
        if(in.nextLine().equals("done")) return in.nextLine();
        throw new UserNotFoundException();
    }

    public LinkedList<Message> newmessages()
    {
        Cryptographer cryptographer = new Cryptographer(new moveSigns());
        LinkedList<Message> messages = new LinkedList<Message>();
        out.println("isnew");
        out.println(user_login);
        int howmany = Integer.parseInt(in.nextLine());
        for(int i=0; i<howmany; i++)
        {
            String userFrom = in.nextLine();
            String userFromName = in.nextLine();
            String date = in.nextLine();
            String message = cryptographer.decryptString(in.nextLine(), password_hash);
            messages.add(new Message(Long.parseLong(date),user_login,userFrom,message,userFromName));
        }
        return messages;
    }

}
