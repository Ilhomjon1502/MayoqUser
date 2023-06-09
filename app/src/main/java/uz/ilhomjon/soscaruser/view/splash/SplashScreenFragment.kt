package uz.ilhomjon.soscaruser.view.splash

import MySharedPreference
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {


    private val binding by lazy { FragmentSplashScreenBinding.inflate(layoutInflater) }
    private val TAG = "SplashScreenFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        MySharedPreference.init(binding.root.context)
        val login = MySharedPreference.getLogin()
        val password = MySharedPreference.getPassword()
        Log.d(TAG, "onCreateView: $password-$login")

        if (login != "" && password != "") {
            findNavController().navigate(R.id.homeFragment)
        } else {
            findNavController().navigate(R.id.signInFragment)
        }

        return binding.root
    }
}