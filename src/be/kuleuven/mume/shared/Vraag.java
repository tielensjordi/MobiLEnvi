package be.kuleuven.mume.shared;

import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Vraag {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key vraagId;
	@Persistent
	private Persoon fromPersoon;
	@Persistent
	private String text;
	@Persistent(dependent = "true")
	private Vak vak;
	@Persistent
	private Date date;
	@Persistent
	private Vraag replyTo;
	
	public Vraag(){
		this.date = Calendar.getInstance().getTime();
	}

	public void setVraagId(Key vraagId) {
		this.vraagId = vraagId;
	}

	public Key getVraagId() {
		return vraagId;
	}
	public void setFromUser(Persoon fromPersoon) {
		this.fromPersoon = fromPersoon;
	}
	public Persoon getFromUser() {
		return fromPersoon;
	}
	public void setText(String vraag) {
		this.text = vraag;
	}
	public String getText() {
		return text;
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
	
	public boolean isReply(){
		if(this.replyTo != null)
			return true;
		return false;
	}

	public void setReplyTo(Vraag replyTo) {
		this.replyTo = replyTo;
	}

	public Vraag getReplyTo() {
		return replyTo;
	}
}
