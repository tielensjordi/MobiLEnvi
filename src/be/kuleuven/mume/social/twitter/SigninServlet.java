package be.kuleuven.mume.social.twitter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.PMF;
import be.kuleuven.mume.shared.Persoon;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

public class SigninServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6637360799707356074L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		//Every Servlet must check whether the Persoon has already signed in.
		Persoon persoon = Persoon.getCurrentPersoon(req,resp,true);
		if(persoon==null)
			return;
		
		if(persoon.getTwitter() != null)
		{
			
			Callback.test(persoon, PMF.get().getPersistenceManager(), req, resp);
			return;
		}
		//service will listen to twittercallback
		String scheme=req.getScheme();
		String serverName=req.getServerName();
		int serverPort = req.getServerPort();
		String callbackUrl = scheme+"://"+serverName+":"+serverPort + "/mobilenvi/twittercallback";
		log.log(Level.INFO, "CallbackUrl:" + callbackUrl);
		
		Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer("Sx53PNSwLsq3ifCn7ylbBw", "JiPdcDv7d206jpeKCZIgZOmqIMZbidSM9REGfq44");
	    
	    RequestToken requestToken;
	    
		try {
			//keep requestToken for creating the access token.
			requestToken = twitter.getOAuthRequestToken(callbackUrl);

		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		    UserService userService = UserServiceFactory.getUserService();
		    String googleId = userService.getCurrentUser().getUserId();
		    
		    Entity rtoken = new Entity("OAuthTemp",requestToken.getToken());
		    rtoken.setProperty("rtoken", convertToBlob(requestToken));
		    rtoken.setProperty("twitter", convertToBlob(twitter));
		    rtoken.setProperty("UserId", googleId);
		    
		    //log.log(Level.INFO, googleId);
		    datastore.put(rtoken);
	        
			//redirect user to twitterlogin page
			resp.sendRedirect(requestToken.getAuthorizationURL());
			
		} catch (TwitterException te) {
			if(401 == te.getStatusCode()){
				log.log(Level.SEVERE, "Twitter: Unable to get the access token.");
		    }else{
		    	log.log(Level.SEVERE, te.toString());
		    }
		}
	}
	private Blob convertToBlob(Object o) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);   
		out.writeObject(o);
		Blob result = new Blob(bos.toByteArray()); 

		out.close();
		bos.close();
		
		return result;
	}
}
