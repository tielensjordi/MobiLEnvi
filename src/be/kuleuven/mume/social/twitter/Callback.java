package be.kuleuven.mume.social.twitter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.PMF;
import be.kuleuven.mume.shared.Persoon;
import be.kuleuven.mume.shared.Vak;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Callback extends HttpServlet {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5844389600588939068L;
		
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			//No Persoon check here.
			//TwitterServer will use this Servlet -> User checking will fail!
			
	    	resp.setContentType("text/plain");
	    	try {
	    		
	    		String oauth_token = req.getParameter("oauth_token");
	    		String verifier = req.getParameter("oauth_verifier");
		        // Get the temp object out of the datastore, and get the old RequestToken
		        // Yes, it MUST be the old RequestToken. Or at least, have all the same parameters
		        // as the old RequestToken.
	    	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    	    
		        Key k = KeyFactory.createKey("OAuthTemp", oauth_token);
				Entity temp = datastore.get(k);
		        RequestToken requestToken = (RequestToken) convertToObject((Blob)temp.getProperty("rtoken"));
		        Twitter twitter = (Twitter)convertToObject((Blob)temp.getProperty("twitter"));
		        String googleId = (String)temp.getProperty("UserId");
		        
		        if(requestToken == null)
		        {
		        	log.log(Level.SEVERE,"Callback: No requestToken attribute not found!;");
		        	return;
		        }
		        
	            AccessToken token = twitter.getOAuthAccessToken(requestToken, verifier);
	            
	            //verifyCredentials will throw exception if credentials are not correct. 
	            twitter.verifyCredentials();
		        
	            PersistenceManager pm = PMF.get().getPersistenceManager();
	            Key key = KeyFactory.createKey(Persoon.class.getSimpleName(), googleId);
	            Persoon persoon = pm.getObjectById(Persoon.class, key);
	            
	            //Updates of Person object will persist automatically
	        	persoon.setTwitterToken(token);//Add token to the user so the server can get access to twitter
	        	
	        	test(persoon, pm, req, resp);
	        	
	            pm.close();
	            datastore.delete(k);
	            log.log(Level.INFO, "Callback received");
	        } 
	    	catch (TwitterException e) {
	    		if(e.getStatusCode() == 401)
	    		{
	    			resp.getWriter().println("Twitter service or network is unavailable!");
	    		}
	    		
	    		log.log(Level.WARNING, "TwitterExep: " + e.getErrorMessage());
	        }
	        catch (EntityNotFoundException e) {
	        	log.log(Level.SEVERE, "Could not find the rToken in the database");
	        }
	        catch ( Exception e) {
	        	log.log(Level.SEVERE, e.getMessage());
	        }
	    	
	}
		
		public static void test(Persoon persoon,PersistenceManager pm, HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			try{
        		
        		Twitter twitter = persoon.getTwitter();
        		if(twitter == null)
        			throw new TwitterException("Twitter is null");
        		
	        	String until = req.getParameter("until");
	        	int page = 1; 
	        	boolean store = false;
	        	
	        	store = Boolean.parseBoolean(req.getParameter("store"));
        		try{
        			page = Integer.parseInt(req.getParameter("page"));
        		} catch(Exception e){}
        		
        		Query q = new Query("mume11");
        		q.setRpp(10);
        		q.setPage(page);
        		
        		try{
        			if(!until.equals("null")) q.until(until);
        		}catch(Exception e){}
        		
        		
        		QueryResult qResult = twitter.search(q);;
        		
        		Vak mume = new Vak();
        		mume.setName("Multimedia");
        		mume.setHashTag("#mume11");
        		for (Tweet tweet : qResult.getTweets()) {
					mume.addTweet(tweet);
				}
        		
        		for (int i = 0; i< mume.getTweets().size();i++) {
        			LocalTweet tweet = mume.getTweets().get(i);
        			resp.getWriter().println((i+page*10) + tweet.toString());
        		}
        		
        		if(store){
        			pm.makePersistent(mume);
        		}
        		
        		RateLimitStatus status = twitter.getRateLimitStatus();
        		log.log(Level.INFO, "Searches Remaining: "+ status.getRemainingHits());
        	}
        	catch(TwitterException e)
        	{
        		log.log(Level.SEVERE, "test" + e.toString() + e.getMessage());
        	}
			
		}

		private Object convertToObject(Blob b) throws IOException, ClassNotFoundException {
			ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes());
			ObjectInput in = new ObjectInputStream(bis);
			Object o = in.readObject();

			bis.close();
			in.close();
			
			return o;
		}

}
