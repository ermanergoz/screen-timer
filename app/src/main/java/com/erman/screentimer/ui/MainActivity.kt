package com.erman.screentimer.ui

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
import com.erman.screentimer.R
import com.erman.screentimer.databinding.ActivityMainBinding
import com.erman.screentimer.model.AdminReceiver
import com.erman.screentimer.util.ACTIVITY_RES_ENABLE
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        binding.viewModel = viewModel

        viewModel.activateAdmin.observe(this, { event ->
            event.getContentIfNotHandled()?.let { requestAdminRights() }
        })

        viewModel.hideKeyboard.observe(this, { event ->
            event.getContentIfNotHandled()?.let { hideKeyboard() }
        })
    }

    private fun requestAdminRights() {
        val compName = ComponentName(this, AdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            getString(R.string.device_admin_explanation)
        )
        startActivityForResult(intent, ACTIVITY_RES_ENABLE)
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTIVITY_RES_ENABLE && resultCode != RESULT_OK) {
            Toast.makeText(
                this,
                getString(R.string.device_admin_disabled),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshAlarmState()
    }
}