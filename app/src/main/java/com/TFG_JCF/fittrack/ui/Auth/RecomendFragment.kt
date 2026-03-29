package com.TFG_JCF.fittrack.ui.Auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentOthersBinding
import com.TFG_JCF.fittrack.databinding.FragmentRecomendBinding


class RecomendFragment : Fragment() {

    private var _binding: FragmentRecomendBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRecomendBinding.bind(view)

        initUI()
    }

    private fun initUI() {
        TODO("Not yet implemented")
    }
}