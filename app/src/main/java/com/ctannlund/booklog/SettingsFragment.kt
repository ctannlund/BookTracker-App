package com.ctannlund.booklog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctannlund.booklog.databinding.FragmentAppsettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentAppsettingsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppsettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        if (sharedPref != null) {
            binding.enableNotifs.isChecked = sharedPref.getBoolean("notifs", true)
        }

        // notif enable button
        binding.enableNotifs.setOnClickListener {
            sharedPref?.edit()?.putBoolean("notifs", binding.enableNotifs.isChecked)?.apply()
        }

        // notif test button
        binding.notificationTestGoalButton.setOnClickListener {
            displayNotifications()
        }

        // goal reset test button
        var clearClicked = false
        binding.clearGoalButton.setOnClickListener {
            clearClicked = if(!clearClicked) {
                Toast.makeText(requireContext(), "Tap reset again to confirm",
                    Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(requireContext(), "Reading goal reset",
                    Toast.LENGTH_SHORT).show()
                viewLifecycleOwner.lifecycleScope.launch {
                    settingsViewModel.resetGoals()
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsToGoalset())
                }
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNotifications() {
        val id = "booktracker_id"

        val sharedPref = activity?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val notifEnabled = sharedPref?.getBoolean("notifs", true)

        if (notifEnabled == true) {
            val notif = NotificationCompat.Builder(requireContext(), id)
            notif.setSmallIcon(R.drawable.book_home)
            notif.setContentTitle("BookTracker Test Notification")
            notif.setContentText("BookTracker notifications are enabled.")
            notif.priority = NotificationCompat.PRIORITY_HIGH

            val display = NotificationManagerCompat.from(requireContext())
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Consider calling
                //    ActivityCompat#requestPermissions
                return
            }
            display.notify(1, notif.build())
        }
    }

}