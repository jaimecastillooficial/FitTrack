package com.TFG_JCF.fittrack.ui.MainApp.Others

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentOthersBinding
import com.TFG_JCF.fittrack.ui.Auth.SignUpActivity

import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java

class OthersFragment : Fragment(R.layout.fragment_others) {

    private var _binding: FragmentOthersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOthersBinding.bind(view)

        binding.CerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireContext(), SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}