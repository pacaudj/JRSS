package squawk.openjfx;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.json.Json;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestHelper {

    public static String doPost(String url, String postData) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // build connection
            URLConnection conn = realUrl.openConnection();
            // set request properties
            conn.setRequestProperty("Content-Type", "application/json");
            // enable output and input
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            // send POST DATA
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static boolean tryAuth() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("token.txt")));
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://squawkapi.chaz.pro/sources");
            httpGet.addHeader("Authorization", "Bearer " + content);
            HttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                return false;
            }
        } catch (Exception err) {
            return false;
        }
        return true;
    }

    public static HttpResponse getSources(String token) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://squawkapi.chaz.pro/sources");
        httpGet.addHeader("Authorization", "Bearer " + token);
        HttpResponse response;
        try {
            response = httpclient.execute(httpGet); // the client executes the request and gets a response
            } catch (Exception err){
            return null;
        }
        return response;
    }

    public static HttpResponse getSourceContent(String token, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://squawkapi.chaz.pro/source/" + id + "/content");
        httpGet.addHeader("Authorization", "Bearer " + token);
        HttpResponse response;
        try {
            response = httpclient.execute(httpGet); // the client executes the request and gets a response
            } catch (Exception err){
            return null;
        }
        return response;
    }

    public static HttpResponse postSource(String token, String link, String name) {
        String postData = Json.createObjectBuilder()
                .add("link", link)
                .add("name", name)
                //.add("password", passwordField.getText())
                .build()
                .toString();
        HttpResponse response;
        try {
            HttpPost httpPost = new HttpPost("https://squawkapi.chaz.pro/source");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + token);
            StringEntity entity = new StringEntity(postData);
            httpPost.setEntity(entity);
            HttpClient httpclient = HttpClientBuilder.create().build();
            response = httpclient.execute(httpPost); // the client executes the request and gets a response
            System.out.println(response);
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return null;
        }
        return response;
    }
}
