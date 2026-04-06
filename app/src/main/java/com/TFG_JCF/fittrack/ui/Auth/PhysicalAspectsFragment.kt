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
            if (validateFields()){
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
            else{
                return@setOnClickListener
            }

        }
        binding.goBack.setOnClickListener {
            navigateToActivityLevel()
        }
    }

    private fun navigateToRecomend() {
        findNavController().navigate(R.id.action_physicalaspects_to_recomend)
    }

    private fun navigateToActivityLevel(){
        findNavController().navigate(R.id.action_physicalaspects_to_activityLevel)

    }

    private fun validateFields(): Boolean {

        val ageText = binding.txtAge.text.toString().trim()
        val heightText = binding.txtHeight.text.toString().trim()
        val weightText = binding.txtWeight.text.toString().trim()

        binding.tvGenderError.visibility = View.GONE
        binding.ageLayout.error = null
        binding.heightLayout.error = null
        binding.weightLayout.error = null

        if (binding.genderToggle.checkedButtonId == View.NO_ID) {
            binding.tvGenderError.visibility = View.VISIBLE
            return false
        }

        if (ageText.isEmpty()) {
            binding.ageLayout.error = "Introduce tu edad"
            return false
        }

        if (heightText.isEmpty()) {
            binding.heightLayout.error = "Introduce tu altura"
            return false
        }

        if (weightText.isEmpty()) {
            binding.weightLayout.error = "Introduce tu peso"
            return false
        }

        return true
    }
}