package be.kuleuven.mume.shared;

@Entity
public class Persoon {
	public int leeftijd;
	
	public void setLeeftijd(int lt) {
		this.leeftijd = lt;
	}
	
	public int getLeeftijd(){
		return this.leeftijd;
	}
}
