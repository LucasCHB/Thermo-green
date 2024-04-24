package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Controller;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Alert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Controller myController;
	
	private JPanel thePanel;
	private URL vinciIcon;
	
	private JLabel titleLabel;
	private JLabel telLabel;
	private JLabel msgLabel;
	
	private JTextField telText;
	private JTextField msgText;
	private JButton sendBtn;
	private JButton cancelBtn;

	/**
	 * Create the frame.
	 */
	public Alert(Controller aController) {
		
		super();
		
		this.myController = aController;
		
		vinciIcon = ConsoleGUI.class.getResource("/img/vinci_ico.jpg");
		setIconImage(Toolkit.getDefaultToolkit().getImage(vinciIcon));
		setTitle("Vinci Thermo Green - Message d'Alerte");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		// Initialisation du panneau
		this.thePanel = new JPanel();
		this.getContentPane().add(thePanel, BorderLayout.CENTER);
		thePanel.setLayout(null);
		
		titleLabel = new JLabel("Alerter");
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(87, 32, 248, 31);
		thePanel.add(titleLabel);
		
		// Création du champ de numéro de téléphone
		telLabel = new JLabel("Numéro de téléphone");
		telLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		telLabel.setBounds(48, 87, 146, 19);
		thePanel.add(telLabel);
		
		telText = new JTextField();
		telText.setBounds(220, 87, 156, 21);
		thePanel.add(telText);
		telText.setColumns(10);
		
		// Création du champ du message
		msgLabel = new JLabel("Message à envoyer");
		msgLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		msgLabel.setBounds(48, 136, 146, 19);
		thePanel.add(msgLabel);
		
		msgText = new JTextField();
		msgText.setColumns(10);
		msgText.setBounds(220, 136, 156, 21);
		thePanel.add(msgText);
		
		// Création du bouton Envoyer
		sendBtn = new JButton("Envoyer");
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (telText.getText().matches("0[0-9]{9}")
						&& !(msgText.getText().equals(""))) {
					aController.getMySMSSender().sendSMS(telText.getText(), msgText.getText());
					aController.getTheAlert().setVisible(false);
					aController.getTheConsole().setVisible(true);
				}
			}
		});
		sendBtn.setBounds(109, 217, 85, 21);
		thePanel.add(sendBtn);
		
		// Création du bouton Annuler
		cancelBtn = new JButton("Annuler");
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				aController.getTheAlert().setVisible(false);
				aController.getTheConsole().setVisible(true);
			}
		});
		cancelBtn.setBounds(220, 217, 85, 21);
		thePanel.add(cancelBtn);
	}
}
