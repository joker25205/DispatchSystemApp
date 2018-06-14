package com.ukeess.dispatchsystemapp

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothConnectionFragment
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothServerImpl
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothServerType
import com.ukeess.dispatchsystemapp.bluetooth.MeterTunnelFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    val DEVICE_NAME = "PimServer"
    val TUNNEL_SEVER_NAME = "TunnelServer"
    var sUUID_PIM = UUID.fromString("895a86e2-6a31-11e8-adc0-fa7ae01bbebc")
    var sUUID_Tunnel = UUID.fromString("c90c5b86-536f-11e8-9c2d-fa7ae01bbebc")

    var tunnelCommunicationFragment: MeterTunnelFragment? = null
    var bluetoothConnectionFragment: BluetoothConnectionFragment? = null

    private val mOnNavigationView = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.connection_device -> {
                if (!item.isChecked) {
                    replaceFragment(bluetoothConnectionFragment)
                    Toast.makeText(baseContext, "Connection Click", Toast.LENGTH_SHORT).show()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.tunnel_communication -> {
                if (!item.isChecked) {
                    replaceFragment(tunnelCommunicationFragment)
                    Toast.makeText(baseContext, "Tunnel Click", Toast.LENGTH_SHORT).show()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothConnectionFragment = BluetoothConnectionFragment()
        tunnelCommunicationFragment = MeterTunnelFragment()

        val btServerPim = BluetoothServerImpl(TUNNEL_SEVER_NAME, sUUID_Tunnel, tunnelCommunicationFragment!!, BluetoothServerType.METER_TUNNEL)

        tunnelCommunicationFragment?.setBluetoothServerSendData(btServerPim)

        bluetoothConnectionFragment?.setBluetoothServerTunnel(btServerPim)

        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.frame_layout)
        if (fragment == null) {
            manager.beginTransaction().add(R.id.frame_layout, bluetoothConnectionFragment).commit()

        }

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(mOnNavigationView)
        navigationView.setCheckedItem(R.id.connection_device)


    }

    fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.menu_red -> {
//                return true
//            }
//            R.id.menu_green -> {
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//
//    }


}
