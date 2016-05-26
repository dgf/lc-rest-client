package de.htwb.lc;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 5440297997686483909L;

    private String body;
    private String charSet;
    private int code;
    private String contentType;
    private long length;

    public Response(int code, String body, String charSet, String contentType, long length) {
        this.code = code;
        this.body = body;
        this.charSet = charSet;
        this.contentType = contentType;
        this.length = length;
    }

    public String getBody() {
        return body;
    }

    public String getCharSet() {
        return charSet;
    }

    public int getCode() {
        return code;
    }

    public String getContentType() {
        return contentType;
    }

    public long getLength() {
        return length;
    }
}
