package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {

	
	public final String Database_name = "visio";
	public final String Database_user = "root";
	public final String Database_pass = "";
	
	public Connection con;
	
	public boolean init() throws SQLException {
		
		String lien="jdbc:mysql://localhost:3306/visio";
		String user="root";
		String pass="";
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				this.con = DriverManager.getConnection(lien,user,pass);
			} catch (SQLException e) {

				System.out.println("Error: Database Connection Failed ! Please check the connection Setting");

				return false;

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return false;
		}

		return true;
	}
	
	public void db_close() throws SQLException
	{
		try {
			con.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public ObservableList<String> getDate(String filiere) {
		
		ObservableList<String> list = FXCollections.observableArrayList();
		
		try {
			
			String sql = "select distinct dateP from presence where id_c in (select cours.id from cours where filiere=?) ";
			PreparedStatement prepare = null;
			prepare = (PreparedStatement) con.prepareStatement(sql);
			prepare.setString(1, filiere);
			
			ResultSet r = prepare.executeQuery();
			while(r.next()) {
				list.add(r.getString("dateP"));
			}
			
			System.out.println("la taille est :"+list.size());
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return list;
		}
		
		
		return list;
	}

	public ObservableList<String> getPeriode(String date) {
		
		ObservableList<String> list = FXCollections.observableArrayList();
		
		try {
			
			String sql = "select distinct periode from presence where dateP=? ";
			PreparedStatement prepare = null;
			prepare = (PreparedStatement) con.prepareStatement(sql);
			prepare.setString(1, date);
			
			ResultSet r = prepare.executeQuery();
			while(r.next()) {
				list.add(r.getString("periode"));
			}
			
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return list;
		}
		
		
		return list;
		
		
	}

	public ObservableList<Etudiant> getPresence(String filiere, String periode, String date) {
		
		ObservableList<Etudiant> list = FXCollections.observableArrayList();
		
		String req = " select p.matricule_e, f.first_name, f.last_name, f.filiere, c.nom_C from presence p, face_bio f, cours c where p.matricule_e=f.matricule and p.id_c=c.id and f.filiere=? and p.dateP=? and p.periode=? ";
		
		PreparedStatement prepare = null;
		try 
		{
			prepare = (PreparedStatement) con.prepareStatement(req);
			prepare.setString(1, filiere);
			prepare.setString(2, date);
			prepare.setString(3, periode);
			
			ResultSet r = prepare.executeQuery();
			
			while(r.next()) 
			{
				String mat = r.getString("matricule_e");
				String nom = r.getString("first_name");
				String prenom= r.getString("last_name");
				String fil = r.getString("filiere");
				String cours = r.getString("nom_C");
				Etudiant etudiant = new Etudiant(mat.toUpperCase(),nom.toUpperCase(),prenom,fil,cours);
				
				list.add(etudiant);
				System.out.println("Etudiant trouvé : "+cours);
			}
			
			return list;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return list;
		}
	}
	
	
	public ObservableList<EtudiantAbs> getAbscence(String filiere, String periode, String date) {
		
		ObservableList<EtudiantAbs> list = FXCollections.observableArrayList();
		
		String req = " select f.matricule, f.first_name, f.last_name, f.filiere from face_bio f where f.filiere = ? and f.matricule not in ( select matricule_E from presence where dateP=? and periode=?) ";
		
		PreparedStatement prepare = null;
		try 
		{
			prepare = (PreparedStatement) con.prepareStatement(req);
			prepare.setString(1, filiere);
			prepare.setString(2, date);
			prepare.setString(3, periode);
			
			ResultSet r = prepare.executeQuery();
			
			while(r.next()) 
			{
				String mat = r.getString("matricule");
				String nom = r.getString("first_name");
				String prenom= r.getString("last_name");
				String fil = r.getString("filiere");
				String cours = "";
				EtudiantAbs etudiant = new EtudiantAbs(mat.toUpperCase(),nom.toUpperCase(),prenom,fil);
				
				list.add(etudiant);
				
			}
			
			return list;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return list;
		}
	}
		
	
	
	
	
}
