package com.planlegger.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.*
import com.planlegger.core.isInternetAvailable
import com.planlegger.databinding.ActivityMainBinding
import com.planlegger.databinding.DialogInternetBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }
    private val UPDATE_APP_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isInternetAvailable()) {
            setWebView()
            checkAppUpdatesAvailability()
        } else {
            noInternetDialog()
        }
    }
    private fun getUrl():String{
        var baseUrl = "https://planlegger.com/"
        val launchUrl=intent.getStringExtra("url")
        if (launchUrl!=null&& launchUrl.isNotEmpty()&&!launchUrl.equals("null")){
            baseUrl=launchUrl
        }
        return baseUrl
    }

    private fun checkAppUpdatesAvailability() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                             AppUpdateType.IMMEDIATE
                    )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        UPDATE_APP_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_APP_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                }

                Activity.RESULT_CANCELED -> {
                    checkAppUpdatesAvailability()
                }

                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    checkAppUpdatesAvailability()
                }
            }
        }
    }
    
    private fun setWebView() {
        binding.webView.loadUrl(getUrl())
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?,
                                                  request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!mActivity.isFinishing && !mDialog.isShowing) mDialog.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (!mActivity.isFinishing && mDialog.isShowing) mDialog.dismiss()
                if (!mActivity.isFinishing && binding.swipeToRefresh.isRefreshing) binding.swipeToRefresh.isRefreshing =
                    false
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?,
                                         request: WebResourceRequest?,
                                         error: WebResourceError?) {
                if (!mActivity.isFinishing && mDialog.isShowing) mDialog.dismiss()
                if (!mActivity.isFinishing && binding.swipeToRefresh.isRefreshing) binding.swipeToRefresh.isRefreshing =
                    false
                val errorMessage = "Got Error! $error"
                Log.e(TAG, error.toString())
                //showToast(errorMessage)
                super.onReceivedError(view, request, error)
                if (error != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (error.errorCode == 400 || error.errorCode == 403) {
                            clearCookies()
                        }
                    }
                }
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e(TAG, errorResponse.toString())
                if (errorResponse != null) {
                    if (errorResponse.statusCode == 400 || errorResponse.statusCode == 403) {
                        clearCookies()
                    }
                }
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = false
            binding.webView.reload()
        }
    }

    private fun clearCookies() {
        try {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } catch (throwable:Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun noInternetDialog() {
        val dialog = Dialog(mActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val internetDialogBinding = DialogInternetBinding.inflate(layoutInflater)
        dialog.setContentView(internetDialogBinding.root)
        dialog.setCancelable(false)

        internetDialogBinding.cvAllow.setOnClickListener(View.OnClickListener {
            mActivity.finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
        dialog.show()
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            mActivity.finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
