package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final Map<String, String> requestHeaderMap = new HashMap<>();
    private Map<String, String> requestParameterMap;
    private RequestLine requestLine;


    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String headerLine;
            String line = br.readLine();
            if (line == null) {
                return;
            }

            log.info(line);

            requestLine = new RequestLine(line);

            while ((headerLine = br.readLine()) != null && !headerLine.equals("")) {
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
                requestHeaderMap.put(pair.getKey(), pair.getValue());
                log.info(headerLine);
            }

            if (getMethod().equals("POST")) {
                parseParameter(IOUtils.readData(br, Integer.parseInt(requestHeaderMap.get("Content-Length"))));
            } else {
                requestParameterMap = requestLine.getParams();
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String getHeader(String headerName) {
        return this.requestHeaderMap.get(headerName);
    }

    private void parseParameter(String parameters) {
        requestParameterMap = HttpRequestUtils.parseQueryString(parameters);
    }

    public String getMethod() {
        return this.requestLine.getMethod();
    }

    public String getPath() {
        return this.requestLine.getPath();
    }

    public String getParameter(String key) {
        return this.requestParameterMap.get(key);
    }

}
