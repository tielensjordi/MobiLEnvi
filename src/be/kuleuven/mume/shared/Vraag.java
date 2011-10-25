package be.kuleuven.mume.shared;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Vraag{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private Vak vak;
	@Persistent
	private String fromPersoon;
	@Persistent
	private String text;
	@Persistent
	private Date date;
	@Persistent(mappedBy = "vraag")
	private List<Antwoord> antwoorden;

	public Vraag(Persoon fromPersoon, Vak vak, String text){
		this.setFromPersoon(fromPersoon.getNickName());
		this.vak = vak;
		this.text = text;
		this.antwoorden = new ArrayList<Antwoord>();
		this.date = Calendar.getInstance().getTime();
	}
	
	public void setId(Key id) {
		this.id = id;
	}

	public Key getId() {
		return id;
	}

	public void setVak(Vak vak) {
		this.vak = vak;
	}

	public Vak getVak() {
		return vak;
	}
	
	public void setFromPersoon(String fromPersoon) {
		this.fromPersoon = fromPersoon;
	}

	public String getFromPersoon() {
		return fromPersoon;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void addAntwoord(Antwoord a)
	{
		this.antwoorden.add(a);
	}
	public void setAntwoorden(List<Antwoord> antwoorden) {
		this.antwoorden = antwoorden;
	}

	public List<Antwoord> getAntwoorden() {
		return antwoorden;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("From: ");
		str.append(this.fromPersoon);
		str.append(" Vraag: ");
		str.append(this.getText());
		str.append("\nAntwoorden:\n");
		for (Antwoord a : this.antwoorden) {
			str.append("\t");
			str.append(a.toString());
			str.append("\n");
		}
		return str.toString();
	}
}
