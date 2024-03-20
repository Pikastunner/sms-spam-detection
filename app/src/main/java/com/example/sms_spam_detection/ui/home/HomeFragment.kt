package com.example.sms_spam_detection.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_spam_detection.MyNotification
import com.example.sms_spam_detection.R
import com.example.sms_spam_detection.databinding.FragmentHomeBinding
import com.example.sms_spam_detection.ui.notifications.NotificationsAdapter
import com.example.sms_spam_detection.ui.notifications.NotificationsViewModel
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.internal.`$Gson$Types`.arrayOf
import com.google.gson.reflect.TypeToken


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val MY_PERMISSIONS_REQUEST_SMS = 123


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.isSmsPermissionGranted.observe(viewLifecycleOwner) { isGranted ->
            updateUI(isGranted)
        }

        val enablePermissionsButton: Button = binding.enablePermissions

        // Set OnClickListener to request permissions when the button is clicked
        enablePermissionsButton.setOnClickListener {
            Log.d("Clicked", "THE BUTTON IS CLICKED LINE 52")
            requestSmsPermissions()
            //viewModel.updateSmsPermissionStatus(isSmsPermissionGranted())
        }

    }

    private fun updateUI(isSmsPermissionsGranted: Boolean) {
        val textView: TextView = binding.textHome
        val imageView: ImageView = binding.bird
        val enablePermissionsButton: Button = binding.enablePermissions

        if (isSmsPermissionsGranted) {
            textView.text = "Enjoy detecting spam"
            imageView.setImageResource(R.drawable.chick)
            enablePermissionsButton.visibility = View.GONE
        }
        else {
            textView.text =
                "SMS permission is not granted. Please grant the permission in settings to start detecting spam."
            imageView.setImageResource(R.drawable.sad)
            enablePermissionsButton.visibility = View.VISIBLE
        }
    }

    // Function to check if SMS permissions are granted
    private fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            // Use the application context to avoid leaks
            requireActivity(),
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        /*homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SMS -> {
                // Handle the result of the permission request
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, perform the required actions
                    viewModel.updateSmsPermissionStatus(isSmsPermissionGranted())
                } else {
                    // Permission denied, handle accordingly
                    // Change the UI to tell the user that access to SMS messages must be allowed
                }
            }
            // Handle other permission requests if needed
        }
    }

    private fun requestSmsPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}