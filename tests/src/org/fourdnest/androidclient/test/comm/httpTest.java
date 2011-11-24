package org.fourdnest.androidclient.test.comm;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import android.net.Uri;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class httpTest extends AndroidTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testHttpGet() throws Exception {
		
		FourDNestProtocol protocol = new FourDNestProtocol();
		String get = protocol.getTest();
		assertTrue(get.length() != 0);
		Log.v("httpget", get);
	}
	
	@Test
	public void testHttpPost() throws Exception {
		FourDNestProtocol protocol = new FourDNestProtocol();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		InputStream is = this.getContext().getAssets().open("kuva.jpg");
		BufferedInputStream bufin = new BufferedInputStream(is);
		File root = Environment.getExternalStorageDirectory();
		FileOutputStream os = new FileOutputStream(new File(root, "kuva.jpg"));
		BufferedOutputStream bufout = new BufferedOutputStream(os);
		int c;
		while ((c = bufin.read()) != -1) {
		    bufout.write(c);
		}
		bufout.close();
		bufin.close();
		Uri uri = Uri.parse("/sdcard/kuva.jpg");
		Log.v("Path", uri.getPath());
		Egg egg = new Egg(5, 10, uri, null, "Now it should finally work from assets.", tags, 100);
		Nest nest = new Nest(007, "testNest", "testNest", "http://test42.4dnest.org/fourdnest/api/", 007);
		protocol.setNest(nest);
		String post = protocol.sendEgg(egg);
		assertTrue(post.split(" ")[0].equalsIgnoreCase("201"));
		Log.v("httppost", post);
	}

}
