package application;

public class EtudiantAbs {

	
	public String matricule;
	public String nom;
	public String prenom;
	public String filiere;
	public EtudiantAbs(String matricule, String nom, String prenom, String filiere) {
	
		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
		this.filiere = filiere;
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
