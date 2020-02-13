package com.myapp.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class CoroutinesTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  fun runCoroutineTest(block: suspend () -> Unit) {
    mainCoroutineRule.runBlocking {
      block()
    }
  }
}