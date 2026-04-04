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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentLoginBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.jvm.java


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()
    private val GOOGLE_SIGN_IN = 100

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
        checkUser()

        initUI()
    }

    private fun checkUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.progressBar.isVisible = true
            resolveNavigationAfterAuth()
        }
    }

    private fun initUI() {
        binding.btnlogin.setOnClickListener {
            binding.progressBar.isVisible = true
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            val uid = FirebaseAuth.getInstance().currentUser!!.uid

                            resolveNavigationAfterAuth()

                        } else {
                            binding.progressBar.isVisible = false
                            showAlert()
                        }

                    }
            }else{
                binding.progressBar.isVisible = false
                showEmptyFieldsErrors(email, password)
            }
        }
        binding.tvSignUp.setOnClickListener {
            navigateToSignUp()
        }
        binding.btnloginGoogle.setOnClickListener {

            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this.requireContext(), googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)


        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("No se ha encontrado un usuario con estos datos")
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

    private fun showEmptyFieldsErrors(
        email: String,
        password: String,
    ) {

        if (email.isEmpty()) {
            binding.emailLayout.error = "Introduce tu email"
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = "Introduce una contraseña"
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.progressBar.isVisible = true

        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

            val account = task.getResult(ApiException::class.java)

            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                    if (it.isSuccessful) {

                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            vm.signUpData.uid = user.uid
                            vm.signUpData.email = user.email
                            vm.signUpData.name = user.displayName ?: ""
                        }
                        resolveNavigationAfterAuth()

                    } else {
                        binding.progressBar.isVisible = false
                        showAlert()
                    }

                }
            }
            }catch (e: ApiException){
                binding.progressBar.isVisible = false
                showAlert()
            }
        }
    }
    private fun resolveNavigationAfterAuth() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            binding.progressBar.isVisible = false
            showAlert()
            return
        }

        val uid = user.uid

        vm.checkIfUserProfileExists(uid) { exists ->
            binding.progressBar.isVisible = false
            requireActivity().runOnUiThread {
                binding.progressBar.isVisible = false

                if (exists) {
                    navigateToHome()
                } else {
                    vm.signUpData.uid = user.uid
                    vm.signUpData.email = user.email
                    vm.signUpData.name = user.displayName ?: ""

                    navigateToGoal()
                }
            }
        }
    }
    private fun navigateToGoal() {
        findNavController().navigate(R.id.action_login_to_goal)
    }
}