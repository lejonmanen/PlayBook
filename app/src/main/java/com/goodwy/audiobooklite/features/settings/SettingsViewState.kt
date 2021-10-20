package com.goodwy.audiobooklite.features.settings

data class SettingsViewState(
  val useDarkTheme: Boolean,
  val showDarkThemePref: Boolean,
  val resumeOnReplug: Boolean,
  //val seekTimeInSeconds: Int,
  //val autoRewindInSeconds: Int,
  val tintNavBar: Boolean,
  val screenOrientationPref: Boolean,
  val gridViewAutoPref: Boolean
)