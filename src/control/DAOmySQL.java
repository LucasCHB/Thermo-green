package control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.jasypt.util.text.BasicTextEncryptor;

public class DAOmySQL {

	// Specification
	private Properties theConfig;
	private BasicTextEncryptor theEncryptor;
	
	Controller myController;
	String URL;
	String username;
	String password;
	Connection myConnection;
	Statement myStatement;
	
	public DAOmySQL(Controller aController) throws SQLException {
		this.myController = aController;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		this.theEncryptor = new BasicTextEncryptor();
		this.theEncryptor.setPassword("P@ssw0rd");
		
		this.theConfig = new Properties();
		
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("data/vtg.cfg")) {
		    theConfig.load(input);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		this.URL = theEncryptor.decrypt(theConfig.getProperty("db.url"));
		
		this.username = theEncryptor.decrypt(theConfig.getProperty("db.username"));
		this.password = theEncryptor.decrypt(theConfig.getProperty("db.password"));
		
		this.myConnection = DriverManager.getConnection(URL, username, password);
		
		this.myStatement = myConnection.createStatement();
		
	}
	
	/**
	 * Récupère la liste de tous les utilisateurs de la base de données.
	 * @return ResultSet contenant la liste des utilisateurs.
	 * @throws SQLException si une erreur survient lors de l'exécution de la requête SQL.
	 */
	public ResultSet listUsers() throws SQLException {
		String theQuery = "SELECT * FROM user;";
		ResultSet theResultSet = myStatement.executeQuery(theQuery);
		return theResultSet;
	}
	
	public ResultSet listStadiumNames() throws SQLException {
		String theQuery = "SELECT name FROM stadium;";
		ResultSet theResultSet = myStatement.executeQuery(theQuery);
		return theResultSet;
	}
	
	public ResultSet listMeasures() throws SQLException {
		String theQuery = "SELECT * FROM measures;";
		ResultSet theResultSet = myStatement.executeQuery(theQuery);
		return theResultSet;
	}
	
	public void changePWD(String login, String pwd) throws SQLException {
	    String theQuery = "UPDATE user SET password = ? WHERE login = ?";
	    
	    try (PreparedStatement preparedStatement = myConnection.prepareStatement(theQuery)) {
	        preparedStatement.setString(1, hashPassword(pwd));
	        preparedStatement.setString(2, login);
	        
	        int rowsAffected = preparedStatement.executeUpdate();
	        
	        if (rowsAffected > 0) {
	        	// Le mot de passe a été changé avec succès
	            System.out.println("Mot de passe changé avec succès ! Redémarrez pour appliquer les changements.");
	        } else {
	            System.err.println("Aucune ligne mise à jour. Utilisateur non trouvé.");
	        }
	    }
	}

	
	private static String hashPassword(String motDePasse) {
        // Génération d'un sel aléatoire (par défaut, BCrypt utilise un coût de 10)
        String salt = BCrypt.gensalt();

        // Hashage du mot de passe avec le sel
        return BCrypt.hashpw(motDePasse, salt);
    }
}