package loginandsignup;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionDB {
    //methode de connexion
public static Connection getConnection() throws SQLException {
        // Informations de connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/gestion_artisant"; 
        String user = "root"; // Votre utilisateur MySQL (par défaut : root)
        String password = "root"; // Votre mot de passe MySQL (vide par défaut sur WAMP)

        // Tentative de connexion
       return DriverManager.getConnection(url, user, password);
    }
public static void main(String[] args) {
    try {
        Connection conn = getConnection();
        System.out.println("Connexion établie avec succès !");
        conn.close(); // Fermer la connexion après utilisation
    } catch (SQLException e) {
        System.out.println("Erreur de connexion : " + e.getMessage());
    }
}

        
    }

