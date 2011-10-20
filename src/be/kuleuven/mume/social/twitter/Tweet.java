package be.kuleuven.mume.social.twitter;

import java.net.URL;

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

	}

