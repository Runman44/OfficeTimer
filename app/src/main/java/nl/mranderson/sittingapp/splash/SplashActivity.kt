package nl.mranderson.sittingapp.splash

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.mranderson.sittingapp.NavigationActivity
import nl.mranderson.sittingapp.timer.service.TimerService
import nl.mranderson.sittingapp.timer.stop.TimerActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isMyServiceRunning(TimerService::class.java)) {
            val intent = TimerActivity.newInstance(this)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }
}