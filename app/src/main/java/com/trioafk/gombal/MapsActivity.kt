package com.trioafk.gombal

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject
import android.os.AsyncTask.execute
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private var myLoc: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

            val listmaps: ArrayList<mapsdata> = ArrayList<mapsdata>()
            val mapsadapter = mapsadapter(listmaps,this)
            val layoutManager = LinearLayoutManager(this)

            place.adapter=mapsadapter
            place.layoutManager=layoutManager
            place.setHasFixedSize(true)

            val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+myLoc?.latitude+","+myLoc?.longitude+"&radius=1500&type=bengkel&keyword=bengkel+motor&key=AIzaSyBEbr-TjCmABy4jXQG2SP58z7V_2_E98VU"

            btn_load.setOnClickListener(){
                btn_load.isEnabled = false
                btn_load.setText("Loading ...")
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
                            var buka:String;
                            if(place.getJSONObject("opening_hours").getBoolean("open_now")){
                                buka="Buka"
                            }else{
                                buka="Tutup"
                            }
                            listmaps.add(mapsdata(name,buka,place.getString("rating")))
                            mMap.addMarker(MarkerOptions().position(loc).title(name))
                        }
                        mapsadapter.notifyDataSetChanged()
                    },
                    Response.ErrorListener {

                    })

                val queue = Volley.newRequestQueue(this)
                queue.add(request)
            }
    }
}
