package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import threads.IncomingMessagesThread;
import dictionaries.Contact;
import dictionaries.ContactBrowser;
import dictionaries.ContactDB;
import javax.swing.SwingConstants;


public class MainForm {

	private JFrame frmMolicareMessager;
	private JTextField loginField;
	private JPasswordField passwordField;
	private JTextField RegLoginField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JTextField RegNameField;
	//Kontakty
	private ContactBrowser contactBrowser;
	private LinkedList<String> values = new LinkedList<String>();
	private String username;
	private String login;
	private String key;
    private LinkedList<ConversationWindow> windows = new LinkedList<ConversationWindow>();
	//Przesyłanie danych
	private static Socket echoSocket = null;
    private static PrintWriter out = null;
    private static Scanner in = null;
    private Thread incomingMessages;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() 
			{
				try 
				{
					echoSocket = new Socket("fenix.servebeer.com", 1992);
		            out = new PrintWriter(echoSocket.getOutputStream(), true);
		            in = new Scanner(new InputStreamReader(
		            echoSocket.getInputStream()));
		            if(!in.nextLine().equals("full"))
		            {
						MainForm window = new MainForm();
						window.frmMolicareMessager.setVisible(true);
		            }
		            else 
		            {
		            	JOptionPane.showMessageDialog(null, "Server full!");
		            	System.exit(0);
		            }
				} 
				catch (Exception e) { JOptionPane.showMessageDialog(null, "Connection refused!"); }
			}
		});
	}

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
	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMolicareMessager = new JFrame();
		frmMolicareMessager.getContentPane().setBackground(new Color(240, 255, 240));
		frmMolicareMessager.setTitle("MoliCare Messenger");
		frmMolicareMessager.setBounds(450, 100, 270, 320);
		frmMolicareMessager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		frmMolicareMessager.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setVerticalAlignment(SwingConstants.TOP);
		lblLogin.setBackground(new Color(240, 255, 240));
		
		loginField = new JTextField();
		loginField.setBackground(new Color(240, 255, 240));
		loginField.setColumns(14);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBackground(new Color(240, 255, 240));
		
		passwordField = new JPasswordField();
		passwordField.setBackground(new Color(240, 255, 240));
		passwordField.setColumns(14);
		
		final JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setBackground(new Color(240, 255, 240));
		
		JLabel lblSignIn = new JLabel("Sign In");
		lblSignIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblRegister = new JLabel("Register");
		lblRegister.setBackground(new Color(240, 255, 240));
		lblRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblLogin_1 = new JLabel("Login:");
		lblLogin_1.setBackground(new Color(240, 255, 240));
		
		RegLoginField = new JTextField();
		RegLoginField.setBackground(new Color(240, 255, 240));
		RegLoginField.setColumns(10);
		
		JLabel lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setBackground(new Color(240, 255, 240));
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBackground(new Color(240, 255, 240));
		passwordField_1.setColumns(10);
		
		JLabel lblRepeatPassword = new JLabel("Repeat Password:");
		lblRepeatPassword.setBackground(new Color(240, 255, 240));
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBackground(new Color(240, 255, 240));
		passwordField_2.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBackground(new Color(240, 255, 240));
		
		RegNameField = new JTextField();
		RegNameField.setBackground(new Color(240, 255, 240));
		RegNameField.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBackground(new Color(240, 255, 240));
		
		btnRegister.addActionListener(new ActionListener() //Register
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) 
			{
				String login = RegLoginField.getText();
				String pass = passwordField_1.getText();
				String passconf = passwordField_2.getText();
				String name = RegNameField.getText();
				
				if(!login.isEmpty() && !pass.isEmpty() && !passconf.isEmpty() && !name.isEmpty())
				{
					if(pass.equals(passconf))
					{
						out.println("register");
						out.println(login);
						out.println(md5(pass));
						out.println(name);
						String confirmation = in.nextLine();
						if(confirmation.equals("done")) JOptionPane.showMessageDialog(null, "Registration complete. You can sign in now.");
						else JOptionPane.showMessageDialog(null, "User already exists!");
					}
					else JOptionPane.showMessageDialog(null, "Passwords must be the same!");
				}
				else JOptionPane.showMessageDialog(null, "All fields are required!");
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPassword)
										.addComponent(lblLogin))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(loginField, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
										.addComponent(passwordField)
										.addComponent(btnSignIn, Alignment.LEADING)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblName)
										.addComponent(lblLogin_1)
										.addComponent(lblPassword_1)
										.addComponent(lblRepeatPassword))
									.addGap(19)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(passwordField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(RegNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(RegLoginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(101)
							.addComponent(lblSignIn))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(92)
							.addComponent(lblRegister))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(80)
							.addComponent(btnRegister)))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblSignIn)
					.addGap(10)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLogin))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSignIn)
					.addGap(11)
					.addComponent(lblRegister)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(RegLoginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLogin_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword_1))
					.addGap(8)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRepeatPassword))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(RegNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(btnRegister))
		);
		panel.setLayout(gl_panel);
		
		final JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(240, 255, 240));
		panel_1.setVisible(false);
		frmMolicareMessager.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(240, 255, 240));
		panel_2.setBorder(new LineBorder(Color.GRAY));
		panel_1.add(panel_2, BorderLayout.NORTH);
		
		JButton btnDodaj = new JButton("Add");
		btnDodaj.setBackground(new Color(240, 255, 240));
		panel_2.add(btnDodaj);
		
		JButton btnUsu = new JButton("Delete");
		btnUsu.setBackground(new Color(240, 255, 240));
		panel_2.add(btnUsu);
		
		final JButton btnLogout = new JButton("Logout");
		btnLogout.setBackground(new Color(240, 255, 240));
		btnLogout.setVisible(false);
		panel_2.add(btnLogout);
		
		final JList<Object> list = new JList<Object>();
		list.setBackground(new Color(240, 255, 240));
		list.setVisible(false);
		list.setBorder(new LineBorder(Color.GRAY));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 14));
		list.setModel(new AbstractListModel<Object>() {

			private static final long serialVersionUID = 1L;
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				return values.get(index);
			}
		});
		panel_1.add(list, BorderLayout.CENTER);
		
  //FUNKCJE
		list.addMouseListener(new MouseAdapter()  //Klikni�cie myszk� w usera na li�cie
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()) 
				{
					arg0.consume();
					//handle double click.
					ContactDB base = contactBrowser.getBase();
					ConversationWindow conversationWindow = new ConversationWindow(username, login, key, base.getContact(list.getSelectedIndex()), contactBrowser, out, windows);
					conversationWindow.setTitle(list.getSelectedValue().toString());
					conversationWindow.setVisible(true);
					windows.add(conversationWindow);
				}
				
			}
		});
		
		btnDodaj.addActionListener(new ActionListener() //Klikni�cie w Add
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame addWindow = new AddWindow(values, list, contactBrowser, in, out);
				addWindow.setVisible(true);
			}
		});
		
		btnUsu.addActionListener(new ActionListener() //Klikni�cie w Delete 
		{
			public void actionPerformed(ActionEvent e) 
			{
				ContactDB base = contactBrowser.getBase();
				base.deleteContact(list.getSelectedIndex());
				values.remove(list.getSelectedIndex());
				list.setModel(new AbstractListModel<Object>() {

					private static final long serialVersionUID = 1L;
					public int getSize() {
						return values.size();
					}
					public Object getElementAt(int index) {
						return values.get(index);
					}
				});
				try{ contactBrowser.saveBase(); } 
				catch (Exception e1){ JOptionPane.showMessageDialog(null, "Saving error!"); }
			}
		});
		
		btnSignIn.addActionListener(new ActionListener() //Logowanie
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) 
			{
				String templogin = loginField.getText();
				String temppass = passwordField.getText();
				if(!templogin.isEmpty() && !temppass.isEmpty())
				{
					out.println("login");
					out.println(loginField.getText());
					out.println(md5(passwordField.getText()));
					String confirmation = in.nextLine();
					if(confirmation.equals("done"))
					{
						username = in.nextLine();
						login = loginField.getText();
						key = md5(passwordField.getText());
						try
						{ 
							frmMolicareMessager.setTitle(username);
							panel.setVisible(false);
							btnLogout.setVisible(true);
							list.setVisible(true);
							panel_1.setVisible(true);
							frmMolicareMessager.setBounds(frmMolicareMessager.getBounds().x, frmMolicareMessager.getBounds().y, 250, 500);
							contactBrowser = new ContactBrowser(login, true); 
							ContactDB base = contactBrowser.getBase();
							LinkedList<Contact> contacts = base.getContactList();
							values.clear();
							for(Contact i:contacts) values.add(i.getName()+'('+i.getLogin()+')');
							list.setModel(new AbstractListModel<Object>() {
	
								private static final long serialVersionUID = 1L;
								public int getSize() {
									return values.size();
								}
								public Object getElementAt(int index) {
									return values.get(index);
								}
							});
							incomingMessages = new IncomingMessagesThread(login, username, key, contactBrowser, in, out, windows);
							incomingMessages.start();
						} 
						catch (Exception arg)
						{ 
							//Contact loading error
							try
							{ 
								contactBrowser = new ContactBrowser(login, false);
								contactBrowser.saveBase();
								frmMolicareMessager.setTitle(username);
								panel.setVisible(false);
								btnLogout.setVisible(true);
								list.setVisible(true);
								panel_1.setVisible(true);
								frmMolicareMessager.setBounds(frmMolicareMessager.getBounds().x, frmMolicareMessager.getBounds().y, 250, 500);
								incomingMessages = new IncomingMessagesThread(login, username, key, contactBrowser, in, out, windows);
								incomingMessages.start();
							}
							catch(Exception s){ JOptionPane.showMessageDialog(null, "Base saving error!"); }
						}
					}
					else JOptionPane.showMessageDialog(null, "Bad login, password or user not found!");
				}
				else JOptionPane.showMessageDialog(null, "All fields are required!");
			}
		});
		
		btnLogout.addActionListener(new ActionListener() //Logout
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) 
			{
				out.println("logout");
				incomingMessages.stop();
				frmMolicareMessager.setTitle("MoliCare Messenger");
				panel.setVisible(true);
				btnLogout.setVisible(false);
				list.setVisible(false);
				panel_1.setVisible(false);
				values.clear();
				list.setModel(new AbstractListModel<Object>() {
					
					private static final long serialVersionUID = 1L;
					public int getSize() {
						return values.size();
					}
					public Object getElementAt(int index) {
						return values.get(index);
					}
				});
				frmMolicareMessager.setBounds(frmMolicareMessager.getBounds().x, frmMolicareMessager.getBounds().y, 270, 320);
			}
		});
		
		frmMolicareMessager.addWindowListener(new WindowAdapter() //Wy��czenie programu
		{
			@Override
			public void windowClosed(WindowEvent arg0) 
			{
		        try 
		        {
		        	out.close();
			        in.close();
					echoSocket.close();
				} 
		        catch (Exception e){ JOptionPane.showMessageDialog(null, "Connection error!"); }
			}
		});
		
		passwordField.addKeyListener(new KeyAdapter() //Enter na Password
		{
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) btnSignIn.doClick();
			}
		});
		
		loginField.addKeyListener(new KeyAdapter() //Enter na Login
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER) btnSignIn.doClick();
			}
		});
		
	}
}
