package com.deloitte.testCasePractices.ui

import com.deloitte.testCasePractices.model.Repository
import org.junit.*

class TestListAdapter {

    private val listAdapter = ListAdapter()
    private lateinit var mockDataSet: List<Repository>
    @Before
    fun before() {
        mockDataSet = listOf(Repository(), Repository(), Repository())
    }

    @Test
    fun testSetData_withMockDataset() {
        listAdapter.setData(mockDataSet)
        val actualValue = listAdapter.itemCount
        val expectedValue = mockDataSet.size
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun testSetData_withEmptyList() {
        listAdapter.setData(emptyList())
        val actualValue = listAdapter.itemCount
        val expectedValue = 0
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun testClearData_forClearingSetData() {
        listAdapter.setData(mockDataSet)
        var actualValue = listAdapter.itemCount
        var expectedValue = mockDataSet.size
        Assert.assertEquals(expectedValue, actualValue)
        listAdapter.clearData()
        actualValue = listAdapter.itemCount
        expectedValue = 0
        Assert.assertEquals(expectedValue, actualValue)
    }
}