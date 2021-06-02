package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpHeadersTest {

    @Test
    void getContentLength() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-length: 15");
        headers.add("JSESSIONID=A7670BEC59EBD9B6AE610ABD6410183F");

        assertEquals(15, headers.getContentLength());
        assertEquals("A7670BEC59EBD9B6AE610ABD6410183F", headers.getSession().getId());
    }

}