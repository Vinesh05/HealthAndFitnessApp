package com.example.healthandfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthandfitness.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply{
            btnGetOtpLoginActivity.setOnClickListener {

                btnSubmitLoginActivity.visibility = View.VISIBLE
                btnGetOtpLoginActivity.visibility = View.INVISIBLE

                edtTxtOtpLoginActivity.visibility = View.VISIBLE
                edtTxtPhoneLoginActivity.visibility = View.INVISIBLE

            }
        }

    }
}