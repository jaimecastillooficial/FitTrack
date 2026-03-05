package com.TFG_JCF.fittrack.ui.Auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentLoginBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        initUI()
    }

    private fun initUI() {
        binding.btnlogin.setOnClickListener {
            binding.progressBar.isVisible = true
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (email.isNotEmpty() || password.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            val uid = FirebaseAuth.getInstance().currentUser!!.uid

                            navigateToHome()

                        } else {
                            binding.progressBar.isVisible = false
                            showAlert()
                        }

                    }
            }


        }
        binding.tvSignUp.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error iniciando sesion")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun navigateToHome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    private fun navigateToSignUp() {
        findNavController().navigate(R.id.action_login_to_auth)
    }
}