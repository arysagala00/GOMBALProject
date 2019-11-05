package com.trioafk.gombal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class EditProfilAct : AppCompatActivity() {

    var photo_location: Uri? = null
    var photo_max: Int = 1

    lateinit var photo_edit_profile:ImageView
    lateinit var btn_add_new_photo:Button
    lateinit var btn_save_profile:Button
    lateinit var xnama_lengkap:EditText
    lateinit var xbio:EditText
    lateinit var xusername:EditText
    lateinit var xpassword:EditText
    lateinit var xemail_address:EditText
    lateinit var btn_back_enam:LinearLayout

    lateinit var reference: DatabaseReference
    lateinit var storage: StorageReference

    internal var USERNAME_KEY = "usernamekey"
    internal var username_key = ""
    internal var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        getUsernameLocal()

        photo_edit_profile = findViewById(R.id.photo_edit_profile)
        xnama_lengkap = findViewById(R.id.xnama_lengkap)
        xbio = findViewById(R.id.xbio)
        xusername = findViewById(R.id.xusername)
        xpassword = findViewById(R.id.xpassword)
        xemail_address = findViewById(R.id.xemail_address)
        btn_save_profile = findViewById(R.id.btn_save_profile)
        btn_add_new_photo = findViewById(R.id.btn_add_new_photo)
        btn_back_enam = findViewById(R.id.btn_back_enam)

        reference = FirebaseDatabase.getInstance().reference.child("Users").child(username_key_new!!)

        storage = FirebaseStorage.getInstance().reference.child("Photousers").child(username_key_new!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                xnama_lengkap.setText(dataSnapshot.child("nama_lengkap").value!!.toString())
                xbio.setText(dataSnapshot.child("bio").value!!.toString())
                xusername.setText(dataSnapshot.child("username").value!!.toString())
                xpassword.setText(dataSnapshot.child("password").value!!.toString())
                xemail_address.setText(dataSnapshot.child("email_address").value!!.toString())

                Picasso.with(this@EditProfilAct)
                    .load(dataSnapshot.child("url_photo_profile").value!!.toString()).centerCrop()
                    .fit().into(photo_edit_profile)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        btn_save_profile.setOnClickListener {
            btn_save_profile.isEnabled = false
            btn_save_profile.text = "Loading ..."

            if(photo_location==null){
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("username").setValue(xusername.text.toString())
                        dataSnapshot.ref.child("password").setValue(xpassword.text.toString())
                        dataSnapshot.ref.child("bio").setValue(xbio.text.toString())
                        dataSnapshot.ref.child("nama_lengkap").setValue(xnama_lengkap.text.toString())
                        dataSnapshot.ref.child("email_address").setValue(xemail_address.text.toString())
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

                val gotobackrprofile = Intent(baseContext, MyProfileAct::class.java)
                startActivity(gotobackrprofile)
                finish()
            }

            else if(photo_location!=null){
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("username").setValue(xusername.text.toString())
                        dataSnapshot.ref.child("password").setValue(xpassword.text.toString())
                        dataSnapshot.ref.child("bio").setValue(xbio.text.toString())
                        dataSnapshot.ref.child("nama_lengkap").setValue(xnama_lengkap.text.toString())
                        dataSnapshot.ref.child("email_address").setValue(xemail_address.text.toString())
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

                val storageReference1 = storage.child(
                    System.currentTimeMillis().toString() + "." + getFileExtension(photo_location!!)
                )
                storageReference1.putFile(photo_location!!).addOnSuccessListener {
                    storageReference1.downloadUrl.addOnSuccessListener { uri ->
                        val uri_photo = uri.toString()
                        reference.ref.child("url_photo_profile").setValue(uri_photo)
                    }.addOnCompleteListener {
                        val gotobackrprofile = Intent(baseContext, MyProfileAct::class.java)
                        startActivity(gotobackrprofile)
                        finish()
                    }
                }.addOnCompleteListener {

                }
            }
        }

        btn_add_new_photo.setOnClickListener { findPhoto() }

        btn_back_enam.setOnClickListener { onBackPressed() }
    }

    fun findPhoto() {
        val pic = Intent()
        pic.type = "image/*"
        pic.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pic, photo_max)
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == photo_max && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            photo_location = data.data!!
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile)
        }
    }

    fun getUsernameLocal() {
        val sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key, "")
    }
}
