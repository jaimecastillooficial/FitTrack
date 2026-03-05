package com.TFG_JCF.fittrack.ui.Auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentAuthBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding : FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        initUI()

    }

    private fun initUI() {
        binding.btnCreateAccount.setOnClickListener {
            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty()) {
                binding.progressBar.isVisible = true
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        val uid = user?.uid

                        vm.signUpData.uid = uid
                        vm.signUpData.email = binding.txtEmail.toString()
                        vm.signUpData.password = binding.txtPassword.toString()
                        vm.signUpData.name = binding.txtName.text.toString()

                        navigateToGoal()
                    }
                    else{
                        binding.progressBar.isVisible = false
                        showAlert()
                    }
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

private fun navigateToGoal() {
    findNavController().navigate(R.id.action_auth_to_goal)
}
    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_auth_to_login)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error registrando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}