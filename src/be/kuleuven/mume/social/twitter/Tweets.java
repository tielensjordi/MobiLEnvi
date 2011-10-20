package be.kuleuven.mume.social.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Tweets extends ArrayList<Tweet>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5145116698874733216L;
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Tweet t : this) {
			b.append(t.toString() + "\n");
		}
		return b.toString();
	}
}
