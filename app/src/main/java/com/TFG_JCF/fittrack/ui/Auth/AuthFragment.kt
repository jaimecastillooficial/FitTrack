package com.TFG_JCF.fittrack.ui.Auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentAuthBinding
import com.TFG_JCF.fittrack.ui.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding : FragmentAuthBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun initUI() {
        binding.btnCreateAccount.setOnClickListener {
            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        val uid = user?.uid

                       //TODO navigateToGoal()
                    }
                    else{
                        showAlert()
                    }
                }
            }
        }
//        binding.BtnAcceder.setOnClickListener {
//            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty()) {
//
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString()).addOnCompleteListener{
//                    if (it.isSuccessful){
//
//                        val user = FirebaseAuth.getInstance().currentUser
//                        val uid = user?.uid
//
//
//                    }
//                    else{
//                        showAlert()
//                    }
//                }
//            }
//        }

    }

private fun navigateToGoal() {
}

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}