package next.step.chap2.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CalculatorTest {

    private Calculator cal;



    @BeforeEach
    public void setUp() {
        cal = new Calculator();
    }

    @ParameterizedTest(name = "문자열을_쉼표_또는_콜론_구분자를_기준으로_분리한_값_{0}의_합을_반환한다")
    @MethodSource("numberSet")
    void 문자열을_쉼표_또는_콜론_구분자를_기준으로_분리한_각_n의_합을_반환한다(int answer, String values) {
        assertEquals(cal.add(values), answer);
        assertEquals(cal.add(values), answer);
        assertEquals(cal.add(values), answer);
        assertEquals(cal.add(values), answer);
    }

    private static List<Arguments> numberSet() {
        return Arrays.asList(
                Arguments.of(0, ""),
                Arguments.of(3, "1,2"),
                Arguments.of(6, "1,2,3"),
                Arguments.of(6, ",1,2:3")
        );
    }

    @Test
    void subtract() {
        assertEquals(3, cal.subtract(6, 3));
    }

    @AfterEach
    public void after() {
    }
}