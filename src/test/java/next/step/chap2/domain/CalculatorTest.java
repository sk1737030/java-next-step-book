package next.step.chap2.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    void _커스텀_구분자를_포함한_값을_입력_할_경우_구분자는_세미콜론이며_결과_값은_6이_반환되어야한다() {
        assertEquals(cal.add("//;\n1;2;3"), 6);
    }

    @ParameterizedTest(name = "문자열 계산기에 {0}을 넣으면 RuntimeException 처리가 되어야한다")
    @ValueSource(strings = {"-1", "-1111", "-9999", "-15"})
    void _문자열_계산기에_음수를_전달하는_경우_RuntimeException_처리가_되어야한다(String minusValues) {
        assertThrows(RuntimeException.class, () -> cal.add(minusValues));
    }

    @Test
    void subtract() {
        assertEquals(3, cal.subtract(6, 3));
    }

    @AfterEach
    public void after() {
    }
}