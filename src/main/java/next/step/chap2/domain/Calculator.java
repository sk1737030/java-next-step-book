package next.step.chap2.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    int add(int i, int j) {
        return i + j;
    }

    int subtract(int i, int j) {
        return i - j;
    }

    int multiply(int i, int j) {
        return i * j;
    }

    int divide(int i, int j) {
        return i / j;
    }

    public int add(String values) {
        int result = 0;
        if (values.isEmpty()) {
            return result;
        }

        Pattern p = Pattern.compile("\\d(\\/\\/[^\\/\\/n]+\\\\n)?([^/|;|:|,])*");
        Matcher m = p.matcher(values);

        while (m.find()) {
            String group = m.group();
            if (!group.isBlank())
                result += Integer.parseInt(group);
        }

        return result;
    }
}
