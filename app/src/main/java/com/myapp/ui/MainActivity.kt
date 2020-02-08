package com.myapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.myapp.R

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.navigationHost).navigateUp() || super.onSupportNavigateUp()
  }

}
