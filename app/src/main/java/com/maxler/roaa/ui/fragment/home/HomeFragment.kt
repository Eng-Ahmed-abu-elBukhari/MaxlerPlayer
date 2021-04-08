package com.maxler.roaa.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.maxler.roaa.data.repository.MainRepository
import com.maxler.roaa.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private val viewModel by lazy {
        ViewModelProvider(this,HomeViewModelFactory(MainRepository(requireContext()))).get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenCreated {
            viewModel.songs.collect {
                Log.i("HomeFragment","${it.first().duration}")
            }
        }



    }






}