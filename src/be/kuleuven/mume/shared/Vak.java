package be.kuleuven.mume.shared;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Extension;

import com.google.appengine.api.datastore.Key;

import twitter4j.Tweet;

import be.kuleuven.mume.social.twitter.LocalTweet;
import be.kuleuven.mume.social.twitter.VakTweet;

@PersistenceCapable
public class Vak {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key vakId;
	@Persistent
	private String name;
	@Persistent
	private String hashTag;
	@Persistent(mappedBy = "vak")
	@Element(dependent = "true")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="date asc"))
	private List<Vraag> vragen;
	@Persistent(mappedBy = "vak")
	@Element(dependent = "true")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="createdAt asc"))
	private List<VakTweet> tweets;
	
	public Vak(){
		name = "";
		hashTag = "";
		this.vragen = new ArrayList<Vraag>();
		this.tweets = new ArrayList<VakTweet>();
	}
	
	public void setVakId(Key vakId) {
		this.vakId = vakId;
	}
	public Key getVakId() {
		return vakId;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void addVraag(Vraag v)
	{
		vragen.add(v);
	}

	public void setVragen(List<Vraag> vragen) {
		this.vragen = vragen;
	}

	public List<Vraag> getVragen() {
		return vragen;
	}

	public String tweetsToString() {
		StringBuilder b = new StringBuilder();
		for (LocalTweet t : this.tweets) {
			b.append(t.toString() + "\n");
		}
		return b.toString();
	}
	public void addTweet(Tweet tweet){
		this.tweets.add(new VakTweet(tweet, this));
	}
	public void addTweet(VakTweet tweet) throws Exception{
		if(tweet.getVak() != null)
			throw new Exception("Cannot add a tweet from another Vak");
		this.tweets.add(tweet);
	}
	public void setTweets(List<VakTweet> tweets) {
		this.tweets = tweets;
	}

	public List<VakTweet> getTweets() {
		return tweets;
	}
}
