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
import com.TFG_JCF.fittrack.data.utils.PasswordValidator
import com.TFG_JCF.fittrack.databinding.FragmentAuthBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root

    }
//TODO meter requisitos de complejidad y poder volver atras en el onboarding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        initUI()

    }

    private fun initUI() {
        binding.btnCreateAccount.setOnClickListener {
            createAccount()
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun createAccount() {
        val name = binding.txtName.text.toString().trim()
        val email = binding.txtEmail.text.toString().trim()
        val password = binding.txtPassword.text.toString()
        val confirmPassword = binding.txtConfirmPassword.text.toString()

        clearErrors()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showEmptyFieldsErrors(name, email, password, confirmPassword)
            return
        }

        if (!isValidEmail(email)) {
            binding.emailLayout.error = "Introduce un correo válido"
            binding.txtEmail.requestFocus()
            return
        }

        val passwordError = PasswordValidator.validate(password)
        if (passwordError != null) {
            binding.passwordLayout.error = passwordError
            return
        }

        if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = "Las contraseñas no coinciden"
            return
        }

        binding.progressBar.isVisible = true

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                binding.progressBar.isVisible = false

                if (it.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid

                    vm.signUpData.uid = uid
                    vm.signUpData.email = email
                    vm.signUpData.password = password
                    vm.signUpData.name = name

                    navigateToGoal()
                } else {
                    showAlert()
                }
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

    private fun showPasswordAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Las contraseñas no coinciden")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun clearErrors() {
        binding.nameLayout.error = null
        binding.emailLayout.error = null
        binding.passwordLayout.error = null
        binding.confirmPasswordLayout.error = null
    }

    private fun showEmptyFieldsErrors(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (name.isEmpty()) {
            binding.nameLayout.error = "Introduce tu nombre"
        }

        if (email.isEmpty()) {
            binding.emailLayout.error = "Introduce tu email"
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = "Introduce una contraseña"
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.error = "Confirma tu contraseña"
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}