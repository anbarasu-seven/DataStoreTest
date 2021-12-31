package com.example.datastoretest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataStore: DataStoreManager
    private var nameText: EditText? = null
    private var emailField: EditText? = null
    private var mobileField: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataStore = DataStoreManager(this)

        nameText = findViewById<EditText>(R.id.nameText)
        emailField = findViewById<EditText>(R.id.emailField)
        mobileField = findViewById<EditText>(R.id.mobileField)


        findViewById<Button>(R.id.saveButton).setOnClickListener {
            //get data
            val name = nameText?.text.toString()
            val email = emailField?.text.toString()
            val mobile = mobileField?.text.toString()

            //save to pref data store in ASYNC manner
            GlobalScope.launch {
                val user = UserData(name, email, mobile)
                dataStore.save(user)
            }
        }


        loadData()
    }

    private fun loadData() {
        GlobalScope.launch{
            val userDataFlow: Flow<UserData> = dataStore.getFromDataStore()
            val userData = userDataFlow.first()

            GlobalScope.launch(Dispatchers.Main){
                nameText?.setText(userData.name)
                emailField?.setText(userData.email)
                mobileField?.setText(userData.phoneNo)
            }
        }
    }
}