package com.myapp.ui

import leakcanary.AppWatcher

object ThirdPartyLibInit {

  // Initialize for all third party libs like LeakCanary.
  // The reason why we use flavor instead of Dagger to inject is because using Dagger, we must
  // `implement` unnecessary these dependencies into build.gradle, while using flavor, only
  // needed flavor will have `flavorNameImplement` to include these dependencies.
  // This is `dev` flavor, and we need to enable LeakCanary
  fun init() {
    AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false)
  }

}