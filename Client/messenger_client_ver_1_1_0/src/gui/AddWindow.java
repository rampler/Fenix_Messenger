package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dictionaries.ContactBrowser;
import dictionaries.ContactDB;

public class AddWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

	public AddWindow(final LinkedList<String> values, final JList<Object> list, final ContactBrowser contactBrowser, final Scanner in, final PrintWriter out) {
        JPanel contentPane;
        setTitle("Add contact");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 70);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblUserLogin = new JLabel("User login:");
		panel.add(lblUserLogin);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		
		final JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() //klikni�cie a Add
		{
			public void actionPerformed(ActionEvent e) 
			{
				try
				{ 
					out.println("finduser");
					out.println(textField.getText());
					String confirmation = in.nextLine();
					if(confirmation.equals("done"))
					{
						String contactName = in.nextLine();
						ContactDB base = contactBrowser.getBase();
						base.addContact(textField.getText(), contactName);
						
						contactBrowser.saveBase();
						//Dodanie do listy
						values.add(contactName+'('+textField.getText()+')');
						AbstractListModel<Object> listModel = new AbstractListModel<Object>() {
	
							private static final long serialVersionUID = 1L;
							public int getSize() {
								return values.size();
							}
							public Object getElementAt(int index) {
								return values.get(index);
							}
						};
						list.setModel(listModel);
						dispose();
					}
					else JOptionPane.showMessageDialog(null, "User not found!");
				} 
				catch (Exception e1){ JOptionPane.showMessageDialog(null, "Contact adding error!"); }
			}
		});
		
		textField.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) btnAdd.doClick();
			}
		});
		
		panel.add(btnAdd);
	}

}
