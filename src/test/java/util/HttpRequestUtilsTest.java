package util;

import model.User;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestUtilsTest {
    @Test
    void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertNull(parameters.get("password"));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertEquals(parameters.get("password"), "password2");
    }

    @Test
    void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertTrue(parameters.isEmpty());

        parameters = HttpRequestUtils.parseQueryString("");
        assertTrue(parameters.isEmpty());

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertTrue(parameters.isEmpty());
    }

    @Test
    void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertNull(parameters.get("password"));
    }

    @Test
    void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertEquals(parameters.get("logined"), "true");
        assertEquals(parameters.get("JSessionId"), "1234");
        assertNull(parameters.get("session"));
    }

    @Test
    void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertEquals(pair, new Pair("userId", "javajigi"));
    }

    @Test
    void getKeyValue_invalid() {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertNull(pair);
    }

    @Test
    void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertEquals(pair, new Pair("Content-Length", "59"));
    }

    @Test
    void readValue() {
        String params = "userId=java&password=1234&name=test&email=sk1737030@naver.com";
        Map<String, String> objectValues = HttpRequestUtils.parseQueryString(params);
        User user = new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));

        assertEquals(objectValues.get("userId"), "java");
        assertEquals(objectValues.get("userId"), user.getUserId());
        assertEquals(objectValues.get("password"), user.getPassword());
        assertEquals(objectValues.get("name"), user.getName());
        assertEquals(objectValues.get("email"), user.getEmail());
    }

    @Test
    void parseRequestUrlToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        String data = "GET /index.jsp HTTP/1.1";
        expectedMap.put("requestMethod", "GET");
        expectedMap.put("requestPath", "/index.jsp");
        expectedMap.put("requestHttpVersion", "HTTP/1.1");

        Map<String, String> actualMap = new HashMap<>();
        HttpRequestUtils.parseRequestUrlToMap(actualMap, data);
        assertEquals(actualMap, expectedMap);
    }

    @Test
    void parseUrl_POST() {
        String http = "POST /user/create HTTP/1.1";
        Map<String, String> actual = new HashMap<>();

        HttpRequestUtils.parseRequestUrlToMap(actual, http);

        assertEquals("POST", actual.get("requestMethod"));
        assertEquals("/user/create", actual.get("requestPath"));
        assertEquals("HTTP/1.1", actual.get("requestHttpVersion"));
    }
}
