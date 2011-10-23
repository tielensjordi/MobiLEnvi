package be.kuleuven.mume.social.twitter;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

public class TweetFactory {
	
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/*public LocalTweets getRecentTweets(int page){
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
	    // The factory instance is re-useable and thread safe.
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer("Sx53PNSwLsq3ifCn7ylbBw", "JiPdcDv7d206jpeKCZIgZOmqIMZbidSM9REGfq44");
	    
		try {
		    
		    
		} catch (TwitterException e) {
			log.log(Level.SEVERE, e.toString());
		}
	    
	    return tweets;
	}
	public LocalTweets bySearchTerm(String searchTerm, int page) {
		  Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		  tweets.clear();
		  
		  if(page<1)
			  return tweets;
		  
		  String searchUrl;
		  if(isBlank(searchTerm))
			  searchUrl = "https://api.twitter.com/1/statuses/public_timeline.json?count=1&include_entities=true&rpp=20&page=" + page;
		  else
			  searchUrl = "http://search.twitter.com/search.json?q=@"
		        + searchTerm + "&rpp=20&page=" + page;
		  
		  JSONObject jsonObject = null;
		  
		  try{
			  
			  URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
			  HTTPResponse resp = fetcher.fetch(new URL(searchUrl));  
		  
		    String obj =  new String(resp.getContent());
		    jsonObject = new JSONObject(obj);
		  
		  	}catch(Exception ex){
			    log.log(Level.WARNING,"Exception: " + ex.getMessage());
			}
		  
		  	return getTweets(jsonObject);
		}
	  
	  private boolean isBlank(String searchTerm) {
		if(searchTerm == null)
			return true;
		if(searchTerm.trim().equals(""))
			return true;
		return false;
	}
	public LocalTweets getTweets(JSONObject json){
		    
			  try {
				    Object j = json.get("results");
				    JSONArray a = (JSONArray)j;
				    JSONObject t;
				    
				    for(int i = 0 ; i < a.length() ; i++) {
						  t = (JSONObject)a.get(i);
						   
						  LocalTweet tweet = new LocalTweet(
								      t.get("from_user").toString(),
								      t.get("text").toString(),
								      new URL(((JSONObject)t).get("profile_image_url").toString())
								    );
						tweets.add(tweet);
				    }

			  } catch(Exception ex){
				  log.log(Level.WARNING, "Exception: " + ex.getMessage());
			  }
			   
			  return tweets;
	  }*/
}
