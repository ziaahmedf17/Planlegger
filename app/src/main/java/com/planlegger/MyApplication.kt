package com.planlegger

import android.app.Application
import com.onesignal.OneSignal

class MyApplication : Application() {
    private val ONESIGNAL_APP_ID = "d87b038c-0e58-49a5-90a0-97cbd846cdbe"

    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.promptForPushNotifications();
    }

}