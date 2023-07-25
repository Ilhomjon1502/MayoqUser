package uz.ilhomjon.soscaruser.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding.ambulanceBtn.setOnClickListener {
            mapFragment("ambulance")
        }
        binding.firing.setOnClickListener {
            mapFragment("firing")
        }
        binding.policy.setOnClickListener {
            mapFragment("policy")
        }
        binding.service.setOnClickListener {
            mapFragment("service")
        }
        return binding.root
    }

    fun mapFragment(string: String) {
        findNavController().navigate(R.id.mapFragment, bundleOf("type" to string))
    }
}