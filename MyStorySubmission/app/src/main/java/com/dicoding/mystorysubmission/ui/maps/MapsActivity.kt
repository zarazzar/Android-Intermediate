package com.dicoding.mystorysubmission.ui.maps

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.data.Result
import com.dicoding.mystorysubmission.data.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.mystorysubmission.databinding.ActivityMapsBinding
import com.dicoding.mystorysubmission.ui.detail.DetailActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var markerList: List<ListStoryItem>
    private val boundsBuilder = LatLngBounds.builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupMapStyle()
        getLocation()

        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 15f))
            Toast.makeText(this@MapsActivity, getString(R.string.map_toDetail), Toast.LENGTH_SHORT)
                .show()
            true
        }

        mMap.setOnInfoWindowClickListener {
            val intentToDetail = Intent(this@MapsActivity, DetailActivity::class.java)
            intentToDetail.putExtra(DetailActivity.EXTRA_ID, it.tag.toString())
            startActivity(intentToDetail)
        }
    }

    private fun getLocation() {
        mapsViewModel.getAllLocation().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        loadToast(false)
                        markerList = result.data.listStory
                        addMarker()
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.failed_getLocation),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Loading -> {
                        loadToast(true)
                    }
                }
            }
        }
    }

    private fun addMarker() {
        mMap.clear()
        markerList.forEach { data ->
            val latlng = LatLng(data.lat, data.lon)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(data.name)
                    .snippet(data.description)
            )
            boundsBuilder.include(latlng)
            marker?.tag = data.id
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300,
            )
        )
    }

    private fun setupMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@MapsActivity,
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Parsing style failed.")
            }
        } catch (exc: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Err: ", exc)
            Toast.makeText(this@MapsActivity, getString(R.string.failed_getMap), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun loadToast(isLoading: Boolean) {
        if (isLoading) {
            Toast.makeText(this@MapsActivity, getString(R.string.map_loading), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this@MapsActivity, getString(R.string.map_loaded), Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }

}