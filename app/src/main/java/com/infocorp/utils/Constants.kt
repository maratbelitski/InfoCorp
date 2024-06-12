package com.infocorp.utils

enum class Constants(val value: String) {
    DATABASE_NAME("corporation.db"),
    BASE_URL("http://suggestions.dadata.ru/suggestions/api/4_1/rs/"),
    AUTHORIZATION_TOKEN("Authorization: Token 6ebaf8e334bac982c60b1ba2fae777febca0303f"),

    GENERAL_DB("CORPORATION"),
    USER_DB("USER_IT_CORPORATION"),

    INFO_CORP_PREFERENCES("info_corp_preferences"),

    THEME_PARAMS_PREFERENCES("themeParams"),
    NIGHT_MODE("33"),
    LIGHT_MODE("17"),

    TITLE_CV("titleCV"),
    BODY_CV("bodyCV"),
    LINK_CV("linkCV"),

    LANG_PARAMS_PREFERENCES("language"),
    LANG_RU("ru"),
    LANG_EN("en"),

    BELARUS_LOCATION("geo:0,0?q= Беларусь"),
    GOOGLE_PACKAGE("com.google.android.apps.maps"),

    NOT_SPECIFIED("Not specified")
}