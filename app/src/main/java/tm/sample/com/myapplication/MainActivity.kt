package tm.sample.com.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName

    var mService : Messenger? = null

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            mService = null
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            mService = Messenger(service)
            bound = true
        }
    }

    var bound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onClick = view@{ it:View? ->

            Log.d(TAG, "onClick")

            bindService(Intent(this, MessengerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

            if(!bound)
                return@view

            val msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0)
            try {
                mService!!.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }}

        btn_say_hello.setOnClickListener(onClick)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        /*if(bound){
            unbindService(serviceConnection)
            bound = false
        }*/
    }
}
