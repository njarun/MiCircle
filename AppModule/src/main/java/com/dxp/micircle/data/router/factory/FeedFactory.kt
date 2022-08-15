package com.dxp.micircle.data.router.factory

import com.dxp.micircle.data.router.source.FeedsEntityData
import com.dxp.micircle.data.router.source.local.LocalFeedsEntityData
import com.dxp.micircle.data.router.source.netowrk.NetworkFeedsEntityData
import javax.inject.Inject

class FeedFactory @Inject constructor(private val networkFeedsEntityData: NetworkFeedsEntityData,
                                      private val localFeedsEntityData: LocalFeedsEntityData) {

    fun create(source: Source): FeedsEntityData {

        return when (source) {

            Source.NETWORK -> networkFeedsEntityData
            else -> localFeedsEntityData
        }
    }
}