package com.infocorp.data.mapper

import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.domain.Corporation
import javax.inject.Inject

class CorporationMapper @Inject constructor() {
    fun corporationDtoToCorporation(corpDto: CorporationDto): Corporation {
        return Corporation(
            id = corpDto.id,
            idFirebase = corpDto.idFirebase,
            name = corpDto.name,
            poster = corpDto.poster,
            description = corpDto.description,
            address = corpDto.address,
            phones = corpDto.phones,
            email = corpDto.email,
            website = corpDto.website
        )
    }
}