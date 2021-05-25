package http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestLineTest {

    @Test
    void parseRequestUrlToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        String data = "GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1";

        Map<String, String> actualMap = new HashMap<>();
        RequestLine requestLine = new RequestLine(data);
        assertEquals(actualMap, expectedMap);
        assertEquals("GET", requestLine.getMethod());
        assertEquals("/user/create", requestLine.getPath());
        assertEquals(3, requestLine.getParams().size());
        assertEquals("HTTP/1.1", requestLine.getHttpVersion());
    }

    @Test
    void parseUrl_POST() {
        String http = "POST /user/create HTTP/1.1";
        Map<String, String> actual = new HashMap<>();

        RequestLine requestLine = new RequestLine(http);

        assertEquals("POST", requestLine.getMethod());
        assertEquals("/user/create", requestLine.getPath());
        assertEquals("HTTP/1.1", requestLine.getHttpVersion());
    }
}