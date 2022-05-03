package com.example.healthandfitness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthandfitness.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)

                    val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(i)

                    finish()
                } catch (e: Exception) {
                }
            }
        }
        background.start()

    }
}