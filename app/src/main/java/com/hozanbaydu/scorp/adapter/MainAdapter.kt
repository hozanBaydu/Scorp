package com.hozanbaydu.scorp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hozanbaydu.scorp.R
import com.hozanbaydu.scorp.databinding.MainRecyclerrowBinding
import com.hozanbaydu.scorp.model.ScorpModel
import com.hozanbaydu.scorp.view.MainFragmentDirections
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MainAdapter @Inject constructor(

    val glide : RequestManager,
    private val firebase: com.hozanbaydu.scorp.service.Firebase
): RecyclerView.Adapter<MainAdapter.PostHolder>() {
    class PostHolder(val binding:MainRecyclerrowBinding): RecyclerView.ViewHolder(binding.root){

    }

    private lateinit var auth: FirebaseAuth


    var postList=ArrayList<ScorpModel>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=MainRecyclerrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        auth = Firebase.auth
        val currentUsers=auth.currentUser


        holder.binding.textView.text=postList.get(position).comment
        holder.binding.currentName.text=postList.get(position).currentName
        holder.binding.tagName.text=postList.get(position).tagName
        var url=postList.get(position).dowlandUrl
        var tagUrl=postList.get(position).tagUrl
        holder.binding.currentVote.text=postList.get(position).vote
        holder.binding.tagVote.text=postList.get(position).tagVote


        glide.load(url).into(holder.binding.userImageview)
        glide.load(tagUrl).into(holder.binding.tagImageview)
        println(currentUsers?.displayName.toString())



            holder.binding.tagImageview.setOnClickListener {



                if (currentUsers?.displayName==holder.binding.tagName.text.toString()) {
                    holder.binding.tagImageview.findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToUploadTagFragment(
                            postList.get(position).documentId
                        )
                    )
                }else{

                    var tageVoteNumber=holder.binding.tagVote.text.toString().toInt()+1



                    holder.binding.tagVote.text=tageVoteNumber.toString()

                    firebase.uploadVote(postList.get(position).documentId,tageVoteNumber.toString())



                }
            }

        holder.binding.userImageview.setOnClickListener {
            var currentVoteNumber=holder.binding.currentVote.text.toString().toInt()+1

            holder.binding.currentVote.text=currentVoteNumber.toString()

            firebase.uploadVote(postList.get(position).documentId,currentVoteNumber.toString())
        }







    }

    override fun getItemCount(): Int {
        return postList.size
    }
}