package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpCookieTest {

    @Test
    void cookieTest() {
        HttpCookie httpCookie = new HttpCookie("logined=true; JSESSIONID=1234;");
        assertEquals("1234", httpCookie.getCookie("JSESSIONID"));
        assertEquals("true", httpCookie.getCookie("logined"));
    }

}