package com.infocorp.data.mapper

import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.DataDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.OldCorporationsDto
import com.infocorp.data.corporationdto.ResumeStateDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
import com.infocorp.domain.model.ResumeState
import javax.inject.Inject

class CorporationMapper @Inject constructor() {
    fun corporationDtoToCorporation(corpDto: CorporationDto?): Corporation {
        return Corporation(
            id = corpDto?.id?: "",
            idFirebase = corpDto?.idFirebase?: "",
            isFavourite = corpDto?.isFavourite?: false,
            isNew = corpDto?.isNew?: false,
            name = corpDto?.name?: "",
            poster = corpDto?.poster?: "",
            description = corpDto?.description?: "",
            address = corpDto?.address?: "",
            phones = corpDto?.phones?: "",
            email = corpDto?.email?: "",
            website = corpDto?.website?: "",
            notes = corpDto?.notes ?: "",
            resumeState = corpDto?.resumeState ?: 0
        )
    }

    fun userCorporationDtoToCorporation(userCorpDto: UserCorporationDto?): Corporation {
        return Corporation(
            id = userCorpDto?.id?: "",
            idFirebase = userCorpDto?.idFirebase?: "",
            isFavourite = userCorpDto?.isFavourite?: false,
            isNew = userCorpDto?.isNew?: false,
            name = userCorpDto?.name?: "",
            poster = userCorpDto?.poster?: "",
            description = userCorpDto?.description?: "",
            address = userCorpDto?.address?: "",
            phones = userCorpDto?.phones?: "",
            email = userCorpDto?.email?: "",
            website = userCorpDto?.website?: "",
            notes = userCorpDto?.notes ?: "",
            resumeState = userCorpDto?.resumeState ?: 0
        )
    }

    fun corporationToUserCorporation(userCorp: Corporation): UserCorporationDto {
        return UserCorporationDto(
            id = userCorp.id,
            idFirebase = userCorp.idFirebase,
            isFavourite = userCorp.isFavourite,
            isNew = userCorp.isNew,
            name = userCorp.name,
            poster = userCorp.poster,
            description = userCorp.description,
            address = userCorp.address,
            phones = userCorp.phones,
            email = userCorp.email,
            website = userCorp.website,
            notes = userCorp.notes,
            resumeState = userCorp.resumeState
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
            website = corp.website,
            notes = corp.notes,
            resumeState = corp.resumeState
        )
    }

    fun corporationDtoToFavouriteCorp(corp: CorporationDto): FavouriteCorporationsDto {
        return FavouriteCorporationsDto(
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

    fun resumeStateDtoToResumeState(resumeDto: ResumeStateDto): ResumeState{
        return ResumeState(
            id = resumeDto.id,
           idCorporation = resumeDto.idCorporation,
            poster = resumeDto.poster,
            title = resumeDto.title,
            dateSent = resumeDto.dateSent,
            dateResponse = resumeDto.dateResponse,
            result = resumeDto.result,
            notes = resumeDto.notes,
            corporation = resumeDto.corporation
        )
    }

    fun resumeStateToResumeStateDto(resume: ResumeState): ResumeStateDto{
        return ResumeStateDto(
            id = resume.id,
            idCorporation = resume.idCorporation,
            poster = resume.poster,
            title = resume.title,
            dateSent = resume.dateSent,
            dateResponse = resume.dateResponse,
            result = resume.result,
            notes = resume.notes,
            corporation = resume.corporation
        )
    }
}