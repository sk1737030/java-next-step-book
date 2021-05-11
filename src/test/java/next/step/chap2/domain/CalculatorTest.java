package next.step.chap2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CalculatorTest {

    private Calculator cal;

    @BeforeEach
    public void setUp() {
        cal = new Calculator();
    }

    @Test
    void add() {
        assertEquals(9, cal.add(6, 3));
    }

    @Test
    void subtract() {
        assertEquals(3, cal.subtract(6, 3));
    }
}