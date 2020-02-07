package com.myapp.utils

import com.google.common.truth.Truth.assertThat
import com.myapp.utils.CollectionUtils.merge2List
import org.junit.Test

class CollectionUtilsTest {

  @Test
  fun testMerge2List() {
    var list1: List<Int>? = null
    var list2: List<Int>? = null
    var list: MutableList<Int> = merge2List(list1, list2)
    assertThat(list).isEmpty()

    list1 = null
    list2 = listOf(1, 2)
    list = merge2List(list1, list2)
    assertThat(list).containsExactly(1, 2)

    list1 = listOf(1, 2)
    list2 = null
    list = merge2List(list1, list2)
    assertThat(list).containsExactly(1, 2)

    list1 = listOf()
    list2 = listOf()
    list = merge2List(list1, list2)
    assertThat(list).isEmpty()

    list1 = listOf(1, 2)
    list2 = listOf()
    list = merge2List(list1, list2)
    assertThat(list).containsExactly(1, 2)

    list1 = listOf()
    list2 = listOf(1, 2)
    list = merge2List(list1, list2)
    assertThat(list).containsExactly(1, 2)

    list1 = listOf(1, 2)
    list2 = listOf(11, 12)
    list = merge2List(list1, list2)
    assertThat(list).containsExactly(1, 2, 11, 12)

  }
}