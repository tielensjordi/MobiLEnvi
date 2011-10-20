package be.kuleuven.mume.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Persoon {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key persoonId;
	@Persistent
	private int leeftijd;
	
	public Key getPersoonId(){
		return persoonId;
	}
	public void setLeeftijd(int lt) {
		this.leeftijd = lt;
	}
	
	public int getLeeftijd(){
		return this.leeftijd;
	}
}
