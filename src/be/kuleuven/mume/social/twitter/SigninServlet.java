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

import be.kuleuven.mume.Google;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

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
		if(!Google.checkUserLogin(req,resp))
			return;
		
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
			//keep requestToken for creating the acces token.
			requestToken = twitter.getOAuthRequestToken(callbackUrl);

		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		    Entity rtoken = new Entity("OAuthTemp",requestToken.getToken());
		    rtoken.setProperty("rtoken", convertToBlob(requestToken));
		    rtoken.setProperty("UserId", Google.getUser().getUserId());
		    
		    log.log(Level.INFO, requestToken.toString());
		    datastore.put(rtoken);
	        
			//redirect user to login page
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
