package be.kuleuven.mume;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Google extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1523817028186873002L;
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	//checkUserLogin(req,resp);
    	
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();

        if (user != null) {
        	return;
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    }
    
    public static boolean checkUserLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();

        if (user != null) {
        	return true;
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
            return false;
        }
    }
    
    public static User getUser(){
    	UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser();
    }

}
