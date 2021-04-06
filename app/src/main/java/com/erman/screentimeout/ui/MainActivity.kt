package com.erman.screentimeout.ui

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.erman.screentimeout.R
import com.erman.screentimeout.databinding.ActivityMainBinding
import com.erman.screentimeout.model.AdminReceiver
import com.erman.screentimeout.util.ACTIVITY_RES_ENABLE
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    val viewModel: ViewModel by inject()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        binding.viewModel = viewModel

        viewModel.activateAdmin.observe(this, Observer {
            val compName = ComponentName(this, AdminReceiver::class.java)
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                ""
            )
            startActivityForResult(intent, ACTIVITY_RES_ENABLE)
        })

        viewModel.hideKeyboard.observe(this, Observer {
            val view: View? = this.currentFocus
            if (view != null) {
                val inputMethodManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTIVITY_RES_ENABLE -> if (resultCode == RESULT_OK) {
                Toast.makeText(
                    this,
                    getString(R.string.device_admin_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.device_admin_disabled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshAlarmState()
    }
}