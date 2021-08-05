package com.example.foursquaremvvm.scenes.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foursquaremvvm.R
import com.example.foursquaremvvm.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var binding: FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.updateUI(args.venueModel)
    }

}