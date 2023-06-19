package uz.ilhomjon.soscaruser.view.register_info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentRegisterInfoBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData

class RegisterInfoFragment : Fragment() {


    private val binding by lazy { FragmentRegisterInfoBinding.inflate(layoutInflater) }
    private val TAG = "RegisterInfoFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


//        findNavController().popBackStack()
        MySharedPreference.init(binding.root.context)
        var user: User? = null
        user = MySharedPreference.getUser()
        Log.d(TAG, "onCreateView: $user")

        if (user.login != null && user.parol != null) {
            findNavController().popBackStack()
        }
        binding.nextBtn.setOnClickListener {
            MySharedPreference.setUser(MyData.user!!)
            findNavController().navigate(R.id.homeFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }
}