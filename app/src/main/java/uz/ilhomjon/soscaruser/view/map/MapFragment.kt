package uz.ilhomjon.soscaruser.view.map

import MySharedPreference
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentMapBinding
import uz.ilhomjon.soscaruser.databinding.HeaderItemBinding
import uz.ilhomjon.soscaruser.models.Call
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository
import uz.ilhomjon.soscaruser.viewmodel.mapviewmodel.MapViewModel
import uz.ilhomjon.soscaruser.viewmodel.mapviewmodel.MapViewModelFactory
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory
import java.lang.Exception
import java.time.LocalDateTime
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class MapFragment : Fragment(), OnMapReadyCallback, CoroutineScope {

    private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    private val TAG = "MapFragment"
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private var GPS_PERMISSION_REQUEST_CODE: Int = 2
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currentLocation: LatLng
    private val SEND_SMS_PERMISSION_REQUEST_CODE = 1
    private lateinit var mapRepository: MapRepository
    private lateinit var mapViewModelFactory: MapViewModelFactory
    private lateinit var mapViewModel: MapViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mapRepository = MapRepository()
        mapViewModelFactory = MapViewModelFactory(mapRepository)
        mapViewModel =
            ViewModelProvider(this, mapViewModelFactory)[MapViewModel::class.java]



        if (!isGpsEnabled(binding.root.context)) {
            showEnableGPSDialog()
        }
        requestLocationPermission()

        val type = arguments?.getString("type")

        when (type) {
            "ambulance" -> {
                setColor("#992145", "Tez yordam")
                binding.carImage.setImageResource(R.drawable.ambulance_car)
            }

            "firing" -> {
                setColor("#992145", "O't o'chirish")
                binding.carImage.setImageResource(R.drawable.firingcar)
            }

            "policy" -> {
                setColor("#215899", "Politsiya")
                binding.carImage.setImageResource(R.drawable.policy_car)
            }

            "service" -> {
                setColor("#215899", "Xizmat mashinasi")
            }
        }

        binding.menu.setOnClickListener {
            binding.openDrawer.open()
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> {
                    Toast.makeText(context, "About", Toast.LENGTH_SHORT).show()
                }

                R.id.edit -> {
                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        MySharedPreference.init(binding.root.context)
        val headerItemBinding = HeaderItemBinding.inflate(layoutInflater)
        headerItemBinding.menuName.text = MySharedPreference.getUser().login
        Picasso.get().load(MySharedPreference.getUser().imageLink).into(headerItemBinding.menuImg)

        binding.navView.addHeaderView(headerItemBinding.root)

        binding.currentLocation.setOnClickListener {
            // Check if the SEND_SMS permission is granted
            if (ContextCompat.checkSelfPermission(
                    binding.root.context, Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.SEND_SMS),
                    SEND_SMS_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission is already granted, you can send SMS here
                // Call your SMS sending method or implement your SMS logic
                sendSMS(MySharedPreference.getUser())

                val call = Call(
                    id = UUID.randomUUID().toString(),
                    user_id = MySharedPreference.getUser().phoneNumber.toString(),
                    worker_id = null,
                    start_time = LocalDateTime.now().toString(),
                    end_time = null,
                    user_location = currentLocation.toString(),
                    worker_location = null
                )
                mapViewModel.addCall(call)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment).commit()

        mapFragment.getMapAsync(this)

    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                binding.root.context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Lokatsiya ruxsatini so'ragan dialogni ko'rsatish
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(context).setTitle("Lokatsiya ruxsatini so'raymiz")
                    .setMessage("Bu ilovadan foydalanish uchun lokatsiya ruxsatini so'ramiz.")
                    .setPositiveButton("OK") { _, _ ->
                        // Ruxsat so'roqni boshqarish
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_CODE
                        )
                    }.create().show()
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
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GPS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Lokatsiya va GPS ruxsati berilgan
                } else {
                    Toast.makeText(
                        context, "Lokatsiya va GPS ruxsat berilmadi", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    // GPSni yoqishni so'raydigan dialogni ochish
    private fun showEnableGPSDialog() {
        val dialogBuilder = AlertDialog.Builder(binding.root.context)
        dialogBuilder.setMessage("GPSni yoqing").setCancelable(false)
            .setPositiveButton("Sozlamalar") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.setNegativeButton("Bekor qilish") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
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

                // Add marker to the map
                val markerPosition = LatLng(currentLocation.latitude, currentLocation.longitude)
                googleMap.addMarker(
                    MarkerOptions().position(markerPosition).title("Sizning manzilingiz")
                )
            }
        }
    }

    private fun setColor(color: String, title: String) {
        binding.topbarTitle.text = title
        binding.topbarTitle.setTextColor(Color.parseColor(color))
        binding.addCard.setBackgroundColor(Color.parseColor(color))
        binding.homeTv.setTextColor(Color.parseColor(color))
        binding.locTv.setTextColor(Color.parseColor(color))
        binding.navView.setBackgroundColor(Color.parseColor(color))
    }

    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun sendSMS(user: User) {
        try {
            val smsManager = SmsManager.getDefault()

            // on below line we are sending text message.  998907832331
            smsManager.sendTextMessage(
                "+998910561491",
                null,
                "Ism:${user.login}, Telefon raqami:${user.phoneNumber}, Yashash manzili:${user.address}, Kasallik:${user.history}",
                null,
                null
            )

            // on below line we are displaying a toast message for message send,
            Toast.makeText(binding.root.context, "Message Sent", Toast.LENGTH_LONG).show()


        } catch (e: Exception) {
            Log.d(TAG, "sendSMS: ${e.message}")
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}