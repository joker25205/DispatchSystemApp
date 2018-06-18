package com.ukeess.dispatchsystemapp

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothServerImpl
import com.ukeess.dispatchsystemapp.bluetooth.ConnectionFragment
import com.ukeess.dispatchsystemapp.bluetooth.MeterTunnelFragment
import com.ukeess.dispatchsystemapp.enums.BluetoothServerType
import com.ukeess.dispatchsystemapp.fragment.APIFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    val DEVICE_NAME = "PimServer"
    val TUNNEL_SEVER_NAME = "TunnelServer"
    val UUID_PIM = UUID.fromString("895a86e2-6a31-11e8-adc0-fa7ae01bbebc")
    val UUID_TUNNEL = UUID.fromString("c90c5b86-536f-11e8-9c2d-fa7ae01bbebc")

    var meterTunnelFragment: MeterTunnelFragment? = null
    var connectionFragment: ConnectionFragment? = null
    var apiFragment: APIFragment? = null

    private val onNavigationView = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.connection_device -> {
                if (!item.isChecked) {
                    replaceFragment(connectionFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.tunnel_communication -> {
                if (!item.isChecked) {
                    replaceFragment(meterTunnelFragment)
                }
                return@OnNavigationItemSelectedListener true
            } R.id.api_communication -> {
                if (!item.isChecked) {
                    replaceFragment(apiFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectionFragment = ConnectionFragment()
        meterTunnelFragment = MeterTunnelFragment()
        apiFragment = APIFragment()

        val meterTunelServer = BluetoothServerImpl(TUNNEL_SEVER_NAME, UUID_TUNNEL, meterTunnelFragment!!, BluetoothServerType.METER_TUNNEL)

        meterTunnelFragment?.setDataSender(meterTunelServer)

        connectionFragment?.setMeterTunnelServer(meterTunelServer)

        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.frame_layout)
        if (fragment == null) {
            manager.beginTransaction().add(R.id.frame_layout, connectionFragment).commit()
        }

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(onNavigationView)
        navigationView.setCheckedItem(R.id.connection_device)

    }

    fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

}
