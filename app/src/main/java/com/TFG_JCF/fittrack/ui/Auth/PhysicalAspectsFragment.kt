package com.TFG_JCF.fittrack.ui.Auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.Gender
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.databinding.FragmentPhysicalAspectsBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class PhysicalAspectsFragment : Fragment() {

    private var _binding: FragmentPhysicalAspectsBinding? = null
    private val binding get()= _binding!!

    private val vm: SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhysicalAspectsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.btnNext.setOnClickListener {

            // gender
            when (binding.genderToggle.checkedButtonId) {

                R.id.btnMale ->
                    vm.signUpData.gender = Gender.MALE

                R.id.btnFemale ->
                    vm.signUpData.gender = Gender.FEMALE
            }

            // altura y edad
            vm.signUpData.height = binding.txtHeight.text.toString().toInt()
            vm.signUpData.age = binding.txtAge.text.toString().toInt()
            vm.signUpData.weight = binding.txtWeight.text.toString().toFloat()


            navigateToRecomend()
        }
    }

    private fun navigateToRecomend() {
        findNavController().navigate(R.id.action_physicalaspects_to_recomend)
    }
}