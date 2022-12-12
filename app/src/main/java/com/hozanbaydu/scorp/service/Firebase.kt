package com.hozanbaydu.scorp.service

import android.content.Context
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.hozanbaydu.scorp.model.ScorpModel
import com.hozanbaydu.scorp.viewmodel.ScorpViewmodel
import java.net.URI
import java.util.*
import javax.inject.Inject

class Firebase @Inject constructor(

    val glide: RequestManager

) {

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    var postArrayList = ArrayList<ScorpModel>()

    fun upload(
        context: Context,
        selectedPicture: Uri,
        tagText: EditText,
        currentName: String,
        commentText: EditText
    ) {

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val refererence = storage.reference
        val imageReference = refererence.child("images").child(imageName)
        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val dowlandUrl = it.toString()
                    if (auth.currentUser != null) {
                        val postMap = hashMapOf<String, Any>()
                        postMap.put("dowlandUrl", dowlandUrl)
                        postMap.put("userEmail", auth.currentUser!!.email!!)
                        postMap.put("tagName", tagText.text.toString())
                        postMap.put("currentName", currentName)
                        postMap.put("comment", commentText.text.toString())






                        postMap.put("date", Timestamp.now())
                        firestore.collection("posts").add(postMap).addOnSuccessListener {


                        }.addOnFailureListener {
                            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()


            }
        }
    }

    fun getdata(context: Context) {

        firestore = Firebase.firestore
        firestore.collection("posts").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {

                        val documents = value.documents

                        postArrayList.clear()
                        for (document in documents) {

                            val comment = document.get("comment") as? String
                            val userEmail = document.get("userEmail") as? String
                            val dowloadUrl = document.get("dowlandUrl") as? String
                            val currentName = document.get("currentName") as? String
                            val tagName = document.get("tagName") as? String
                            val tagUrl = document.get("tagUrl") as? String
                            val documentId = document.id


                            val post = ScorpModel(
                                userEmail,
                                comment,
                                currentName,
                                tagName,
                                dowloadUrl,
                                documentId,
                                tagUrl
                            )
                            postArrayList.add(post)
                        }
                    }
                }
            }
        }
    }

    fun updateData(id: String,selectedPicture: Uri) {

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val refererence = storage.reference
        val imageReference = refererence.child("images").child(imageName)
        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val dowlandUrl = it.toString()

                    firestore.collection("posts/").document(id).update("tagUrl", dowlandUrl)

                }




            }

        }
    }

    fun uploadVote(id: String,vote:String){
        firestore.collection("posts/").document(id).update("vote", vote)
    }
}