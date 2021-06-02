package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParamsTest {

    @Test
    void getParameter_GET() {
        String param = "userId=javajigi&password=password&name=JaeSung";
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryString(param);

        assertEquals("javajigi", requestParams.getParameter("userId"));
        assertEquals("password", requestParams.getParameter("password"));
        assertEquals("JaeSung", requestParams.getParameter("name"));
    }

}