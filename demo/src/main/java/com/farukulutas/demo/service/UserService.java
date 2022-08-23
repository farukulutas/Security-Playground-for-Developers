package com.farukulutas.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
public class UserService {
    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public ResponseEntity<String> writeToFile(String code) throws IOException, InterruptedException {
        code = decode(code);
        code = code.substring(5);

        Path path = Paths.get("docker-project/www/html/sql.php");
        Path absolute = path.toAbsolutePath();

        byte[] arr = code.getBytes();

        try {
            Files.write(absolute, arr);
        } catch (IOException ex) {
            System.out.print("Invalid Path");
            return new ResponseEntity<>("fail; invalid path", HttpStatus.CREATED);
        }

        if ( testCode().equals("SUCCESS") ) {
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("fail; test failed", HttpStatus.CREATED);
    }

    public String tryPayload(String url) throws IOException, InterruptedException {
        int indexStart;
        int indexEnd;
        String result;

        //url = encode( url);

        HttpClient send2client = HttpClient.newHttpClient();
        HttpRequest Req2client = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> clientRes = send2client.send(Req2client, HttpResponse.BodyHandlers.ofString());

        indexStart = clientRes.body().lastIndexOf("</form>");
        indexEnd = clientRes.body().lastIndexOf("</body>");
        result = clientRes.body().substring(indexStart + 8, indexEnd - 2);
        return result;
    }

    public String testCode() throws IOException, InterruptedException {
        String baseUrl = "http://127.0.0.1/sql.php?id=2";
        String result = tryPayload( baseUrl);
        String test;

        ArrayList<String> payloads = new ArrayList<>();
        payloads.add("");
        payloads.add("+OR+1=1");
        payloads.add("+'");
        payloads.add("+UNION+ALL+SELECT+NULL,NULL,CONCAT(0x7170706271,0x64486156616161516c614d59664766474d6e6864626d6a4c547a756b624c6b6745786863754f4f53,0x7170706271),NULL,NULL--+-");

        for ( String payload : payloads ) {
            test = tryPayload(baseUrl + payload);

            if ( !test.equals(result) ) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

}
