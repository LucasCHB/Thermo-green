/**
 * @author J�r�me Valenti 
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTable;

import DAO.*;
import model.*;
import view.*;
import control.DAOmySQL;

/**
 * <p>
 * Le controlleur :
 * </p>
 * <ol>
 * <li>lit les mesures de température dans un fichier texte</li>
 * <li>retourne la collection des mesures</li>
 * </ol>
 * 
 * @author J�r�me Valenti
 * @version 2.0.0
 *
 */
public class Controller {
	// Specifications
	/**
	 * <h2>Les mesures lues dans le fichier des relevés de températures</h2>
	 */
	
	/**
	 * <h3>ArrayList<Mesure> lesMesures</h3>
	 * <p>Représente les mesures lues dans le fichier des relevés de températures.</p>
	 */
	private ArrayList<Mesure> lesMesures = new ArrayList<Mesure>();
	
	/**
	 * <h3>ArrayList<User> lesUsers</h3>
	 * <p>Représente la liste des utilisateurs.</p>
	 */
	private ArrayList<User> lesUsers = new ArrayList<User>();

	/**
	 * <h3>FileMesures theDAOMesures</h3>
	 * <p>Représente l'accès aux mesures via le DAO (Data Access Object).</p>
	 */
	private FileMesures theDAOMesures;
	
	/**
	 * <h3>User theUser</h3>
	 * <p>Représente l'utilisateur actuel.</p>
	 */
	private User theUser;
	
	/**
	 * <h3>Login theLogin</h3>
	 * <p>Représente l'objet gérant le processus de connexion (login)</p>
	 */
    private Login theLogin;
    
    /**
     * <h3>ConsoleGUI theConsole</h3>
     * <p>Représente l'interface graphique de la console.</p>
     */
    private ConsoleGUI theConsole;
    
    /**
     * <h3>MDPChange theMDPChange</h3>
     * <p>Représente l'objet gérant le changement du mot de passe.</p>
     */
    private MDPChange theMDPChange;
    
    /**
     * <h3>Alert theAlert</h3>
     * <p>Représente l'objet gérant l'envoi d'un message d'alerte.</p>
     */
    private Alert theAlert;

	/**
     * <h3>DAOmySQL myDAOmySQL</h3>
     * <p>Représente l'objet DAO pour l'accès aux données via MySQL.</p>
     */
    private DAOmySQL myDAOmySQL;
    
    /**
     * <h3>DAOmySQL myDAOmySQL</h3>
     * <p>Représente l'objet SMSSender permettant d'envoyer un message d'alerte.</p>
     */
    private SMSSender mySMSSender;

	/**
     * <h3>ResultSet userSet</h3>
     * <p>Représente le jeu de résultats (ResultSet) associé aux utilisateurs.</p>
     */
    private ResultSet userSet;

	// Implementation
    /**
     * <h2>Constructeur de la classe Controller.<h2>
     * <p>Initialise les différentes composantes du contrôleur, y compris la lecture des mesures depuis le fichier,
     * l'accès à la base de données MySQL, la création des utilisateurs, et l'initialisation de l'interface graphique.</p>
     *
     * @throws ParseException si une erreur de conversion de chaîne se produit pendant la lecture des mesures.
     * @throws SQLException si une erreur SQL survient lors de l'accès à la base de données MySQL.
     */
    public Controller() throws ParseException, SQLException {
    	
    	this.theDAOMesures = new FileMesures(this);
    	this.myDAOmySQL = new DAOmySQL(this);
    	theDAOMesures.readDB();
    	
    	this.userSet = myDAOmySQL.listUsers();
    	
    	while(userSet.next()) {
    		this.theUser = new User(userSet.getString("login"), userSet.getString("password"));
    		this.lesUsers.add(theUser);
    	}
    	
    	this.theLogin = new Login(this);
    	this.theLogin.setBounds(100, 100, 450, 300);
    	this.theLogin.setVisible(true);
    	
    	this.theMDPChange = new MDPChange(this);
    	this.theAlert = new Alert(this);
    	this.mySMSSender = new SMSSender(this);
    	
    	this.theConsole = new ConsoleGUI(this);
    }

	/**
	 * <p>
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * </p>
	 * <ol>
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)</li>
	 * </ol>
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout à laSelection des objets qui correspondent aux paramètres
		// Envoi de la collection
		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : lesMesures) {
			if (laZone.compareTo("*") == 0) {
				laSelection.add(mesure);
			} else {
				if (laZone.compareTo(mesure.getNumZone()) == 0) {
					laSelection.add(mesure);
				}
			}
		}
		return laSelection;
	}

	/**
	 * <p>
	 * Retourne la collection des mesures
	 * </p>
	 * 
	 * @return ArrayList<Mesure>
	 */
	public ArrayList<Mesure> getLesMesures() {

		return lesMesures;
	}

	/**
	 * <p>Convertion d'une String en Date</p>
	 * 
	 * @param strDate
	 * @return Date
	 * @throws ParseException
	 * @throws SQLException 
	 */
	public void showConsole() throws ParseException, SQLException {
	    // Construit et affiche l'IHM
	    ConsoleGUI monIHM = new ConsoleGUI(this);
	    monIHM.setLocation(100, 100);

	    // Instancie un contrôleur pour prendre en charge l'IHM
	    Controller control = new Controller(); // Déclarez une variable Controller

	    // Demande l'acquisition des données
	    ArrayList<Mesure> lesMesures = control.getLesMesures(); // Utilisez la variable control déclarée ci-dessus

	    // Construit le tableau d'objet
	    JTable laTable = monIHM.setTable(lesMesures); // Appelez setTable depuis monIHM

	    // Définissez le JScrollPane qui va recevoir la JTable
	    monIHM.scrollPane.setViewportView(laTable);

	    System.out.println("Before set chart in main()");
	    // Affiche le graphique
	    monIHM.setChart();
	    System.out.println("After set chart in main()");
	    monIHM.setVisible(true);
	}

	/**
	 * <h3>public void setLesMesures(ArrayList<Mesure> lesMesures)</h3>
	 * <p>Définit la liste des mesures.</p>
	 *
	 * @param lesMesures La nouvelle liste des mesures.
	 */
	public void setLesMesures(ArrayList<Mesure> lesMesures) {
	    this.lesMesures = lesMesures;
	}

	/**
	 * <h3>public ArrayList<User> getLesUsers()</h3>
	 * <p>Récupère la liste des utilisateurs.</p>
	 *
	 * @return La liste des utilisateurs.
	 */
	public ArrayList<User> getLesUsers() {
	    return lesUsers;
	}

	/**
	 * <h3>public void setLesUsers(ArrayList<User> lesUsers)</h3>
	 * <p>Définit la liste des utilisateurs.</p>
	 *
	 * @param lesUsers La nouvelle liste des utilisateurs.
	 */
	public void setLesUsers(ArrayList<User> lesUsers) {
	    this.lesUsers = lesUsers;
	}

	/**
	 * <h3>public User getTheUser()</h3>
	 * <p>Récupère l'utilisateur actuel.</p>
	 *
	 * @return L'utilisateur actuel.
	 */
	public User getTheUser() {
	    return theUser;
	}

	/**
	 * <h3>public void setTheUser(User theUser)</h3>
	 * <p>Définit l'utilisateur actuel.</p>
	 *
	 * @param theUser Le nouvel utilisateur.
	 */
	public void setTheUser(User theUser) {
	    this.theUser = theUser;
	}

	/**
	 * <h3>public ConsoleGUI getTheConsole()</h3>
	 * <p>Récupère l'interface graphique de la console.</p>
	 *
	 * @return L'interface graphique de la console.
	 */
	public ConsoleGUI getTheConsole() {
	    return theConsole;
	}

	/**
	 * <h3>public void setTheConsole(ConsoleGUI theConsole)</h3>
	 * <p>Définit l'interface graphique de la console.</p>
	 *
	 * @param theConsole La nouvelle interface graphique de la console.
	 */
	public void setTheConsole(ConsoleGUI theConsole) {
	    this.theConsole = theConsole;
	}

	/**
	 * <h3>public Login getTheLogin()</h3>
	 * <p>Récupère l'objet de connexion (login).</p>
	 *
	 * @return L'objet de connexion (login).
	 */
	public Login getTheLogin() {
	    return theLogin;
	}

	/**
	 * <h3>public void setTheLogin(Login theLogin)</h3>
	 * <p>Définit l'objet de connexion (login).</p>
	 *
	 * @param theLogin Le nouvel objet de connexion (login).
	 */
	public void setTheLogin(Login theLogin) {
	    this.theLogin = theLogin;
	}
	
	/**
	 * <h3>public MDPChange getTheMDPChange()</h3>
	 * <p>Récupère l'objet de changement de mot de passe.</p>
	 * 
	 * @return L'objet de changement de mot de passe.
	 */
	public MDPChange getTheMDPChange() {
		return theMDPChange;
	}

	/**
	 * <h3>public void setTheMDPChange(MDPChange theMDPChange)</h3>
	 * <p>Définit l'objet de changement de mot de passe.</p>
	 * 
	 * @param theMDPChange Le nouvel objet de changement de mot de passe
	 */
	public void setTheMDPChange(MDPChange theMDPChange) {
		this.theMDPChange = theMDPChange;
	}
	
	/**
	 * <h3>public DAOmySQL getMyDAOmySQL()</h3>
	 * <p>Récupère l'objet DAO pour l'accès aux données via MySQL.</p>
	 *
	 * @return L'objet DAO pour l'accès aux données via MySQL.
	 */
	public DAOmySQL getMyDAOmySQL() {
	    return myDAOmySQL;
	}

	/**
	 * <h3>public void setMyDAOmySQL(DAOmySQL myDAOmySQL)</h3>
	 * <p>Définit l'objet DAO pour l'accès aux données via MySQL.</p>
	 *
	 * @param myDAOmySQL Le nouvel objet DAO pour l'accès aux données via MySQL.
	 */
	public void setMyDAOmySQL(DAOmySQL myDAOmySQL) {
	    this.myDAOmySQL = myDAOmySQL;
	}
	
	/**
	 * <h3>public Alert getTheAlert()</h3>
	 * <p>Récupère l'objet Alert.</p>
	 *
	 * @return l'objet Alert
	 */
	public Alert getTheAlert() {
		return theAlert;
	}

	/**
	 * <h3>public void setTheAlert(Alert theAlert)</h3>
	 * <p>Définit l'objet SMSSender, qui permet d'envoyer un message d'alerte.</p>
	 *
	 * @param theAlert l'objet SMSSender permettant d'envoyer un message d'alerte
	 */
	public void setTheAlert(Alert theAlert) {
		this.theAlert = theAlert;
	}
	
	/**
	 * <h3>public SMSSender getMySMSSender()</h3>
	 * <p>Définit l'objet SMSSender, qui permet d'envoyer un message d'alerte.</p>
	 *
	 * @return
	 */
	public SMSSender getMySMSSender() {
		return mySMSSender;
	}

	/**
	 * <h3>public void setMySMSSender(SMSSender mySMSSender)</h3>
	 * <p>Définit l'objet SMSSender, qui permet d'envoyer un message d'alerte.</p>
	 *
	 * @param mySMSSender
	 */
	public void setMySMSSender(SMSSender mySMSSender) {
		this.mySMSSender = mySMSSender;
	}
}