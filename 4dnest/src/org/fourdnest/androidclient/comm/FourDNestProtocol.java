package org.fourdnest.androidclient.comm;

import android.net.Uri;
import android.util.Log;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class FourDNestProtocol implements Protocol {
	private static final String TAG = "FourDNestProtocol";
	private static final String EGG_UPLOAD_PATH = "fourdnest/api/v1/egg/upload/";
	private static final String EGG_DOWNLOAD_PATH = "fourdnest/api/v1/egg/";
	private static final String JSON_FORMAT = "?format=json";
	private static final int HTTP_STATUSCODE_OK = 200;
	private static final int HTTP_STATUSCODE_CREATED = 201;
	private static final int HTTP_STATUSCODE_UNAUTHORIZED = 401;
	private static final int HTTP_STATUSCODE_SERVER_ERROR = 500;
	private static final int CONNECTION_TIMEOUT = 15000;
	private Nest nest;

	public FourDNestProtocol() {
	    this.nest = null;
	}

    /**
     * Parses egg's content and sends it in multipart mime format with HTTP
     * post.
     * 
     * @return HTTP status code and egg URI on server if creation successful
     **/
    public ProtocolResult sendEgg(Egg egg) {
        
        String concatedMd5 = "";
        HttpClient client = createHttpClient();
        HttpPost post = new HttpPost(this.nest.getBaseURI() + EGG_UPLOAD_PATH);
        String metadata = eggToJSONstring(egg);
        Log.d("METADATA", metadata);

        // Create list of NameValuePairs
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        /*pairs.add(new BasicNameValuePair("metadata", metadata));
         * String metadataMd5 = md5FromString(metadata);
        */
        pairs.add(new BasicNameValuePair("caption", egg.getCaption()));
        concatedMd5 += md5FromString(egg.getCaption());
        if (egg.getLocalFileURI() != null) {
            pairs.add(new BasicNameValuePair("file", egg.getLocalFileURI()
                    .getPath()));
            concatedMd5 += md5FromFile(egg.getLocalFileURI().getPath());
        }
        
        // FIXME: Add tags later
        
        String multipartMd5String = md5FromString(concatedMd5);
        multipartMd5String = new String(Base64.encodeBase64(multipartMd5String.getBytes()));
        
        int status = 0;
        try {
            post.setEntity(this.createEntity(pairs));
            Date date = new Date();
            
            /*
             * StringToSign = HTTP-Verb + '\n' +
             * base64(Content-MD5) + '\n' +
             * base64(x-4dnest-multipartMD5) + '\n' +
             * Content-Type + '\n' +
             * Date + '\n' +
             * RequestURI
             * 
             * Should be in utf-8 automatically.
             */
            String stringToSign = post.getMethod() + "\n" +
            "" + "\n" +                                     //Content-MD5 empty for now
            multipartMd5String + "\n" +
            ""  + "\n" +                                       //Content-type empty for now
            DateUtils.formatDate(date) + "\n" +
            post.getURI().getPath();
            
            Log.d("message", stringToSign);
            
            String secretKey = "secret";
            String userName = "testuser";
            
            post.setHeader("x-4dnest-multipartMD5", multipartMd5String);
            
            String signature = computeSignature(stringToSign, secretKey);
            String authHeader = userName + ":" + signature;
            post.setHeader("Authorization", authHeader);
            Log.d("sign", authHeader);
            
            post.setHeader("Date", DateUtils.formatDate(date));
            
            post.setHeader("x-4dnest-multipartMD5", multipartMd5String);
            
            HttpResponse response = client.execute(post);
            status = response.getStatusLine().getStatusCode();
            return this.parseResult(status, response);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException, egg not sent "
                    + e.getMessage());
            return new ProtocolResult(null, ProtocolResult.SENDING_FAILED);
        } catch (IOException e) {
            Log.e(TAG, "IOException, egg not sent " + e.getMessage());
            return new ProtocolResult(null, ProtocolResult.SENDING_FAILED);
        }
    }
    
    /**
     * Creates the proper ProtocolResult object from the server response
     * 
     * @param The statuscode given in the server response
     * 
     */
    private ProtocolResult parseResult(int statusCode, HttpResponse response) {
        if (statusCode == HTTP_STATUSCODE_CREATED) {
            return new ProtocolResult(response.getHeaders("Location")[0]
                    .getValue(), ProtocolResult.RESOURCE_UPLOADED);
        }
        else if (statusCode == HTTP_STATUSCODE_UNAUTHORIZED) {
            return new ProtocolResult(null, ProtocolResult.AUTHORIZATION_FAILED);
        }
        else if (statusCode == HTTP_STATUSCODE_SERVER_ERROR) {
            return new ProtocolResult(null, ProtocolResult.SERVER_INTERNAL_ERROR);
        }else {
            return new ProtocolResult(null, ProtocolResult.UNKNOWN_REASON);
        }
    }

    /**
     * Creates the MultipartEntity from name-value -pair list
     * 
     * @throws UnsupportedEncodingException
     */
    private MultipartEntity createEntity(List<NameValuePair> pairs)
            throws UnsupportedEncodingException {
    	Charset charset = Charset.forName("UTF-8");
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT, null, charset);

        for (int i = 0; i < pairs.size(); i++) {
            File file = new File(pairs.get(i).getValue());
            if (pairs.get(i).getName().equalsIgnoreCase("file")) {
                entity.addPart(pairs.get(i).getName(), new FileBody(file));
            } else {
            	StringBody strbd = new StringBody(pairs
                        .get(i).getValue(), charset);
                entity.addPart(pairs.get(i).getName(), new StringBody(pairs
                        .get(i).getValue()));
                //Log.d("STRINGBODY", strbd.getCharset());
            }
        }
        Log.d("CONTENTTYPE", entity.getContentType().getValue());
        return entity;
    }
    
    private DefaultHttpClient createHttpClient() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // http scheme
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        // https scheme
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
                443));
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        return new DefaultHttpClient(cm, params);
    }

    public List<Tag> topTags(int count) {
        // TODO Auto-generated method stub
        return null;
    }


	public int getProtocolId() {
		return ProtocolFactory.PROTOCOL_4DNEST;
	}
	
    public String getTest() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        request.setURI(new URI("http://hs.fi/index.html"));
        HttpResponse response = client.execute(request);
        BufferedReader in = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();
        String page = sb.toString();

        return page;
    }

    public void setNest(Nest nest) {
        this.nest = nest;

    }
    
    public Egg getEgg(String uid) {
    	HttpClient client = createHttpClient();
        HttpGet request = new HttpGet();
        String temp = "http://test42.4dnest.org/";
        String uriPath = temp + EGG_DOWNLOAD_PATH + uid + "/" + JSON_FORMAT;
        Log.d("URI", uriPath);
       
    	try {
    		request.setURI(new URI(uriPath));
    		String jsonStr = responseToString(client.execute(request));
	    	JSONObject js = new JSONObject(jsonStr);
	    	return jSONObjectToEgg(js);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public List<Egg> getStream() {
    	Egg current = null;
    	ArrayList<Egg> eggList = new ArrayList<Egg>();
    	HttpClient client = createHttpClient();
        HttpGet request = new HttpGet();
        String uriPath = this.nest.getBaseURI() + EGG_DOWNLOAD_PATH + JSON_FORMAT;
        Log.d("URIStream", uriPath);
        try {
			request.setURI(new URI(uriPath));
			String jsonStr = responseToString(client.execute(request));
			JSONObject outer = new JSONObject(jsonStr);
			JSONArray jsonArr = outer.getJSONArray("objects");
			for (int i = 0; i < jsonArr.length(); i++) {
				current = jSONObjectToEgg(jsonArr.getJSONObject(i));
				Log.d("CURRENTEGG", current.getExternalId());
				eggList.add(current);
			}
			return eggList;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    /**
     * Retrieves a file from uri to localpath over HTTP
     * 
     * @param uri Location of the file in server
     * @param localPath Local path where the file is to be saved
     * 
     * @return true if file retrieved successfully, false otherwise
     */
    public boolean getMediaFile(String uri, String localPath) {
    	HttpClient client = createHttpClient();
    	try {
			HttpGet request = new HttpGet(new URI(uri));
			HttpResponse resp = client.execute(request);
			if (resp.getStatusLine().getStatusCode() != HTTP_STATUSCODE_OK) {
				return false;
			}
			InputStream is = resp.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream os = new FileOutputStream(new File(localPath));
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int c;
	        while ((c = bis.read()) != -1) {
	            bos.write(c);
	        }
	        bos.close();
	        bis.close();
	        return true;
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return false;
    			
	}
    
    private Egg jSONObjectToEgg(JSONObject js) {
    	try {
			String caption = js.getString("caption");
			String externalFileUri = js.getString("resource_uri");
			String author = js.getString("author");
			Egg egg = new Egg(0, this.nest.getId(), author, null, Uri.parse(externalFileUri), caption, null, 0);
			String uid = js.getString("uid");
			egg.setExternalId(uid);
			return egg;
		} catch (JSONException e) {
			Log.e("JSONTOEGG", "Got JSONexception");
		}
    	return null;
    }
    
    private String eggToJSONstring(Egg egg) {
    	JSONObject temp = new JSONObject();
    	try {
			temp.put("author", egg.getAuthor());
			temp.put("caption", egg.getCaption());
			return temp.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }
    
    private String responseToString(HttpResponse response) throws IOException {
    	InputStream content = null;
    	content = response.getEntity().getContent();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
    	byte[] buf = new byte[1024];
    	int len;
    	while ((len = content.read(buf)) > 0) {
    	bout.write(buf, 0, len);
    	}
    	content.close();
    	return bout.toString();
    }
    
    
    /**
     * Hashes the string in MD5 and returns a base64 encoded string
     * of the hash
     * 
     * @param String to be hashed
     * 
     * @return Base64 encoded md5 hash
     **/
    private String md5FromString(String s) {
        String result = "";
        if (s != null) {
            try {
                byte[] bytes = DigestUtils.md5(s.getBytes("UTF-8"));
                result = new String(Hex.encodeHex(bytes));
    
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                
            }
            
        }
        return result;
    }
    /**
     * Hashes the file with the given path in MD5 and returns a base64 encoded string
     * of the hash
     * 
     * @param Path of the file
     * 
     * @return Base64 encoded md5 hash
     **/
    private String md5FromFile(String path) {
        String result = "";
        try {
            FileInputStream fis = new FileInputStream( new File(path));
            byte[] bytes = DigestUtils.md5(fis);
            result = new String(Hex.encodeHex(bytes));
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
           
        }
        return result;
        
    }
    
    private String computeSignature(String stringToSign, String secretKey) {
        String result = "";
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            String hexStr = new String(Hex.encodeHex(mac.doFinal(stringToSign.getBytes())));
            result = new String(Base64.encodeBase64(hexStr.getBytes()));
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
           
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
           
        }
        return result;
    }
    
}
