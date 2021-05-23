package webserver;

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
    public static final String REQUEST_METHOD = "requestMethod";
    public static final String REQUEST_PATH = "requestPath";

    private final InputStream in;
    private final Map<String, String> requestHeaderMap = new HashMap<>();
    private Map<String, String> requestParameterMap;

    public HttpRequest(InputStream in) {
        this.in = in;
        parseInputStream();
    }

    private void parseInputStream() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String headerLine;
            String line = br.readLine();

            if (line == null) {
                return;
            }
            log.info(line);

            HttpRequestUtils.parseRequestUrlToMap(requestHeaderMap, line);

            while ((headerLine = br.readLine()) != null && !headerLine.equals("")) {
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
                requestHeaderMap.put(pair.getKey(), pair.getValue());

                log.info(headerLine);
            }


            parseHeaderByUrl();

            if (getMethod().equals("POST")) {
                parseParameter(IOUtils.readData(br, Integer.parseInt(requestHeaderMap.get("Content-Length"))));
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String getHeader(String headerName) {
        return this.requestHeaderMap.get(headerName);
    }

    private void parseHeaderByUrl() {
        String httpUrl = this.requestHeaderMap.get(REQUEST_PATH);
        if (httpUrl == null) return;
        int index = httpUrl.indexOf("?");
        if (index > -1) {
            requestHeaderMap.put(REQUEST_PATH, httpUrl.substring(0, index));
            parseParameter(httpUrl, index);
        } else {
            requestHeaderMap.put(REQUEST_PATH, httpUrl);
        }
    }

    private void parseParameter(String parameters) {
        requestParameterMap = HttpRequestUtils.parseQueryString(parameters);
    }

    private void parseParameter(String parameters, int index) {
        requestParameterMap = HttpRequestUtils.parseQueryString(parameters.substring(index + 1));
    }

    public String getMethod() {
        return this.requestHeaderMap.get(REQUEST_METHOD);
    }

    public String getPath() {
        return this.requestHeaderMap.get(REQUEST_PATH);
    }

    public String getParameter(String key) {
        return this.requestParameterMap.get(key);
    }
}
