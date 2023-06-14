package uz.ilhomjon.soscaruser.view.splash

import MySharedPreference
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSplashScreenBinding
import uz.ilhomjon.soscaruser.models.User

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {


    private val binding by lazy { FragmentSplashScreenBinding.inflate(layoutInflater) }
    private val TAG = "SplashScreenFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        findNavController().popBackStack()

        val handler = Handler(Looper.getMainLooper())
        var runnable = Runnable {
            MySharedPreference.init(binding.root.context)
            var user:User?=null
            user=MySharedPreference.getUser()
            Log.d(TAG, "onCreateView: $user")
            if (user.login!=null && user.parol!=null){
                findNavController().navigate(R.id.homeFragment)
            }else{
                findNavController().navigate(R.id.signInFragment)
            }
//            Log.d(TAG, "onCreateView: ${user.login}-${user.parol}")
//            if (user.login != "" && user.parol != "") {
//            } else {
//            }
        }
        handler.postDelayed(runnable, 3000)


        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        binding.emergenix.startAnimation(animation)
        binding.info.startAnimation(animation)
        binding.splashImage.startAnimation(animation)

        return binding.root
    }
}