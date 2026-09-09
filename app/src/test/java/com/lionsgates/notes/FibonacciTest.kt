package com.lionsgates.notes

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FibonacciTest {

    @Before
    fun setUp() {

    }

    private fun fibonacciUsingRecursion(num: Int) : Int {
        return if (num <= 1) {
            return num
        } else {
            fibonacciUsingRecursion(num - 1) + fibonacciUsingRecursion(num - 2)
        }
    }

    @Test
    fun check_if_fibonacci_Using_Recursion_Return_Correct_Value() {
        assertThat(fibonacciUsingRecursion(8)).isEqualTo(21)
    }
}