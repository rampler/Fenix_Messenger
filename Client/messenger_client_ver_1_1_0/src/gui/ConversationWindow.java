package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import cryptography.Cryptographer;
import cryptography.moveSigns;
import dictionaries.ArchiveMessage;
import dictionaries.Contact;
import dictionaries.ContactBrowser;


public class ConversationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextPane txtpnABC;
	private Cryptographer cryptographer = new Cryptographer(new moveSigns());
	
	public JTextPane getTextPane(){ return txtpnABC; }
	@SuppressWarnings("deprecation")
	public ConversationWindow(final String username, final String login, final String key, final Contact contact, final ContactBrowser contactBrowser, final PrintWriter out, final LinkedList<ConversationWindow> windows) {
        JPanel contentPane;
        setBackground(new Color(176, 224, 230));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		contentPane.add(panel, BorderLayout.SOUTH);
		
		textField = new JTextField();
		textField.setBackground(new Color(240, 255, 240));
		textField.setColumns(255);
		
		final JButton btnSend = new JButton("Send");
		btnSend.setBackground(new Color(240, 255, 240));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(textField);
		panel.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		txtpnABC = new JTextPane();
		txtpnABC.setBackground(new Color(240, 255, 240));
		txtpnABC.setEditable(false);
		scrollPane.setViewportView(txtpnABC);
		
		//FUNKCJE
		textField.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) btnSend.doClick();
			}
		});
		
		btnSend.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				out.println("newmessage");
				out.println(contact.getLogin());
				out.println(login);
				out.println(cryptographer.cryptString(textField.getText(), key));
				out.println(username);
				Date date = new Date();
				String minutes = ""+date.getMinutes();
				if(minutes.length() == 1) minutes = "0"+minutes;
				txtpnABC.setText(txtpnABC.getText()+date.getHours()+':'+minutes+" - "+username+":"+'\t'+textField.getText()+'\n');
				
				contact.addArchiveMessage(textField.getText(), date.getTime(), username);
				try { contactBrowser.saveBase(); } 
				catch (Exception e1) { JOptionPane.showMessageDialog(null, "Archive saving error!"); }
				textField.setText("");
			}
		});
		
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowOpened(WindowEvent arg0) 
			{
				for(ArchiveMessage i : contact.getArchive())
				{
					Date date = new Date(i.getDate());
					String minutes = ""+date.getMinutes();
					if(minutes.length() == 1) minutes = "0"+minutes;
					txtpnABC.setText(txtpnABC.getText()+date.getHours()+':'+minutes+" - "+i.getName()+":"+'\t'+i.getMessage()+'\n');
				}
			}
			@Override
			public void windowClosed(WindowEvent e) 
			{
				ConversationWindow temp = null;
				for(ConversationWindow window : windows)
				{
					if(window.getTitle().equals(getTitle())) temp = window;
				}
				windows.remove(temp);
			}
		});
	}

}
