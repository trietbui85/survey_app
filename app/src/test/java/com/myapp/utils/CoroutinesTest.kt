package com.myapp.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class CoroutinesTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  @ExperimentalCoroutinesApi
  fun runCoroutineTest(block: suspend () -> Unit) {
    mainCoroutineRule.runBlocking {
      block()
    }
  }
}