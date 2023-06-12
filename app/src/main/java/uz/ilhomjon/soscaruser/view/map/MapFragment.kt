package uz.ilhomjon.soscaruser.view.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    private val TAG = "MapFragment"
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private var GPS_PERMISSION_REQUEST_CODE: Int = 2
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        showEnableGPSDialog()
        requestLocationPermission()

        mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()


        mapFragment.getMapAsync(this)


        return binding.root
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Lokatsiya ruxsatini so'ragan dialogni ko'rsatish
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(context)
                    .setTitle("Lokatsiya ruxsatini so'raymiz")
                    .setMessage("Bu ilovadan foydalanish uchun lokatsiya ruxsatini so'ramiz.")
                    .setPositiveButton("OK") { _, _ ->
                        // Ruxsat so'roqni boshqarish
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_CODE
                        )
                    }
                    .create()
                    .show()
            } else {
                // Ruxsat so'roqni boshqarish
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // Lokatsiya ruxsati berilgan
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GPS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Lokatsiya va GPS ruxsati berilgan
                } else {
                    Toast.makeText(
                        context,
                        "Lokatsiya va GPS ruxsat berilmadi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    // GPSni yoqishni so'raydigan dialogni ochish
    private fun showEnableGPSDialog() {
        val dialogBuilder = AlertDialog.Builder(binding.root.context)
        dialogBuilder.setMessage("GPSni yoqing")
            .setCancelable(false)
            .setPositiveButton("Sozlamalar") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Bekor qilish") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


    private fun addMarkersToMap(googleMap: GoogleMap, currentLocation: LatLng) {

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    Log.d("TestLocation", "onMapReady: $currentLocation")
                    // Set initial camera position (optional)
                    val initialPosition = LatLng(currentLocation.latitude, currentLocation.longitude) // San Francisco
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 15f))

                    // Add marker to the map
                    val markerPosition = LatLng(currentLocation.latitude, currentLocation.longitude)
                    googleMap.addMarker(MarkerOptions().position(markerPosition).title("Sizning manzilingiz"))
                }
            }



    }
}