package de.htwb.lc;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

public class RestClient {

    private HttpClient createClient(URL url) {
        HttpClient client = new HttpClient();

        // optional authentication provider configuration
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            int delim = userInfo.indexOf(":");
            String user = userInfo.substring(0, delim);
            String pass = userInfo.substring(delim + 1);
            client.getState().setCredentials(//
                    new AuthScope(url.getHost(), url.getPort()), //
                    new UsernamePasswordCredentials(user, pass));
        }
        return client;
    }

    public Response get(String url) throws IOException {
        HttpClient client = createClient(new URL(url));
        GetMethod httpGet = new GetMethod(url);
        int code = client.executeMethod(httpGet);
        String body = httpGet.getResponseBodyAsString();
        return new Response(code, body);
    }
}
