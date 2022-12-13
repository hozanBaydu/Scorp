package com.hozanbaydu.scorp.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hozanbaydu.scorp.R
import com.hozanbaydu.scorp.adapter.MainAdapter
import com.hozanbaydu.scorp.databinding.FragmentMainBinding
import com.hozanbaydu.scorp.viewmodel.ScorpViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment @Inject constructor(

      var mainAdapter:MainAdapter
):Fragment(R.layout.fragment_main) {
    private lateinit var auth: FirebaseAuth
    private var fragmentBinding : FragmentMainBinding? = null
    lateinit var viewModel: ScorpViewmodel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ScorpViewmodel::class.java)

        val binding = FragmentMainBinding.bind(view)
        fragmentBinding = binding


        mainAdapter.notifyDataSetChanged()


        auth = Firebase.auth
        val currentUsers=auth.currentUser

        var name=currentUsers?.displayName

        if (name != null) {
            viewModel.currentUserName=name
        }

        binding.sendButton.setOnClickListener {

            findNavController().navigate(MainFragmentDirections.actionMainFragmentToUploadFragment())
        }

        binding.backButton.setOnClickListener {
            auth.signOut()

            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
        }

        lifecycleScope.launch{
            viewModel.getData(requireContext())
            delay(100)
            mainAdapter.postList=viewModel.postArrayList

            delay(100)

            binding.mainRecyclerview.adapter=mainAdapter
            binding.mainRecyclerview.layoutManager= LinearLayoutManager(requireContext())


        }












    }
}