package com.example.chatapp.chat

import androidx.lifecycle.ViewModel
import com.example.chatapp.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(): ViewModel(){
    private val _messages= MutableStateFlow<List<Message>>(emptyList())
    val message =_messages.asStateFlow()
    private val db= Firebase.database

    fun listenForMessages(channelID: String){
        db.getReference("messages").child(channelID).orderByChild("createdAt")
            //timestamp:26:54
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list =mutableListOf<Message>()
                    snapshot.children.forEach { data->
                        val message=data.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                    }
                        _messages.value=list
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}