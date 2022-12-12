package com.hozanbaydu.scorp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hozanbaydu.scorp.R
import com.hozanbaydu.scorp.databinding.FragmentUploadBinding
import com.hozanbaydu.scorp.viewmodel.ScorpViewmodel

class UploadFragment:Fragment(R.layout.fragment_upload) {

    private var fragmentBinding : FragmentUploadBinding? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri?=null
    lateinit var viewModel: ScorpViewmodel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ScorpViewmodel::class.java)

        val binding = FragmentUploadBinding.bind(view)
        fragmentBinding = binding




        registerLauncher(binding.uploadImageview)


        binding.uploadImageview.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Galeriye gitmek için izin vermeniz gerekiyor", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()


                }else{
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }
            }else{
                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }




        binding.uploadShareButton.setOnClickListener {
            viewModel.updateFireebase(requireContext(),selectedPicture!!,binding.uploadPerson,viewModel.currentUserName,binding.uploadComment)
            findNavController().navigate(UploadFragmentDirections.actionUploadFragmentToMainFragment())
        }




    }
    fun registerLauncher (view: ImageView){
            activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
                if (result.resultCode== AppCompatActivity.RESULT_OK){
                    val intentFromResult=result.data

                    if (intentFromResult!=null){
                        selectedPicture= intentFromResult.data
                        selectedPicture.let {

                            view.setImageURI(it)
                        }
                    }
                }
            }
            permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
                if (result){
                    val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }else{
                    Toast.makeText(requireContext(),"İzin gerekiyor", Toast.LENGTH_LONG).show()
                }
            }
    }
}