package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.*;

import java.util.ArrayList;

import control.*;
import model.User;

/**
 * La classe Login représente la fenêtre de connexion de l'application.
 * Cette fenêtre permet à l'utilisateur de saisir son login et mot de passe pour accéder à l'application.
 * 
 * <p><strong>Composants graphiques :</strong></p>
 * <ul>
 *     <li>{@link #txtLogin} : Champ de texte pour saisir le login.</li>
 *     <li>{@link #txtPassword} : Champ de texte pour saisir le mot de passe.</li>
 *     <li>{@link #btnLogin} : Bouton de connexion.</li>
 * </ul>
 * 
 * <p><strong>Événements :</strong></p>
 * <ul>
 *     <li>La méthode {@link #mouseClicked(MouseEvent)} est déclenchée lorsqu'on clique sur le bouton de connexion.
 *         Elle vérifie les informations saisies et affiche la fenêtre de la console si les informations sont correctes.</li>
 * </ul>
 * 
 * @author Votre Nom
 * @version 1.0
 * @see Controller
 * @see User
 * @see JFrame
 */
public class Login extends JFrame {
	
	private Controller myController;

	private JPanel thePanel;
	private URL vinciIcon;
	
	private JLabel lblLogin;
	private JTextField txtLogin;
	private JLabel lblPassword;
	private JTextField txtPassword;
	private JButton btnLogin;
	
	private ArrayList<User> lesUsers = new ArrayList<User>();
	
	/**
	 * <h3>public Login(Controller aController)</h3>
     * <p>Constructeur de la classe Login.</p>
     * 
     * @param aController Le contrôleur {@code Controller} associé à la fenêtre de connexion.
     */
	public Login(Controller aController) {
		
		super();
		
		//Appelle le constructeur de la classe m�re
		vinciIcon = ConsoleGUI.class.getResource("/img/vinci_ico.jpg");
		setIconImage(Toolkit.getDefaultToolkit().getImage(vinciIcon));
		setTitle("Vinci Thermo Green - Connexion");
		
		setSize(712, 510);
		setResizable(false);
		setFont(new Font("Consolas", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Initialisation du contrôleur et récupération de la liste des utilisateurs
		this.myController = aController;
		this.lesUsers = aController.getLesUsers();
		
		// Configuration de la fenêtre principale
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialisation du panneau
		this.thePanel = new JPanel();
		this.getContentPane().add(thePanel, BorderLayout.CENTER);
		thePanel.setLayout(null);
		
		// Création du champ de Login
		this.lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLogin.setBounds(77, 73, 85, 31);
		thePanel.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(195, 79, 159, 19);
		thePanel.add(txtLogin);
		txtLogin.setColumns(10);
		
		// Création du champ de mot de passe
		this.lblPassword = new JLabel("Mot de passe");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(77, 132, 108, 31);
		thePanel.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(195, 138, 159, 19);
		thePanel.add(txtPassword);
		txtPassword.setColumns(10);
		
		// Ajout du bouton de connexion
		this.btnLogin = new JButton("Login");
		this.thePanel.add(this.btnLogin);
		this.btnLogin.setBounds(77, 207, 139, 21);
		
		JButton btnChangerMdp = new JButton("Changer MDP");
		btnChangerMdp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myController.getTheLogin().setVisible(false);
				myController.getTheMDPChange().setVisible(true);
			}
		});
		btnChangerMdp.setBounds(226, 207, 128, 21);
		thePanel.add(btnChangerMdp);
		
		// Gestionnaire d'événements pour le clic sur le bouton de connexion
		this.btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Parcours de la liste des utilisateurs
				for (User user : lesUsers) {
					
		            // Vérification des informations de connexion
		            if (user.getLogin().equals(txtLogin.getText()) && BCrypt.checkpw(txtPassword.getText(), user.getPassword())) {
		            	
		                // Affichage de la fenêtre de la console et masquage de la fenêtre de connexion
		                myController.getTheLogin().setVisible(false);
						myController.getTheConsole().setVisible(true);
						break;
		            }
		        }
			}
		});
	}
}