package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import org.passay.*;

import control.BCrypt;
import control.Controller;
import model.User;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MDPChange extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private Controller myController;
	
	private JPanel thePanel;
	private URL vinciIcon;
	
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblNewPassword;
	private JLabel lblPasswordConfirm;
	
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtNewPassword;
	private JTextField txtPasswordConfirm;
	
	private JButton confirmBtn;
	private JButton cancelBtn;
	
	private ArrayList<User> lesUsers = new ArrayList<User>();
	
	/**
	 * Create the frame.
	 * @param controller 
	 */
	public MDPChange(Controller aController) throws SQLException {
		
		super();
		
		this.myController = aController;
		this.lesUsers = aController.getLesUsers();
		
		vinciIcon = ConsoleGUI.class.getResource("/img/vinci_ico.jpg");
		setIconImage(Toolkit.getDefaultToolkit().getImage(vinciIcon));
		setTitle("Vinci Thermo Green - Changement de Mot de Passe");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		// Initialisation du panneau
		this.thePanel = new JPanel();
		this.getContentPane().add(thePanel, BorderLayout.CENTER);
		thePanel.setLayout(null);
		
		// Création du champ de nom d'utilisateur
		lblUsername = new JLabel("Nom d'utilisateur");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUsername.setBounds(47, 46, 113, 19);
		thePanel.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(244, 48, 135, 19);
		thePanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		// Création du champ de l'ancien mot de passe
		lblPassword = new JLabel("Mot de Passe");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(47, 87, 113, 19);
		thePanel.add(lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(244, 89, 135, 19);
		thePanel.add(txtPassword);
		
		// Création du champ pour confirmer le nouveau mot de passe
		lblNewPassword = new JLabel("Nouveau Mot de Passe");
		lblNewPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewPassword.setBounds(47, 129, 161, 19);
		thePanel.add(lblNewPassword);
		
		txtNewPassword = new JTextField();
		txtNewPassword.setColumns(10);
		txtNewPassword.setBounds(244, 131, 135, 19);
		thePanel.add(txtNewPassword);
		
		// Création du champ du nouveau mot de passe
		lblPasswordConfirm = new JLabel("Confirmer le Mot de Passe");
		lblPasswordConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPasswordConfirm.setBounds(47, 170, 181, 19);
		thePanel.add(lblPasswordConfirm);
		
		txtPasswordConfirm = new JTextField();
		txtPasswordConfirm.setColumns(10);
		txtPasswordConfirm.setBounds(244, 172, 135, 19);
		thePanel.add(txtPasswordConfirm);
		
		// Ajout du bouton Confirmer
		confirmBtn = new JButton("Confirmer");
		confirmBtn.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // Parcours de la liste des utilisateurs
		        for (User user : lesUsers) {
		            // Vérification des informations de connexion
		            if (user.getLogin().equals(txtUsername.getText())
		                    && BCrypt.checkpw(txtPassword.getText(), user.getPassword())
		                    && !(txtNewPassword.getText().equals(""))
		                    && !(txtPasswordConfirm.getText().equals(""))
		                    && txtNewPassword.getText().equals(txtPasswordConfirm.getText())) {

		                // Mise à jour du mot de passe dans la base de données
		                try {
		                    aController.getMyDAOmySQL().changePWD(txtUsername.getText(), txtNewPassword.getText());
		                } catch (SQLException e1) {
		                    // Gestion de l'exception SQL
		                    e1.printStackTrace();
		                }

		                // Affichage de la fenêtre de la console et masquage de la fenêtre de connexion
		                aController.getTheMDPChange().setVisible(false);
		                break;
		            }
		        }
		    }
		});

		confirmBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		confirmBtn.setBounds(87, 220, 123, 21);
		thePanel.add(confirmBtn);
		
		// Ajout du bouton Annuler
		cancelBtn = new JButton("Annuler");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				aController.getTheMDPChange().setVisible(false);
				aController.getTheLogin().setVisible(true);
			}
		});
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancelBtn.setBounds(221, 220, 123, 21);
		thePanel.add(cancelBtn);
	}
	
	private void pwdVerify(String newPassword) {
		PasswordValidator validator = new PasswordValidator(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule(),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty)
        );

	    RuleResult result = validator.validate(new PasswordData(newPassword));

	    if (result.isValid()) {
	        // Le nouveau mot de passe est valide, vous pouvez le mettre à jour dans votre système.
	        System.out.println("Le nouveau mot de passe est valide.");
	        // Code pour mettre à jour le mot de passe dans votre système
	    } else {
	        // Le nouveau mot de passe ne respecte pas les règles de validation
	        System.out.println("Le nouveau mot de passe n'est pas valide. Règles non respectées :");
	        for (String message : validator.getMessages(result)) {
	            System.out.println(message);
	        }
	    }
	}
}