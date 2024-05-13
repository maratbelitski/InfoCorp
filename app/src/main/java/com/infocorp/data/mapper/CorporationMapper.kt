package com.infocorp.data.mapper

import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.DataDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.OldCorporationsDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
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

    fun userCorporationDtoToCorporation(userCorpDto: UserCorporationDto): Corporation {
        return Corporation(
            id = userCorpDto.id,
            idFirebase = userCorpDto.idFirebase,
            isFavourite = userCorpDto.isFavourite,
            isNew = userCorpDto.isNew,
            name = userCorpDto.name,
            poster = userCorpDto.poster,
            description = userCorpDto.description,
            address = userCorpDto.address,
            phones = userCorpDto.phones,
            email = userCorpDto.email,
            website = userCorpDto.website
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

    fun corporationDtoToNewCorp(corp: CorporationDto): OldCorporationsDto {
        return OldCorporationsDto(
            id = corp.id
        )
    }

    fun corporationToOldCorp(corp: Corporation): OldCorporationsDto {
        return OldCorporationsDto(
            id = corp.id
        )
    }

    fun dataDtoToData(dataDto: DataDto): Data {
        return Data(
            titleCorp = dataDto.titleCorp ?: "",
            fioPerson = dataDto.fioPerson ?: "",
            type = dataDto.type ?: "",
            direction = dataDto.direction ?: "",
            unp = dataDto.unp ?: "",
            address = dataDto.address ?: "",
            status = dataDto.status ?: ""
        )
    }
}