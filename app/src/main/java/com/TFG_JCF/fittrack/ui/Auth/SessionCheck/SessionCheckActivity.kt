package com.TFG_JCF.fittrack.ui.Auth.SessionCheck

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.TFG_JCF.fittrack.databinding.ActivitySessionCheckBinding
import com.TFG_JCF.fittrack.ui.Auth.SignUpActivity
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionCheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionCheckBinding
    private val vm: SessionCheckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkSession()
    }

    private fun checkSession() {
        vm.checkSessionAndProfile { destination ->
            runOnUiThread {
                when (destination) {
                    SessionDestination.LOGIN -> {
                        val intent = Intent(this, SignUpActivity::class.java)
                        intent.putExtra("startDestination", "login")
                        startActivity(intent)
                        finish()
                    }

                    SessionDestination.ONBOARDING -> {
                        val intent = Intent(this, SignUpActivity::class.java)
                        intent.putExtra("startDestination", "goal")
                        startActivity(intent)
                        finish()
                    }

                    SessionDestination.HOME -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}