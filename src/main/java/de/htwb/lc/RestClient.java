package de.htwb.lc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class RestClient {

    private HttpClient createClient(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
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

    private Response execute(HttpClient client, HttpMethodBase method) throws IOException {
        int code = client.executeMethod(method);

        String charSet = method.getResponseCharSet();
        long length = method.getResponseContentLength();
        String body = method.getResponseBodyAsString();

        Header contentTypeHeader = method.getResponseHeader("Content-Type");
        String contentType = null;
        if (contentTypeHeader != null) {
            contentType = contentTypeHeader.getValue();
        }

        method.releaseConnection();
        return new Response(code, body, charSet, contentType, length);
    }

    public Response get(String url) throws IOException {
        return execute(createClient(url), new GetMethod(url));
    }

    public Response post(String url, String body, String contentType, String charset) throws IOException {
        PostMethod method = new PostMethod(url);
        method.setRequestEntity(new StringRequestEntity(body, contentType, charset));
        return execute(createClient(url), method);
    }

}
