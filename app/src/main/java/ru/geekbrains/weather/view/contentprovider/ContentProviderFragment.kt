package ru.geekbrains.weather.view.contentprovider

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.geekbrains.weather.databinding.FragmentContentProviderBinding

class ContentProviderFragment : Fragment() {

    private val requestCode = 999

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance() = ContentProviderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> getContacts()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    requestUserPermission(it)
                }
                else -> {
                    ContactRequestPermission()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            this.requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                } else {
                    context?.let {
                        requestUserPermission(it)
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestUserPermission(it: Context) {
        AlertDialog.Builder(it)
            .setTitle("A permission is needed")
            .setMessage("Please allow the app to view your contacts for correct functioning")

            .setPositiveButton("Allow") { dialog, which ->
                ContactRequestPermission()
            }
            .setNegativeButton("Don't allow") { dialog, which ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun ContactRequestPermission() {
        val launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { result -> result?.let { getContacts() } }
        launcher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val pointer: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " DESC"
            )
            pointer?.let { pointer ->
                for (i in 0 until pointer.count) {
                    val isMovable = pointer.moveToPosition(i)
                    val range = pointer.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    if (isMovable && range > -1) {
                        val name = pointer.getString(range)
                        binding.containerForContacts.addView(TextView(it).apply {
                            text = name
                            textSize = 35f
                        })
                    }
                }
            }
        }
    }
}