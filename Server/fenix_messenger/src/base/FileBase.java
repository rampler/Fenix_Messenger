package base;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 02.03.13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class FileBase {
    private String path;
    private fileDB base = null;

    public fileDB getBase(){ return base; }
    public String getPath(){ return path; }

    public FileBase(String path) throws IOException, ClassNotFoundException
    {
        this.path = path;
        base = loadBase();
    }

    public fileDB loadBase() throws IOException, ClassNotFoundException
    {
        File plik = new File(path);
        if(plik.exists() && plik.isFile())
        {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(file);
            return (fileDB) objectInputStream.readObject();
        }
        return new fileDB();
    }

    public void saveBase() throws IOException
    {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
        objectOutputStream.writeObject(base);
    }

}
