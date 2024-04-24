package DAO;

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

import model.Mesure;
import control.Controller;

public class FileMesures {

	// Specifications
	private Controller myController;
	
	// Implementation
	/**
	 * <h3>public FileMesures(Controller aController)</h3>
     * <p>Constructeur de la classe FileMesures.</p>
     * 
     * @param aController Le contrôleur {@code Controller} associé à la gestion des mesures.
     */
	public FileMesures(Controller aController) {
		super();
		this.myController = aController;
	}
	
	/**
	 * <h3>public void readDB() throws ParseException, SQLException</h3>
	 * <p>Lit un fichier de type CSV (Comma Separated Values)</p>
	 * <p>Le fichier contient les mesures de temp&eacute;rature de la pelouse.</p>
	 * 
	 * @throws ParseException
	 * @throws SQLException 
	 * @since 2.0.0
	 */
	public void readDB() throws ParseException, SQLException {

		ResultSet resultSet = myController.getMyDAOmySQL().listMeasures();

		String numZone = null;
		Date horoDate = null;
		float fahrenheit;

		while (resultSet.next()) {
			
			// Affecte les champs aux param�tre du constructeur de
			// mesure
			numZone = String.valueOf("0" + resultSet.getInt("zone"));
			horoDate = strToDate(resultSet.getString("date_measure"));
			fahrenheit = resultSet.getFloat("temperature");

			// Instancie une Mesure
			Mesure laMesure = new Mesure(numZone, horoDate, fahrenheit);
			myController.getLesMesures().add(laMesure);
			
		}
	}
	
	/**
	 * <p>La méthode strToDate convertit une représentation de chaîne de date en un objet Date. Elle utilise un objet SimpleDateFormat pour analyser la chaîne de date dans le format spécifié.</p>
	 * 
	 * @param strDate La représentation de chaîne de la date à convertir.
	 * @return Un objet Date représentant la date convertie.
	 * @throws ParseException Si la conversion de la chaîne en date échoue en raison d'un format incorrect.
	 */
	private Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("yy-MM-dd kk:mm");

		laDate = leFormat.parse(strDate);
		return laDate;
	}

}
