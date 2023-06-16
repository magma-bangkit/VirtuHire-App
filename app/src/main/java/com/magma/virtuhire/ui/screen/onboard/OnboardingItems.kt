package com.magma.virtuhire.ui.screen.onboard

import com.magma.virtuhire.R

class OnboardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnboardingItems>{
            return listOf(
                OnboardingItems(R.drawable.slide1, R.string.onBoardingTitle1, R.string.onBoardingText1),
                OnboardingItems(R.drawable.slide1, R.string.onBoardingTitle2, R.string.onBoardingText2),
                OnboardingItems(R.drawable.slide1, R.string.onBoardingTitle3, R.string.onBoardingText3)
            )
        }
    }
}