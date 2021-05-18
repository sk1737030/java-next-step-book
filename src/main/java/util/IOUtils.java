package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class IOUtils {
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String[] readHttpUrlHeader(String httpUrlLine) throws IOException {
        return httpUrlLine.split(" ");
    }

    public static String readHttpUrl(String url) {
        String[] httpLine = url.split(" ");
        return httpLine[1];
    }

    public static void readHttpUrlHeader(Map<String, String> requestHeaderParamMap, String line) {
        int index = line.indexOf(":");
        requestHeaderParamMap.put(line.substring(0, index), line.substring(index + 1));
    }
}
