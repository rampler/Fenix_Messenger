package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dictionary.User;

public class mysqlDB implements DB{
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public mysqlDB(){ connect(); }

    public void connect(){
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://server/baza","login","pass");
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    public int howManyUsers()
    {
        int count = 0;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            while(rs.next()){ count = Integer.parseInt(rs.getString(1)); }
        }
        catch (SQLException ex){}
        finally {
            if (rs != null)
            {
                try { rs.close(); }
                catch (SQLException sqlEx){}
                rs = null;
            }

            if (stmt != null)
            {
                try { stmt.close(); }
                catch (SQLException sqlEx){}
                stmt = null;
            }
        }
        return count;
    }

    public User checkUser(String login, String pass)
    {
        User user = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE login='"+login+"' AND pass='"+pass+"'");
            while(rs.next())
            {
                String name = rs.getString(3);
                user = new User(login, pass, name);
            }
        }
        catch (SQLException ex){}
        finally {
            if (rs != null)
            {
                try { rs.close(); }
                catch (SQLException sqlEx){}
                rs = null;
            }

            if (stmt != null)
            {
                try { stmt.close(); }
                catch (SQLException sqlEx){}
                stmt = null;
            }
        }
        return user;
    }

    public boolean findUser(String login)
    {
        boolean confirm = false;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE login='"+login+"'");
            while(rs.next()){ confirm = true; }
        }
        catch (SQLException ex){}
        finally {
            if (rs != null)
            {
                try { rs.close(); }
                catch (SQLException sqlEx){}
                rs = null;
            }

            if (stmt != null)
            {
                try { stmt.close(); }
                catch (SQLException sqlEx){}
                stmt = null;
            }
        }
        return confirm;
    }

    public User getUser(String login)
    {
        User user = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE login='"+login+"'");
            while(rs.next())
            {
                String pass = rs.getString(2);
                String name = rs.getString(3);
                user = new User(login, pass, name);
            }
        }
        catch (SQLException ex){}
        finally {
            if (rs != null)
            {
                try { rs.close(); }
                catch (SQLException sqlEx){}
                rs = null;
            }

            if (stmt != null)
            {
                try { stmt.close(); }
                catch (SQLException sqlEx){}
                stmt = null;
            }
        }
        return user;
    }


    public void addUser(String login, String pass, String name)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO users VALUES ('"+login+"', '"+pass+"', '"+name+"')");
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteUser(String login)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM users WHERE login='"+login+"'");
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

}