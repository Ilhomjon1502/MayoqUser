package uz.ilhomjon.soscaruser.view.register_map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentDialogBinding
import uz.ilhomjon.soscaruser.utils.MyData

class DialogFragment : androidx.fragment.app.DialogFragment(), OnMapReadyCallback {

    private val binding by lazy { FragmentDialogBinding.inflate(layoutInflater) }
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: LatLng
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val TAG = "DialogFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.save.setOnClickListener {
            MyData.user!!.lat=currentLocation.latitude.toString()
            MyData.user!!.long=currentLocation.longitude.toString()
            dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer2, mapFragment).commit()

        mapFragment.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                Log.d("TestLocation", "onMapReady: $currentLocation")
                // Set initial camera position (optional)
                val initialPosition =
                    LatLng(currentLocation.latitude, currentLocation.longitude) // San Francisco
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 15f))

                Log.d(TAG, "onMapReady: ${currentLocation.latitude}-${currentLocation.longitude}")
                // Add marker to the map
                val markerPosition = LatLng(currentLocation.latitude, currentLocation.longitude)
                googleMap.addMarker(
                    MarkerOptions().position(markerPosition).title("Sizning manzilingiz")
                )
            }
        }

    }
}
