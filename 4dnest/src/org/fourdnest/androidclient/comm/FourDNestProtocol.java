package org.fourdnest.androidclient.comm;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Tag;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class FourDNestProtocol implements Protocol {

	public String sendEgg(Egg egg) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Tag> topTags(int count) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getTest() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		request.setURI(new URI("http://hs.fi/index.html"));
		HttpResponse resp = client.execute(request);
		return resp.toString();
	}
	
	public String postTest() throws Exception {
		HttpClient client = new DefaultHttpClient();
		//HttpContext context = new BasicHttpContext();
		HttpPost post = new HttpPost("http://test42.4dnest.org/fourdnest/api/v1/egg/upload/");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("name", "comma"));
		pairs.add(new BasicNameValuePair("title", "Post_test"));
		pairs.add(new BasicNameValuePair("caption", "Just testing with this funny picture"));
		pairs.add(new BasicNameValuePair("file", "/sdcard/kuva.jpg"));
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);
		for (int i = 0; i < pairs.size(); i ++) {
			if (pairs.get(i).getName().equalsIgnoreCase("file")) {
				entity.addPart(pairs.get(i).getName(), new FileBody(new File(pairs.get(i).getValue())));
			}else {
				entity.addPart(pairs.get(i).getName(), new StringBody(pairs.get(i).getValue()));
			}
		}
		post.setEntity(entity);
		ResponseHandler<String> responseHandler=new BasicResponseHandler();
		String resp = client.execute(post, responseHandler);
		return resp;
	}
}
