package com.example.chatapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chatapp.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(){
    private val firebaseDatabase= Firebase.database
    private val _channels= MutableStateFlow<List<Channel>>(emptyList())
    val channels =_channels.asStateFlow()


    init{
        getChannels()

    }
    private fun getChannels() {
        val reference = firebaseDatabase.getReference("Channel") // Capital 'C' since your Firebase node is "Channel"
        reference.get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<Channel>()
                snapshot.children.forEach { data ->
                    val key = data.key
                    val value = data.value
                    if (key != null && value != null) {
                        Log.d("FirebaseData", "Key: $key, Value: $value")
                        val channel = Channel(key, value.toString())
                        list.add(channel)
                    }
                }
                Log.d("FirebaseData", "Parsed Channel List: $list")
                _channels.value = list
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseData", "Failed to fetch data", exception)
            }
    }

    fun addChannel (name: String){
        val key =firebaseDatabase.getReference("Channel").push().key
        firebaseDatabase.getReference("Channel").child(key!!).setValue(name).addOnSuccessListener {
            getChannels()
        }
    }
}

