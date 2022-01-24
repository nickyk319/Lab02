package com.example.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.Scanner;

public class BookClient {
    public static void main(String[] args) throws InterruptedException {

        replaceBook("MorningBrew", "Lily", "0101aaa");
        replaceBook("AfternoonBrew", "Nicky", "0101bbb");
        replaceBook("MidnightBrew", "Jerry", "0101ccc");

        System.out.println(getBook("MorningBrew"));

        Thread.sleep(5000);

        replaceBook("MorningBrew", "Sara", "0101aaa");

        System.out.println(getBook("MorningBrew"));

        Thread.sleep(5000);

        System.out.println(getBook("MidnightBrew"));

    }

    /**
     * Gets the list of books by calling the API
     * @return string representation of all the books currently in the library
     */
    private static String getBook (String title){
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(String.format("http://localhost:8080/library/book/%s", title));
            CloseableHttpResponse response = client.execute(httpGet);
            return readResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "Fail to get books";
        }
    }

    /**
     * Reads the response and converts it into a string
     * @param response response from http request
     * @return string of the response
     * @throws IOException
     */
    public static String readResponse (CloseableHttpResponse response) throws IOException {
        Scanner sc = new Scanner(response.getEntity().getContent());
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
            sb.append("\n");
        }
        response.close();
        return sb.toString();
    }

    private static void replaceBook(String title, String author, String isbn) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(String.format("http://localhost:8080/library/book/%s/%s/%s",title, author,isbn));
            CloseableHttpResponse httpResponse = client.execute(httpPut);
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
