package be.kuleuven.mume.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import be.kuleuven.mume.shared.Persoon;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5978140401583941374L;
	
	
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	//Check if user is logged in.
    	Persoon p = Persoon.getCurrentPersoon(req,resp,true);
    	if(null==p)
    		return;
    	
    	resp.setContentType("text/plain");
    	
    	//TweetFactory tf = new TweetFactory();
    	
    	//resp.getWriter().print((new JSONObject(p)).toString());
    	//resp.getWriter().print(tf.bySearchTerm(req.getParameter("searchTerm"), 1).toString());
    	//resp.getWriter().print(Tweets.getRecentTweets(1).toString());
        
       resp.getWriter().println(p.getGoogleId());
       
	}


}
