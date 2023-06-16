package com.magma.virtuhire.ui.screen.introduction

sealed class IntroductionStep {
    object Birthday : IntroductionStep()
    object Education : IntroductionStep()
    object Skills : IntroductionStep()
    object City : IntroductionStep()
    object PreferredJobType : IntroductionStep()
    object Salary : IntroductionStep()
    object PreferredCities : IntroductionStep()
    object PreferredJobCategories : IntroductionStep()
    object Resume: IntroductionStep()
}