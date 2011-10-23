package be.kuleuven.mume.shared;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Vraag {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key vraagId;
	@Persistent
	private User fromUser;
	@Persistent
	private String vraag;
	@Persistent
	private Vak vak;
	@Persistent
	private Date date;
	
	public void setVraagId(Key vraagId) {
		this.vraagId = vraagId;
	}
	public Key getVraagId() {
		return vraagId;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	public User getFromUser() {
		return fromUser;
	}
	public void setVraag(String vraag) {
		this.vraag = vraag;
	}
	public String getVraag() {
		return vraag;
	}
	public void setVak(Vak vak) {
		this.vak = vak;
	}
	public Vak getVak() {
		return vak;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
}
