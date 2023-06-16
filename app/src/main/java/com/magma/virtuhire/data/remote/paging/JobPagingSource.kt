package com.magma.virtuhire.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.response.JobOpeningResponse

class JobPagingSource(
    private val apiService: ApiService,
): PagingSource<Int, JobOpeningResponse>() {
    override fun getRefreshKey(state: PagingState<Int, JobOpeningResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JobOpeningResponse> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getAllJobs(page = page)

            LoadResult.Page(
                data = response.data,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.data.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}