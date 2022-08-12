package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.domain.router.model.MediaModel
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.MediaType
import java.io.File
import java.util.*

object MiMediaMapper {

    fun List<MiMedia>.toMediaModelList(): ArrayList<MediaModel> {

        val modelList = ArrayList<MediaModel>()

        forEach {

            it.path?.let {

                modelList.add(
                    MediaModel(
                        UUID.randomUUID().toString(),
                        "",
                        MediaType.IMAGE.value, //@Todo Hardcoded for now, when the other media types are enabled from picker, update this -- nj
                        it,
                        File(it).length()))
            }
        }

        return modelList
    }
}