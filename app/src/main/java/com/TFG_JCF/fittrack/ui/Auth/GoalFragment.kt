package com.TFG_JCF.fittrack.ui.Auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.GoalType
import com.TFG_JCF.fittrack.databinding.FragmentGoalBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class GoalFragment : Fragment() {

    private var _binding : FragmentGoalBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding.btnNext.setOnClickListener {

            binding.tvGoalError.visibility = View.GONE

            when (binding.goalToggleGroup.checkedButtonId) {

                R.id.btnLose ->
                    vm.signUpData.goalType = GoalType.LOSE_WEIGHT

                R.id.btnMaintain ->
                    vm.signUpData.goalType = GoalType.MAINTAIN_WEIGHT

                R.id.btnGain ->
                    vm.signUpData.goalType = GoalType.GAIN_WEIGHT

                else ->{
                    binding.tvGoalError.visibility = View.VISIBLE
                    return@setOnClickListener
            }
            }
            navigateToActivityLevel()
        }
        binding.goBack.setOnClickListener {
            navigateToSignIn()
        }
    }

    private fun navigateToSignIn(){
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_goal_to_login)

    }
    private fun navigateToActivityLevel(){
        findNavController().navigate(R.id.action_goal_to_activityLevel)

    }


}