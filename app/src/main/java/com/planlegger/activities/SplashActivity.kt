package com.planlegger.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.planlegger.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay

class
SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val data: Uri? = intent.data
        launchSplash(data.toString())
    }

    private fun launchSplash(url: String) {
        lifecycleScope.launchWhenResumed {
            delay(3000)
            // move ahead
            val intent=Intent(mActivity, MainActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            mActivity.finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}