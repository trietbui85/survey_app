package com.myapp.ui

object ThirdPartyLibInit {

  // Initialize for all third party libs like LeakCanary.
  // The reason why we use flavor instead of Dagger to inject is because using Dagger, we must
  // `implement` unnecessary these dependencies into build.gradle, while using flavor, only
  // needed flavor will have `flavorNameImplement` to include these dependencies.
  // And this is `prod` flavor, but we don't have any third party libs to init.
  fun init() {}

}