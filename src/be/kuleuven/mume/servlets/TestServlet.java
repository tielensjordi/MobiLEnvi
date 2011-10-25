package be.kuleuven.mume.servlets;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.PMF;
import be.kuleuven.mume.shared.Persoon;
import be.kuleuven.mume.shared.Vak;
import be.kuleuven.mume.shared.Vraag;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5978140401583941374L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//Check if user is logged in.
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Persoon p = Persoon.getCurrentPersoon(pm, req, resp, true);
		if (null == p)
			return;

		resp.setContentType("text/plain");

		//TweetFactory tf = new TweetFactory();

		//resp.getWriter().print((new JSONObject(p)).toString());
		//resp.getWriter().print(tf.bySearchTerm(req.getParameter("searchTerm"), 1).toString());
		//resp.getWriter().print(Tweets.getRecentTweets(1).toString());
		
		
		resp.getWriter().println(p.getVragen().get(0).toString());

	}

}
