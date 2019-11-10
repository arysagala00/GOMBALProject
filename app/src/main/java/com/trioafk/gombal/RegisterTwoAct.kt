package com.trioafk.gombal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register_one.*
import kotlinx.android.synthetic.main.activity_register_two.*

class RegisterTwoAct : AppCompatActivity() {

    lateinit var photo_location:Uri
    var photo_max: Int = 1

    lateinit var reference: DatabaseReference
    lateinit var storage: StorageReference

    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)

        getUsernameLocal()

        btn_add_photo.setOnClickListener(){
            findPhoto()
        }

        btn_back_dua.setOnClickListener(){
            onBackPressed()
        }

        btn_continue_dua.setOnClickListener(){
            //Membuat tombol disable
            btn_continue_dua.isEnabled=false
            btn_continue_dua.setText("Loading ...")

            //menyimpan kepada firebase
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new!!)
            storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new!!)

            //validasi untuk file, apakah ada atau tidak
            if (photo_location != null) {

                val storageReference1 = storage.child(System.currentTimeMillis().toString() + "." + getFileExtension(photo_location)
                )
                storageReference1.putFile(photo_location).addOnSuccessListener {
                    storageReference1.downloadUrl.addOnSuccessListener { uri ->
                        val uri_photo = uri.toString()
                        reference.ref.child("url_photo_profile").setValue(uri_photo)
                        reference.ref.child("nama_lengkap").setValue(nama_lengkap.text.toString())
                        reference.ref.child("bio").setValue(bio.text.toString())
                    }.addOnCompleteListener {
                        val gotosuccess =
                            Intent(this@RegisterTwoAct, SuccessRegisterAct::class.java)
                        startActivity(gotosuccess)
                    }
                }.addOnCompleteListener {}
            }
        }
    }

    fun findPhoto(){
        val pic = Intent()
        pic.setType("image/*")
        pic.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(pic,photo_max)
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
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user)
        }
    }

    fun getUsernameLocal() {
        val sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key, "")
    }
}
