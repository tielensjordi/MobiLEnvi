package be.kuleuven.mume.shared;

import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Antwoord {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private Key fromPersoon;
	@Persistent
	private String text;
	@Persistent
	private Date date;
	@Persistent
	private Vraag vraag;
	
	public Antwoord(Persoon fromPersoon, String text, Vraag vraag){
		this.fromPersoon = fromPersoon.getGoogleId();
		this.text = text;
		this.date = Calendar.getInstance().getTime();
		this.vraag = vraag;
		vraag.addAntwoord(this);
	}
	public void setId(Key antwoordId) {
		this.id = antwoordId;
	}
	public Key getId() {
		return id;
	}
	public void setFromPersoon(Key fromPersoon) {
		this.fromPersoon = fromPersoon;
	}
	public Key getFromPersoon() {
		return fromPersoon;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setVraag(Vraag vraag) {
		this.vraag = vraag;
	}
	public Vraag getVraag() {
		return vraag;
	}
	public String toString(){
		return this.text;
	}
}
