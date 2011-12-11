package org.fourdnest.androidclient.comm;

public class ProtocolResult {
    private String url;
    private int statusCode;
    public static int RESOURCE_UPLOADED = 1;
    public static int AUTHORIZATION_FAILED = 2;
    public static int SERVER_INTERNAL_ERROR = 3;
    public static int SENDING_FAILED= 4;
    public static int UNKNOWN_REASON = 5;
    
    public ProtocolResult(String url, int statusCode) {
        this.url = url;
        this.statusCode = statusCode;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
}
