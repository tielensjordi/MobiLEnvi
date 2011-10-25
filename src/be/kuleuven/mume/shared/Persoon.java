package be.kuleuven.mume.shared;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@PersistenceCapable
public class Persoon {
	@PrimaryKey
	@Persistent
	//(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key googleId;
	@NotPersistent
	private PersistenceManager pm;
	@Persistent
	private String nickName;
	@Persistent
	private int leeftijd;
	@Persistent(serialized = "true")
	private AccessToken twitterToken;
	@NotPersistent
	private List<Vraag> vragen;
	@NotPersistent
	private List<Vak> vakken;
	@Persistent
	private List<Key> vragenId;
	@Persistent
	private List<Key> vakkenId;
	@NotPersistent
	Logger log;
	
	public Persoon(){	
		this.vragen = new ArrayList<Vraag>();
		this.vakken = new ArrayList<Vak>();
		this.vragenId = new ArrayList<Key>();
		this.vakkenId = new ArrayList<Key>();
		log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	public static Persoon getCurrentPersoon(PersistenceManager pm){
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String googleId = user.getUserId();
		Persoon p = null;
		Key key = KeyFactory.createKey(Persoon.class.getSimpleName(), googleId);
		try {
			//If existing use object Persoon from db.
			p = pm.getObjectById(Persoon.class, key);
		} catch (Exception e) {
			log.log(Level.WARNING,
					"New Persoon will be added to the db" + e.getMessage());
		}

		if (p == null) {
			p = new Persoon();
			p.setGoogleId(key);//Set GoogleId as PK
			p.setPm(pm);
			p.nickName = user.getNickname();
			pm.makePersistent(p);
		}
		pm.flush();
		
		return p;
	}
	public static Persoon getCurrentPersoon(PersistenceManager pm, HttpServletRequest req, HttpServletResponse resp, boolean redirectToGoogleLogin) throws IOException
	{	
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		//check if a user is logged in.
		//redirect to login page if not yet logged in.
		if(user == null)
		{
        	if(redirectToGoogleLogin)
        		resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
            return null;
		}
		
		return getCurrentPersoon(pm);

	}
	
	public void setPm(PersistenceManager pm){
		this.pm = pm;
	}
	
	public void setNickName(String name) {
		this.nickName = name;
	}

	public String getNickName() {
		return nickName;
	}

	public Twitter getTwitter(){
		Twitter twitter = null;
		if (this.twitterToken != null) {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("Sx53PNSwLsq3ifCn7ylbBw",
					"JiPdcDv7d206jpeKCZIgZOmqIMZbidSM9REGfq44");
			twitter.setOAuthAccessToken(this.twitterToken);
		}
		return twitter;
	}

	public Key getGoogleId() {
		return googleId;
	}

	public void setGoogleId(Key googleId) {
		this.googleId = googleId;
	}

	public void setLeeftijd(int lt) {
		this.leeftijd = lt;
	}

	public int getLeeftijd() {
		return this.leeftijd;
	}

	public void setTwitterToken(AccessToken twitterToken) {
		this.twitterToken = twitterToken;
	}

	public AccessToken getTwitterToken() {
		return twitterToken;
	}
	
	public void addVraag(Vraag v){
		this.vragen.add(v);
	}
	
	public void setVragen(List<Vraag> vragen) {
		//Idd keys to sepperate list. These will be stored
		for (Vraag vraag : vragen) {
			this.vragenId.add(vraag.getId());
		}
		this.vragen = vragen;
	}

	@SuppressWarnings("unchecked")
	public List<Vraag> getVragen() {
		if(this.vragen.isEmpty())
		{
			Query q = pm.newQuery(Vraag.class);
		    q.setFilter("fromPersoon == fromPersoonParam");
		    q.setOrdering("date desc");
		    q.declareParameters("Persoon fromPersoonParam");
		    this.vragen = (List<Vraag>)q.execute(this);
		}
		return vragen;
	}

	public void setVakken(List<Vak> vakken) {
		System.out.println("needs to be implemented");
		log.log(Level.SEVERE,"Needs to be implemented");
		this.vakken = vakken;
	}

	public List<Vak> getVakken() {
		System.out.println("needs to be implemented");
		log.log(Level.SEVERE,"Needs to be implemented");
		return vakken;
	}

	public void setVragenId(List<Key> vragenId) {
		this.vragenId = vragenId;
	}

	public List<Key> getVragenId() {
		return vragenId;
	}

	public void setVakkenId(List<Key> vakkenId) {
		this.vakkenId = vakkenId;
	}

	public List<Key> getVakkenId() {
		return vakkenId;
	}
}
