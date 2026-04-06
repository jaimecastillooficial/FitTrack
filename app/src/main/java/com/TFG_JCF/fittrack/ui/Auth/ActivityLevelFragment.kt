package com.TFG_JCF.fittrack.ui.Auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.ActivityLevel
import com.TFG_JCF.fittrack.databinding.FragmentActivityLevelBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ActivityLevelFragment : Fragment() {

    private var _binding: FragmentActivityLevelBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding.btnNext.setOnClickListener {

            when (binding.activityToggleGroup.checkedButtonId) {

                R.id.btnSedentary ->
                    vm.signUpData.activityLevel = ActivityLevel.SEDENTARY

                R.id.btnLight ->
                    vm.signUpData.activityLevel = ActivityLevel.LIGHT

                R.id.btnModerate ->
                    vm.signUpData.activityLevel = ActivityLevel.MODERATE

                R.id.btnHigh ->
                    vm.signUpData.activityLevel = ActivityLevel.HIGH

                R.id.btnVeyHigh ->
                    vm.signUpData.activityLevel = ActivityLevel.VERY_HIGH
                else -> {
                    binding.tvActivityError.visibility = View.VISIBLE
                    return@setOnClickListener
                }
            }

            navigateToPhysicalAspects()
        }
        binding.goBack.setOnClickListener {
            navigateToGoal()
        }
    }
    private fun navigateToGoal() {
        findNavController().navigate(R.id.action_activityLevel_to_goal)

    }
    private fun navigateToPhysicalAspects() {
        findNavController().navigate(R.id.action_activityLevel_to_physical)

    }

}