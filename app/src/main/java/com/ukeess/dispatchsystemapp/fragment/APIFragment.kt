package com.ukeess.dispatchsystemapp.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.adapters.APILogsAdapter
import com.ukeess.dispatchsystemapp.enums.APIStatus
import com.ukeess.dispatchsystemapp.enums.LogType
import com.ukeess.dispatchsystemapp.model.APILogsInfo
import com.ukeess.dispatchsystemapp.model.api.*
import com.ukeess.dispatchsystemapp.utils.SeparatorDecoration
import com.ukeess.dispatchsystemapp.utils.getCurrentTime
import kotlinx.android.synthetic.main.fragment_api.*

class APIFragment : Fragment(), APILogsAdapter.ItemClickListener {

    var logList = ArrayList<APILogsInfo>()
    var adapter: APILogsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiMessageList.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        apiMessageList.addItemDecoration(SeparatorDecoration(context!!, Color.GRAY, 1.5f))
        apiMessageList.setHasFixedSize(true)
        adapter = APILogsAdapter(context!!, logList, this)
        apiMessageList.adapter = adapter

        btPimStatus.setOnClickListener(clickListener)
        btStartTrip.setOnClickListener(clickListener)
        btUpdateTrip.setOnClickListener(clickListener)
        btRequestPayment.setOnClickListener(clickListener)
        btPaymentStatus.setOnClickListener(clickListener)
        btFinishTrip.setOnClickListener(clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api, container, false)
    }


    override fun onItemClick(itemPosition: Int) {
        val alertDialog = AlertDialog.Builder(context!!)
        alertDialog.setTitle(logList[itemPosition].typeMessage.name + "  ------> " + logList[itemPosition].message)
        alertDialog.setMessage(logList[itemPosition].jsonData)
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
        })
        alertDialog.show()
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btPimStatus -> {
                Toast.makeText(context, "Clicked btPimStatus", Toast.LENGTH_SHORT).show()
                createRequestApi(APIStatus.PIM_STATUS)
            }
            R.id.btStartTrip -> {
                Toast.makeText(context, "Clicked btStartTrip", Toast.LENGTH_SHORT).show()
                val paymentInfo = PaymentInfo(420, 580, 1000)
                val driverInfo = DriverInfo("990", "Jacque", "Fresco", "1916", "not image")
                val startTrip = StartTripRequest(getCurrentTime(), APIStatus.START_TRIP.name, paymentInfo, "2406782A", driverInfo)
                logList.add(0, APILogsInfo("STATUS_TRIP", getGsonBuilder().toJson(startTrip), LogType.REQUEST, getCurrentTime()))
                adapter?.notifyDataSetChanged()
            }
            R.id.btUpdateTrip -> {
                Toast.makeText(context, "Clicked btUpdateTrip", Toast.LENGTH_SHORT).show()
                val paymentInfo = PaymentInfo(420, 580, 1000)
                val updateTripRequest = UpdateTripRequest(getCurrentTime(), APIStatus.START_TRIP.name, paymentInfo)
                logList.add(0, APILogsInfo("UPDATE_TRIP", getGsonBuilder().toJson(updateTripRequest), LogType.REQUEST, getCurrentTime()))
                adapter?.notifyDataSetChanged()
            }
            R.id.btRequestPayment -> {
                Toast.makeText(context, "Clicked btRequestPayment", Toast.LENGTH_SHORT).show()
                val paymentInfo = PaymentInfo(420, 580, 1000)
                val requestPayment = RequestPayment(getCurrentTime(), APIStatus.START_TRIP.name, paymentInfo, true, false)
                logList.add(0, APILogsInfo("REQUEST_PAYMENT", getGsonBuilder().toJson(requestPayment), LogType.REQUEST, getCurrentTime()))
                adapter?.notifyDataSetChanged()
            }
            R.id.btPaymentStatus -> {
                Toast.makeText(context, "Clicked btPaymentStatus", Toast.LENGTH_SHORT).show()
                val paymentStatus = BaseRequest(getCurrentTime(), APIStatus.PAYMENT_STATUS.name)
                logList.add(0, APILogsInfo(APIStatus.PAYMENT_STATUS.name, getGsonBuilder().toJson(paymentStatus), LogType.REQUEST, getCurrentTime()))
                adapter?.notifyDataSetChanged()
            }
            R.id.btFinishTrip -> {
                Toast.makeText(context, "Clicked btFinishTrip", Toast.LENGTH_SHORT).show()
                val pimStatus = BaseRequest(getCurrentTime(), APIStatus.FINISH_TRIP.name)
                logList.add(0, APILogsInfo(APIStatus.FINISH_TRIP.name, getGsonBuilder().toJson(pimStatus), LogType.REQUEST, getCurrentTime()))
                adapter?.notifyDataSetChanged()
            }
        }
    }

    fun createRequestApi(apiStatus: APIStatus) {
        when(apiStatus){
            APIStatus.PIM_STATUS ->logList.add(0, APILogsInfo(APIStatus.PIM_STATUS.name, getGsonBuilder().toJson(BaseRequest(getCurrentTime(), apiStatus.name)), LogType.REQUEST, getCurrentTime()))
            APIStatus.PIM_STATUS -> print("fd")
            APIStatus.PIM_STATUS -> print("fd")
            APIStatus.PIM_STATUS -> print("fd")
            APIStatus.PIM_STATUS -> print("fd")
        }
        adapter?.notifyDataSetChanged()
    }

    private fun getGsonBuilder() = GsonBuilder().setPrettyPrinting().create()

}

