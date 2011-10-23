package be.kuleuven.mume.social.twitter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.InheritanceStrategy;

import com.google.appengine.api.datastore.Key;

import twitter4j.Tweet;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class LocalTweet {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key tweetId;
    @Persistent
    private String fromUser;
    //@Persistent
    //private long fromUserId;
    @Persistent
    private String text;
    @Persistent
    private String image_url;
    @Persistent
    private Date createdAt;
    @Persistent
    private String toUser;
    //@Persistent
    //private long toUserId;
    @Persistent
    private String location;
    
	public LocalTweet(Tweet t) {
		try{
	    this.fromUser = t.getFromUser();
	    //this.setFromUserId(t.getFromUserId());
	    this.text = t.getText();
	    this.image_url = t.getProfileImageUrl();
	    this.setCreatedAt(t.getCreatedAt());
	    this.setToUser(t.getToUser());
	    //this.setToUserId(t.getToUserId());
	    this.setLocation(t.getLocation());
		}
		catch(Exception e){
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			log.log(Level.SEVERE, "LocalTweet creation failed" + e.toString());
		}
	  }
	  
	public LocalTweet(){
		
	}
	
	public String toString(){
		return "Usr: " + this.fromUser + " Mess: " + this.text;
	}

	public Key getTweetId() {
		return tweetId;
	}

	/*public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public long getFromUserId() {
		return fromUserId;
	}*/

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getToUser() {
		return toUser;
	}

	/*public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public long getToUserId() {
		return toUserId;
	}*/

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

}

