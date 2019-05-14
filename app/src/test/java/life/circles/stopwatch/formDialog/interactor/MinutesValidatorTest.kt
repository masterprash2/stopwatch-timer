package life.circles.stopwatch.formDialog.interactor

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class MinutesValidatorTest {

    @Before
    fun setUp() {
    }

    @Test
    fun testNegativeInput() {
        val validate = MinutesValidator.validate("-12")
        assertFalse(validate.first)
        assertEquals("Duration should be between 1 to 60",validate.second)
        assertEquals(0,validate.third)
    }

    @Test
    fun testValid() {
        for(num in 1..60) {
            val validate = MinutesValidator.validate(num.toString())
            assertTrue(validate.first)
            assertNull(validate.second)
            assertEquals(num, validate.third)
        }
    }

    @Test
    fun testGreaterThan60() {
        val validate = MinutesValidator.validate("61")
        assertFalse(validate.first)
        assertEquals("Duration should be less than 60",validate.second)
        assertEquals(60,validate.third)
    }

    @Test
    fun testAlphaNumeric() {
        val validate = MinutesValidator.validate("abc")
        assertFalse(validate.first)
        assertNotNull(validate.second)
        assertEquals(0,validate.third)
    }

    @Test
    fun testDecimal() {
        val validate = MinutesValidator.validate("1.2")
        assertFalse(validate.first)
        assertNotNull(validate.second)
        assertEquals(0,validate.third)
    }

    @After
    fun tearDown() {
    }
}