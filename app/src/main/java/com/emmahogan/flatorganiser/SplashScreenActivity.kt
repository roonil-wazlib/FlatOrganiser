package com.emmahogan.flatorganiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    //TODO open on this activity, if user already logged in create user class instance and only load rest of app when this is done
    //TODO else got to MainActivity, and create user class instance in login/signup screens
    //TODO should remove loading issue on HomeActivity
}