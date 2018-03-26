/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author samet
 */
public class HttpServerXL {

    private static HttpServer ServerSocket;

    public static void Start() throws IOException {
        ServerSocket = HttpServer.create(new InetSocketAddress(7000), 0);
        ServerSocket.createContext("/info", new InfoHandler());
        ServerSocket.createContext("/get", new GetHandler());
        ServerSocket.createContext("/page", new GetPageHandler());
        ServerSocket.createContext("/param", new GetParamHandler());
        ServerSocket.setExecutor(null); // create defalult extructur
        ServerSocket.start();

    }

    static class InfoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange HandleResponse) throws IOException {
            String ResponseText = "Fatih Sultan Mehmet Vakif University";
            HandleResponse.sendResponseHeaders(500, ResponseText.length()); // headeri ayarlıyoruz
            OutputStream os = HandleResponse.getResponseBody(); // body referansını alıyoruz
            os.write(ResponseText.getBytes()); // cevabı yazdırıyoruz.
            os.close();
        }
    }

    static class GetHandler implements HttpHandler {

        public void handle(HttpExchange HandleResponse) throws IOException {

            // pdf göndereceğiz
            Headers h = HandleResponse.getResponseHeaders();
            h.add("Content-Type", "application/pdf"); // dosya tipini belirtiyoruz

            File file = new File("C:\\deneme.pdf"); // pdf yolu dosyayı alıyoruz
            byte[] bytearray = new byte[(int) file.length()]; // bir buffer tanımlıyoruz
            FileInputStream fis = new FileInputStream(file); // dosyayı byte arraya çevireceğiz
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);// dosyauı byte arraya geçvirdik

            // cevap için hazırısz
            HandleResponse.sendResponseHeaders(200, file.length());// cevap için header ayarlaması
            OutputStream os = HandleResponse.getResponseBody();// body referansını alıyoruz
            os.write(bytearray, 0, bytearray.length); // dosya datakısmına bytedizisi olarak eklendi ve gönderildi
            os.close();
        }
    }

    static class GetPageHandler implements HttpHandler {

        public void handle(HttpExchange HandleResponse) throws IOException {

            // text dosya göndereceğiz
            Headers h = HandleResponse.getResponseHeaders(); // göndereceğimiz dosya tipini seçiyoruz
            h.add("Content-Type", "text/html"); // header ayarlaması yapıyoruz

            File file = new File("D:\\deneme.html"); // dosyayı alıyoruz
            byte[] bytearray = new byte[(int) file.length()]; // bir buffer tanımlıyoruz
            FileInputStream fis = new FileInputStream(file); // dosyayı byte arraya çevireceğiz
            BufferedInputStream inpustrm = new BufferedInputStream(fis);
            inpustrm.read(bytearray, 0, bytearray.length); // dosyauı byte arraya geçvirdik

            // cevap için hazırısz
            HandleResponse.sendResponseHeaders(200, file.length()); // cevap için header ayarlaması
            OutputStream os = HandleResponse.getResponseBody(); // body referansını alıyoruz
            os.write(bytearray, 0, bytearray.length); // dosya datakısmına bytedizisi olarak eklendi ve gönderildi
            os.close();
        }
    }

    static class GetParamHandler implements HttpHandler {

        public void handle(HttpExchange HandleResponse) throws IOException {
            StringBuilder response = new StringBuilder();
            Map<String, String> parms = HttpServerXL.queryToMap(HandleResponse.getRequestURI().getQuery());
            response.append("<html><body>");
            response.append("hello : " + parms.get("hello") + "<br/>");
            response.append("foo : " + parms.get("foo") + "<br/>");
            response.append("<div>  <p style=\"color: #ffffff; background-color: #ff0000\">Text color: white, background-color: red</p>\n\" </div>");
            response.append("</body></html>");

            HandleResponse.sendResponseHeaders(200, response.length());
            OutputStream os = HandleResponse.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();

        }
    }

    //http://localhost:8000/param?&hello=samet&foo=kaya
    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        HttpServerXL.Start();
    }

}
