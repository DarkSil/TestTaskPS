package com.example.testtaskps.modules

import javax.inject.Qualifier

object Qualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AccountsDataStore

}