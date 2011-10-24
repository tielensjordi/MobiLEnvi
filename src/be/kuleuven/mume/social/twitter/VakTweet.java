package be.kuleuven.mume.social.twitter;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import twitter4j.Tweet;

import be.kuleuven.mume.shared.Vak;

@PersistenceCapable
public class VakTweet extends LocalTweet {
	
	@Persistent
	private Vak vak;

	public VakTweet(Tweet tweet, Vak vak) {
		super(tweet);
		this.setVak(vak);
	}

	/*public VakTweet(Vak vak) {
		this.vak = vak;
	}*/

	public void setVak(Vak vak) {
		this.vak = vak;
	}

	public Vak getVak() {
		return vak;
	}
}
