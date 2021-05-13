package next.step.chap2.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {
    public int add(String values) {
        int result = 0;

        if (values == null || values.isEmpty()) {
            return result;
        }

        Pattern p = Pattern.compile("[-\\d](\\/\\/[^\\/\\/n]+\\\\n)*?([^/|;|:|,])*");
        Matcher m = p.matcher(values);

        while (m.find()) {
            result += isBlank(m.group());
        }

        return result;
    }

    private int isBlank(String values) {
        if (!values.isBlank()) {
            return toPositive(values);
        }

        return 0;
    }

    private int toPositive(String value) {
        if (Integer.parseInt(value) < 0) {
            throw new RuntimeException();
        }
        return Integer.parseInt(value);
    }
}
