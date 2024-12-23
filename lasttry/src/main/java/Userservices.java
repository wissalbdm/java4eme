

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;


public class Userservices {
    public void registerUser(String username, String password, String email, String role,String additionalInfo) {
    String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        // Inscription de l'utilisateur dans la table users
        stmt.setString(1, username);
        stmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); // Hachage du mot de passe
        stmt.setString(3, email);
        stmt.setString(4, role);

        stmt.executeUpdate();

        // Récupérer l'ID de l'utilisateur inséré
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int userId = rs.getInt(1);

            // Ajouter l'utilisateur dans la table spécifique en fonction du rôle
            if ("clients".equals(role)) {
                registerClient(userId, additionalInfo);
            } else if ("artisants".equals(role)) {
                registerArtisan(userId, additionalInfo);
            } else if ("admin".equals(role)) {
                registerAdmin(userId, additionalInfo);
            }

            System.out.println("Utilisateur inscrit avec succès !");
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de l'inscription : " + e.getMessage());
    }
}

private void registerClient(int userId, String additionalInfo) throws SQLException {
    String sql = "INSERT INTO clients (Nom, Prenom, Email,Tele,Adresse) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String[] info = additionalInfo.split(",");
        stmt.setInt(1, userId);
        stmt.setString(2, info[0]);  // first_name
        stmt.setString(3, info[1]);  // last_name
        stmt.setString(4, info[2]);
        stmt.setString(5, info[3]);
        stmt.setString(6, info[4]);
        stmt.executeUpdate();
    }
}

private void registerArtisan(int userId, String additionalInfo) throws SQLException {
    String sql = "INSERT INTO artisants (Nom, Prenom, Email,Tele,Categorie) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String[] info = additionalInfo.split(",");
        stmt.setInt(1, userId);
        stmt.setString(2, info[0]);  // first_name
        stmt.setString(3, info[1]);  // last_name
        stmt.setString(4, info[2]);
        stmt.setString(5, info[3]);
        stmt.setString(6, info[4]);  // specialty
        stmt.executeUpdate();
    }
}

private void registerAdmin(int userId, String additionalInfo) throws SQLException {
    String sql = "INSERT INTO admins (user_id, Nom, Email) VALUES (?, ?, ?)";
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String[] info = additionalInfo.split(",");
        stmt.setInt(1, userId);
        stmt.setString(2, info[0]);  // first_name
        stmt.setString(3, info[1]);  // last_name
        stmt.executeUpdate();
    }
}
public String loginUser(String email, String password) {
    String sql = "SELECT password, role FROM users WHERE email = ?";
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (BCrypt.checkpw(password, hashedPassword)) {
                    System.out.println("Connexion réussie en tant que : " + role);
                    return role;
                } else {
                    System.out.println("Mot de passe incorrect !");
                }
            } else {
                System.out.println("Utilisateur introuvable !");
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la connexion : " + e.getMessage());
    }
    return null;
}

    
}
