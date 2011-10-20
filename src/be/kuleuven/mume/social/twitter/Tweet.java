package be.kuleuven.mume.social.twitter;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

public class Tweet {
	  public String username;
	  public String message;
	  public URL image_url;
	   
	  public Tweet(String username, String message, URL url) {
	    this.username = username;
	    this.message = message;
	    this.image_url = url;
	  }
	  
	  public String toString(){
		return "Usr: " + username + " Mess: " + message;
	  }
	  
	  public static Tweets getTweets(String searchTerm, int page) {
		  Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		  Tweets tweets = new Tweets();
		  
		  if(page<1)
			  return tweets;
		  
		  String searchUrl;
		  if(searchTerm=="null")
			  searchUrl = "https://api.twitter.com/1/statuses/public_timeline.json?count=1&include_entities=true";
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
		  
		    ArrayList<Object> arr = new ArrayList<Object>();
		    
		  try {
		    Object j = jsonObject.get("results");
		    JSONArray a = (JSONArray)j;
		    
		    for(int i = 0 ; i < a.length() ; i++) {
				  arr.add(a.get(i));
		    }

			for(Object t : arr) {
			    Tweet tweet = new Tweet(
			      ((JSONObject)t).get("from_user").toString(),
			      ((JSONObject)t).get("text").toString(),
			      new URL(((JSONObject)t).get("profile_image_url").toString())
			    );
			    tweets.add(tweet);
			  }
		  } catch(Exception ex){
			  log.log(Level.WARNING,jsonObject.names().toString() + "Exception: " + ex.getMessage());
		  }
		   
		  return tweets;
		}

	}

