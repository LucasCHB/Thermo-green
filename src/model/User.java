package model;

/**
 * La classe User représente un utilisateur avec un login et un mot de passe.
 */
public class User {
    // Specifications
    private String login;
    private String password;

    /**
     * <p>Constructeur par défaut de la classe User.</p>
     */
    public User() {
    }

    /**
     * <p>Constructeur avec paramètres de la classe User.</p>
     *
     * @param aLogin Le login de l'utilisateur.
     * @param aPassword Le mot de passe de l'utilisateur.
     */
    public User(String aLogin, String aPassword) {
        this.login = aLogin;
        this.password = aPassword;
    }

    /**
     * <p>Obtient le login de l'utilisateur.</p>
     *
     * @return Le login de l'utilisateur.
     */
    public String getLogin() {
        return login;
    }

    /**
     * <p>Modifie le login de l'utilisateur.</p>
     *
     * @param login Le nouveau login de l'utilisateur.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * <p>Obtient le mot de passe de l'utilisateur.</p>
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Modifie le mot de passe de l'utilisateur.</p>
     *
     * @param password Le nouveau mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
