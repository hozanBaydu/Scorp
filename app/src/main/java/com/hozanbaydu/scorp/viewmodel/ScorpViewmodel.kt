package com.hozanbaydu.scorp.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.hozanbaydu.scorp.adapter.MainAdapter
import com.hozanbaydu.scorp.model.ScorpModel
import com.hozanbaydu.scorp.service.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ScorpViewmodel @Inject constructor(
    private val firebase:Firebase,
    private val adapter: MainAdapter

):ViewModel() {

    fun updateFireebase(context: Context, selectedPicture: Uri, tagText: EditText, currentName:String,commentText: EditText){

        firebase.upload(context,selectedPicture,tagText,currentName,commentText)
    }
    var currentUserName=String()
    var postArrayList=firebase.postArrayList
    fun update(id:String,selectedPicture: Uri){
        firebase.updateData(id,selectedPicture)
    }
    fun uploadVote(id: String,vote:String){
        firebase.uploadVote(id,vote)
    }
    fun getData(context: Context){
        firebase.getdata(context)
    }
}