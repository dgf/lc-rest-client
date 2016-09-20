package de.htwb.lc;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

    private Response execute(HttpClient client, HttpMethodBase method, String accept) throws IOException {
        // set accept header
        if (accept != null) {
            method.addRequestHeader("Accept", accept);
        }

        // call HTTP
        int code = client.executeMethod(method);

        // get headers
        String charSet = method.getResponseCharSet();
        long length = method.getResponseContentLength();
        String body = method.getResponseBodyAsString();

        Header contentTypeHeader = method.getResponseHeader("Content-Type");
        String contentType = null;
        if (contentTypeHeader != null) {
            contentType = contentTypeHeader.getValue();
        }

        // release and return
        method.releaseConnection();
        return new Response(code, body, charSet, contentType, length);
    }

    public Response delete(String url, String accept) throws IOException {
        return execute(createClient(url), new DeleteMethod(url), accept);
    }

    public Response delete(String url) throws IOException {
        return delete(url, null);
    }

    public Response get(String url, String accept) throws IOException {
        return execute(createClient(url), new GetMethod(url), accept);
    }

    public Response get(String url) throws IOException {
        return get(url, null);
    }

    public Response post(String url, String accept, String body, String contentType, String charset) throws IOException {
        PostMethod method = new PostMethod(url);
        method.setRequestEntity(new StringRequestEntity(body, contentType, charset));
        return execute(createClient(url), method, accept);
    }

    public Response post(String url, String body, String contentType, String charset) throws IOException {
        return post(url, null, body, contentType, charset);
    }

    public Response put(String url, String accept, String body, String contentType, String charset) throws IOException {
        PutMethod method = new PutMethod(url);
        method.setRequestEntity(new StringRequestEntity(body, contentType, charset));
        return execute(createClient(url), method, accept);
    }

    public Response put(String url, String body, String contentType, String charset) throws IOException {
        return put(url, null, body, contentType, charset);
    }

}
