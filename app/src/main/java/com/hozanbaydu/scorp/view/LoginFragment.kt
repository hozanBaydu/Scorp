package com.hozanbaydu.scorp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.hozanbaydu.scorp.R
import com.hozanbaydu.scorp.databinding.FragmentLoginBinding

class LoginFragment:Fragment(R.layout.fragment_login) {

    private var fragmentBinding : FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        fragmentBinding = binding

        auth = Firebase.auth

        val currentUsers=auth.currentUser  //initialize ettik.

        if (currentUsers!=null){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }

        binding.signIn.setOnClickListener {
            val email=binding.mailEditText.text.toString()
            val password=binding.passwordEditText.text.toString()

            if (email.equals("") || password.equals("")){

                Toast.makeText(requireContext(),"Enter your email and password!!", Toast.LENGTH_LONG).show()

            }else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }



        binding.signupButton.setOnClickListener {


            binding.signupButton.visibility=View.INVISIBLE
            binding.signIn.visibility=View.INVISIBLE
            binding.signupButton2.visibility=View.VISIBLE


            val email=binding.mailEditText.text.toString()
            val password=binding.passwordEditText.text.toString()

            if (email.equals("") || password.equals("")){

                Toast.makeText(requireContext(),"Enter your email and password!!",Toast.LENGTH_LONG).show()

            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                    binding.mailEditText.visibility=View.INVISIBLE
                    binding.passwordEditText.visibility=View.INVISIBLE
                    binding.nameEditText.visibility=View.VISIBLE


                    Toast.makeText(requireContext(),"Lütfen bir isim belirleyin",Toast.LENGTH_SHORT).show()

                    binding.signupButton2.setOnClickListener {



                        var name=binding.nameEditText.text.toString()

                        val user = Firebase.auth.currentUser

                        val profileUpdates = userProfileChangeRequest {
                            displayName = "@"+name
                        }

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {


                                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                                }
                            }





                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }



        }


    }
}