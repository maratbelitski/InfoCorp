package com.infocorp.utils

enum class Constants(val value:String) {
    DATABASE_NAME("corporation.db"),
    BASE_URL("http://suggestions.dadata.ru/suggestions/api/4_1/rs/"),
    AUTHORIZATION_TOKEN("Authorization: Token 6ebaf8e334bac982c60b1ba2fae777febca0303f"),

    INFO_CORP_PREFERENCES ("info_corp_preferences"),

    THEME_PARAMS_PREFERENCES ("themeParams"),
    NIGHT_MODE("33"),
    LIGHT_MODE("17"),

    TITLE_CV("titleCV"),
    BODY_CV("bodyCV"),

    LANG_PARAMS_PREFERENCES ("language"),
    LANG_RU ("ru"),
    LANG_EN ("en"),

    SLEEP("1000"),

    NOT_SPECIFIED("Not specified")
}