package de.htwb.lc;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 2347965502490417996L;

    private String body;
    private int code;

    public Response(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

}
