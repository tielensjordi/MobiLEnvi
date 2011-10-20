package be.kuleuven.mume.social.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Tweets extends ArrayList<Tweet>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5145116698874733216L;
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Tweet t : this) {
			b.append(t.toString() + "\n");
		}
		return b.toString();
	}
	
	public static Tweets getRecentTweets(int page){
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
	    // The factory instance is re-useable and thread safe.
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer("Sx53PNSwLsq3ifCn7ylbBw", "JiPdcDv7d206jpeKCZIgZOmqIMZbidSM9REGfq44");
	    Tweets tweets = new Tweets();
	    List<Status> statuses;
		try {
			statuses = twitter.getHomeTimeline();
			
		    log.log(Level.INFO, "Showing friends timeline.");
		    
		    for (Status status : statuses) {
		    	tweets.add(new Tweet(status.getUser().getName(), status.getText(), status.getUser().getProfileImageURL()));
		    }
		    
		    
		} catch (TwitterException e) {
			log.log(Level.SEVERE, e.toString());
		}
	    
	    return tweets;
	}
}
