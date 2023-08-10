package com.planlegger.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.planlegger.core.CustomProgressDialog

/* Use this in Activities instead of startActivityForResult & requesting permissions
Intent intent = new Intent(this, NewActivity.class);
activityLauncher.launch(intent, result -> {
    if (result.getResultCode() == Activity.RESULT_OK)

});
*/
open class BaseActivity : AppCompatActivity(), View.OnClickListener {
    var mActivity= Activity()
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    lateinit var mDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        mDialog = CustomProgressDialog(this)
    }

    fun setUpClicks(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {}
}