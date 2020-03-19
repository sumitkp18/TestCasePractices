package com.deloitte.testCasePractices.adapters

import com.deloitte.testCasePractices.adapter.ListAdapter
import com.deloitte.testCasePractices.model.Repository
import org.junit.*

/**
 * Test cases for [ListAdapter]
 */
class TestListAdapter {

    private val listAdapter = ListAdapter()
    private lateinit var mockDataSet: List<Repository>
    @Before
    fun before() {
        mockDataSet = listOf(Repository(), Repository(), Repository())
    }

    /**
     * test case for setData method to check setting of mock data in adapter dataset
     */
    @Test
    fun testSetData_withMockDataset() {
        listAdapter.setData(mockDataSet)
        val actualValue = listAdapter.itemCount
        val expectedValue = mockDataSet.size
        Assert.assertEquals(expectedValue, actualValue)
    }

    /**
     * test case for setData method to check setting of empty list to adapter dataset
     */
    @Test
    fun testSetData_withEmptyList() {
        listAdapter.setData(emptyList())
        val actualValue = listAdapter.itemCount
        val expectedValue = 0
        Assert.assertEquals(expectedValue, actualValue)
    }

    /**
     * test case for clearData method
     */
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