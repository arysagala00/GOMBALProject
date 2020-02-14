package com.trioafk.gombal

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
import org.json.JSONObject


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
                        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+myLoc?.latitude+","+myLoc?.longitude+"&radius=1500&type=bengkel&keyword=motor&key="
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

                btn_load.setText("Loading ...")
                btn_load.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    val listkita: ArrayList<mapsdata> = ArrayList<mapsdata>()
                    val ChatAdapter = mapsadapter(listkita,this)
                    val layoutManager = LinearLayoutManager(this)

                    tot.adapter = ChatAdapter
                    tot.layoutManager = layoutManager
                    tot.setHasFixedSize(true)

                    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+myLoc?.latitude+","+myLoc?.longitude+"&radius=1500&type=bengkel&keyword=Motor&key="
//              print("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+myLoc?.latitude+","+myLoc?.longitude+"&radius=1500&type=bengkel&keyword="+edtSearch.text+"&key=")
                    val request = StringRequest(
                        Request.Method.GET,
                        url,
                        Response.Listener { res ->
                            var it = JSONObject(res)
                            Log.d("JSON",res)
                            for(i in 0 until it.getJSONArray("results").length()){
                                val place = it.getJSONArray("results")[i] as JSONObject
                                val name = place.getString("name")
                                val latitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                                val longitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                                val loc = LatLng(latitude, longitude)

                                mMap.addMarker(MarkerOptions().position(loc).title(name))
                                var alamat: String = place.getString("vicinity")
                                var rating: String = place.getString("rating")
                                listkita.add(mapsdata(name,alamat,rating))
                            }
                            ChatAdapter.notifyDataSetChanged()
                        },
                        Response.ErrorListener {

                        })

                    val queue = Volley.newRequestQueue(this)
                    queue.add(request)

                    btn_load.text = "CARI"
                    btn_load.isEnabled = true

                }, 2000)

            }

    }



    fun showSelectedWorkshop(mapsdata: mapsdata){
        val goToDetail = Intent(this@MapsActivity,DetailOrder::class.java)
        goToDetail.putExtra(DetailOrder.EXTRA_NAME, mapsdata.nama)
        startActivity(goToDetail)
    }
}
