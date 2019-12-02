package com.trioafk.gombal

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private var myLoc: Location? = null
    private var url: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_back_home.setOnClickListener(){onBackPressed()}
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var locationClient : FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        var locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        var locationCallback: LocationCallback =
            object : LocationCallback(){
                override fun onLocationResult(loc: LocationResult?) {
                    loc?: return
                    for(location in loc.locations){
                        myLoc = location
                        val myLocation = LatLng(location.latitude, location.longitude)
                        print("fungsi"+myLoc?.latitude)
                        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+myLoc?.latitude+","+myLoc?.longitude+"&radius=1500&type=bengkel&keyword=motor&key=AIzaSyDxLLN6ngrM1nR2WJQXoOmHLCZJfM6ysto"
                        if(marker == null){
                            marker = mMap.addMarker(MarkerOptions().position(myLocation).title("Lokasi Saya").icon(

                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            ))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15.0f))
                        }else{
                            marker?.position = myLocation
                        }
                    }
                }
            }

        locationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()

        )




            btn_load.setOnClickListener {
                val listmaps: ArrayList<mapsdata> = ArrayList<mapsdata>()
                val mapsadapter=mapsadapter(listmaps,this)
                val layoutmanager=LinearLayoutManager(this)
                place.adapter=mapsadapter
                place.layoutManager=layoutmanager
                place.setHasFixedSize(true)
                mMap.addMarker(MarkerOptions().position(
                    LatLng(-7.776772899999999,110.3816376
                )).title("Mozes Motor"))
                listmaps.add(mapsdata("Mozes Motor","Jl. Colombo No. 3, Samirono, Karang Malang, Caturtunggal, Kota Yogyakarta","4.4"))
                mMap.addMarker(MarkerOptions().position(
                    LatLng(-7.788647999999999,110.378261
                    )).title("Anda Motor"))
                listmaps.add(mapsdata("Anda Motor","Jl. Dr. Wahidin Sudirohusodo No.1 A, Klitren, Kota Yogyakarta","4.4"))
                mapsadapter.notifyDataSetChanged()



                mapsadapter.setOnItemClickCallback(object : mapsadapter.OnItemClickCallback {
                    override fun onItemClicked(data: mapsdata?) {
                        showSelectedWorkshop(data!!)
                    }
                })
            }
    }



    fun showSelectedWorkshop(mapsdata: mapsdata){
        val goToDetail = Intent(this@MapsActivity,DetailOrder::class.java)
        goToDetail.putExtra(DetailOrder.EXTRA_NAME, mapsdata.nama)
        startActivity(goToDetail)
    }
}
