package uz.ilhomjon.soscaruser.view.register_map

import android.Manifest
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentRegisterMapBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory

class RegisterMapFragment : Fragment() {

    private val binding by lazy { FragmentRegisterMapBinding.inflate(layoutInflater) }
    private lateinit var smsCodeRepository: SmsCodeRepository
    private lateinit var signUpViewModelFactory: SignUpViewModelFactory
    private lateinit var signUpViewModel: SignUpViewModel
    private var LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private var GPS_PERMISSION_REQUEST_CODE: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        smsCodeRepository = SmsCodeRepository()
        signUpViewModelFactory = SignUpViewModelFactory(smsCodeRepository)
        signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

        MySharedPreference.init(binding.root.context)
        var user: User? = null
        user = MySharedPreference.getUser()

        if (user.login != null && user.parol != null) {
            findNavController().popBackStack()
        }


        binding.nextBtn.setOnClickListener {
            Log.d("saveUser", "onCreateView: ${MyData.user}")
            //Firebase
            signUpViewModel.addUser(MyData.user!!)
            //Shared
            findNavController().navigate(R.id.registerInfoFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (!isGpsEnabled(binding.root.context)) {
            showEnableGPSDialog()
        }
        requestLocationPermission()

        binding.openMap.setOnClickListener {
            val dialogFragment = DialogFragment()
            dialogFragment.show(parentFragmentManager.beginTransaction(), "tag")
        }
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

    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}