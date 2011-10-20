package be.kuleuven.mume.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.shared.Persoon;
import be.kuleuven.mume.social.twitter.TweetFactory;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5978140401583941374L;
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	resp.setContentType("text/plain");
    	Persoon p = new Persoon();
    	TweetFactory tf = new TweetFactory();
    	p.setLeeftijd(30);
    	//resp.getWriter().print((new JSONObject(p)).toString());
    	resp.getWriter().print(tf.bySearchTerm(req.getParameter("searchTerm"), 1).toString());
    	//resp.getWriter().print(Tweets.getRecentTweets(1).toString());
        
    	
    	/*PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(someobject);
        } finally {
            pm.close();
        }
    	*/
	}


}
