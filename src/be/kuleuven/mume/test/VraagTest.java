package be.kuleuven.mume.test;


import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VraagTest {
	
	@Before
	public void startup() {
	}
	
	@Test
	public void testStoreVak() throws IOException{
		
		URL url = new URL("http://localhost:8888/vraag?q=add&text=Hoe zou dit moeten?&vakid=agltb2JpbGVudmlyCQsSA1ZhaxgRDA");
		
		URLConnection conn = url.openConnection();
		String responcecode = conn.getHeaderField(0);
		Assert.assertEquals("HTTP/1.1 200 OK", responcecode);
		
		
	}
	
	@Test
	public void testWrongUrl() throws IOException{
		
		URL url = new URL("http://localhost:8888/vak?q=add&name=reza&hashtag=mijnvak");
		
		URLConnection conn = url.openConnection();
		String responcecode = conn.getHeaderField(0);
		Assert.assertNotSame("HTTP/1.1 200 OK", responcecode);
	}
}
