package uz.ilhomjon.soscaruser.view.register_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentRegisterInfoBinding

class RegisterInfoFragment : Fragment() {


    private val binding by lazy { FragmentRegisterInfoBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        findNavController().popBackStack()

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        return binding.root
    }
}