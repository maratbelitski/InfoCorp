package com.infocorp.data.mapper

import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.NewCorporationsDto
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class CorporationMapper @Inject constructor() {
    fun corporationDtoToCorporation(corpDto: CorporationDto): Corporation {
        return Corporation(
            id = corpDto.id,
            idFirebase = corpDto.idFirebase,
            isFavourite = corpDto.isFavourite,
            isNew = corpDto.isNew,
            name = corpDto.name,
            poster = corpDto.poster,
            description = corpDto.description,
            address = corpDto.address,
            phones = corpDto.phones,
            email = corpDto.email,
            website = corpDto.website
        )
    }

    fun corporationToCorporationDto(corp: Corporation): CorporationDto {
        return CorporationDto(
            id = corp.id,
            idFirebase = corp.idFirebase,
            isFavourite = corp.isFavourite,
            isNew = corp.isNew,
            name = corp.name,
            poster = corp.poster,
            description = corp.description,
            address = corp.address,
            phones = corp.phones,
            email = corp.email,
            website = corp.website
        )
    }

    fun corporationDtoToFavouriteCorp(corp: CorporationDto): FavouriteCorporationsDto {
        return FavouriteCorporationsDto(
            id = corp.id
        )
    }

    fun corporationDtoToNewCorp(corp: CorporationDto): NewCorporationsDto {
        return NewCorporationsDto(
            id = corp.id
        )
    }

    fun corporationToNewCorp(corp: Corporation): NewCorporationsDto {
        return NewCorporationsDto(
            id = corp.id
        )
    }
}