package com.example.healthandfitness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.healthandfitness.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var currentPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("Vinesh", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("Vinesh", "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@LoginActivity,"Invalid Phone Number",Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@LoginActivity,"Too Many requests",Toast.LENGTH_SHORT).show()
                }
                binding.apply{
                    btnGetOtpLoginActivity.visibility = View.VISIBLE
                    btnProgressBarLoginActivity.visibility = View.INVISIBLE
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d("Vinesh", "onCodeSent:$verificationId")

                binding.apply {
                    edtTxtPhoneLoginActivity.visibility = View.GONE
                    edtTxtOtpLoginActivity.visibility = View.VISIBLE
                    btnSubmitLoginActivity.visibility = View.VISIBLE
                    btnProgressBarLoginActivity.visibility = View.INVISIBLE
                }
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        binding.apply{
            btnGetOtpLoginActivity.setOnClickListener {

                currentPhoneNumber = "+91${edtTxtPhoneLoginActivity.text}"
                if(currentPhoneNumber.isNotEmpty() && currentPhoneNumber.length==13){
                    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("$currentPhoneNumber")
                        .setTimeout(10L, TimeUnit.SECONDS)
                        .setActivity(this@LoginActivity)
                        .setCallbacks(callbacks)
                        .build()
                    btnGetOtpLoginActivity.visibility = View.INVISIBLE
                    btnProgressBarLoginActivity.visibility = View.VISIBLE
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
                else{
                    Toast.makeText(this@LoginActivity,"Invalid Phone Number",Toast.LENGTH_SHORT).show()
                }

            }
            btnSubmitLoginActivity.setOnClickListener {

                val code = edtTxtOtpLoginActivity.text.toString().trim()
                val credential = PhoneAuthProvider.getCredential(storedVerificationId,code)
                btnSubmitLoginActivity.visibility = View.INVISIBLE
                btnProgressBarLoginActivity.visibility = View.VISIBLE
                signInWithPhoneAuthCredential(credential)

            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { signInTask ->
            if (signInTask.isSuccessful) {
                Log.d("Vinesh", "signInWithCredential:success")
                val userData = HashMap<String, Any>()
                userData["phoneNumber"] = currentPhoneNumber
                userData["age"] = 0
                userData["weight"] = 0
                userData["height"] = 0
                userData["calorieIntake"] = 0
                db.collection("users").document(firebaseAuth.currentUser?.uid!!).get().addOnCompleteListener{ checkUserAlreadyExists ->
                    if(checkUserAlreadyExists.result.exists()){
                        Intent(this@LoginActivity, MainActivity::class.java).also{
                            startActivity(it)
                        }
                    }
                    else{
                        db.collection("users").document(firebaseAuth.currentUser?.uid!!).set(userData).addOnCompleteListener { userDataTask ->
                            if(userDataTask.isSuccessful){
                                Intent(this@LoginActivity, InputUserDetailsActivity::class.java).also{
                                    startActivity(it)
                                }
                            }
                            else{
                                Toast.makeText(this@LoginActivity,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }

            }
            else {
                Log.w("Vinesh", "signInWithCredential:failure", signInTask.exception)
                if (signInTask.exception is FirebaseAuthInvalidCredentialsException) {
                    binding.apply {
                        btnSubmitLoginActivity.visibility = View.VISIBLE
                        btnProgressBarLoginActivity.visibility = View.INVISIBLE
                        edtTxtOtpLoginActivity.setText("")
                    }
                    Toast.makeText(this@LoginActivity,"Incorrect OTP",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}