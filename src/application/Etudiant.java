package application;

public class Etudiant {

	public String matricule;
	public String nom;
	public String prenom;
	public String filiere;
	public String Nomcours;
	
	public Etudiant(String matricule, String nom, String prenom, String filiere, String cour) {
		
		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
		this.filiere = filiere;
		this.Nomcours = cour;
	}

	public String getNomcours() {
		return Nomcours;
	}

	public void setNomcours(String nomcours) {
		Nomcours = nomcours;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getFiliere() {
		return filiere;
	}

	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}
	
	
	
}
