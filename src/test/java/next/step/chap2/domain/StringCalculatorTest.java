package next.step.chap2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringCalculatorTest {
    private StringCalculator cal;

    @BeforeEach
    public void setUp() {
        cal = new StringCalculator();
    }

    @ParameterizedTest(name = "문자열을 쉼표 또는 콜론 구분자를 기준으로 분리한 값 {0}의 합을 반환한다")
    @MethodSource("numberSet")
    void 문자열을_쉼표_또는_콜론_구분자를_기준으로_분리한_각_n의_합을_반환한다(int answer, String values) {
        assertEquals(cal.add(values), answer);
    }

    private static List<Arguments> numberSet() {
        return Arrays.asList(
                Arguments.of(0, null),
                Arguments.of(0, ""),
                Arguments.of(3, "1,2"),
                Arguments.of(6, "1,2,3"),
                Arguments.of(6, ",1,2:3")
        );
    }

    @ParameterizedTest(name = "커스텀 구분자를 포함한 값을 입력 할 경우 이 {0}의 합이 반환되어야한다")
    @MethodSource("numberSet2")
    void _커스텀_구분자를_포함한_N_입력_할_경우_구분자는_세미콜론이며_결과_n이_반환되어야한다(int answer, String values) {
        assertEquals(answer, cal.add(values));
    }

    private static List<Arguments> numberSet2() {
        return Arrays.asList(
                Arguments.of(6, "//;\n1;2;3"),
                Arguments.of(6, "//;\n1;2//;\n3")
        );
    }

    @ParameterizedTest(name = "문자열 계산기에 {0}을 넣으면 RunstimeException 처리가 되어야한다")
    @ValueSource(strings = {"-1", "-1111", "-9999", "-15"})
    void _문자열_계산기에_음수를_전달하는_경우_RuntimeException_처리가_되어야한다(String minusValues) {
        assertThrows(RuntimeException.class, () -> cal.add(minusValues));
    }

}