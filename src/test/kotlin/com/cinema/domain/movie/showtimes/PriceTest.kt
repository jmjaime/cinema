package com.cinema.domain.movie.showtimes

import com.cinema.domain.movie.Price
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class PriceTest {

    @Test
    fun `can create valid price`() {
        Assertions.assertEquals(BigDecimal.TEN, Price(BigDecimal.TEN).amount)
    }

    @Test
    fun `price should be positive`() {
        val error = assertThrows<IllegalStateException> { Price(BigDecimal.valueOf(-1)) }
        Assertions.assertEquals("price should be grater than 0", error.message)
    }
}