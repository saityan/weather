package ru.geekbrains.weather.maps

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentGoogleMapsMainBinding

class MapsFragment : Fragment() {
    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding: FragmentGoogleMapsMainBinding
        get() = _binding!!

    private lateinit var googleMap: GoogleMap

    private val markers: ArrayList<Marker> = arrayListOf()

    private val callback = OnMapReadyCallback { googleMap ->
        this@MapsFragment.googleMap = googleMap

        val defaultLocation = LatLng(55.7, 37.6)
        googleMap.addMarker(
            MarkerOptions().position(defaultLocation).title("Marker in default location")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        with(this@MapsFragment.googleMap) {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setOnMapLongClickListener {
                moveToLocation(it)
                addMapMarker(it)
                drawMapLine()
            }
        }
    }

    private fun GoogleMap.addMapMarker(it: LatLng) {
        this.addMarker(
            MarkerOptions()
                .icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_pin)
                )
                .position(it)
                .title("")
        )?.let { it1 ->
            markers.add(
                it1
            )
        }
    }

    private fun GoogleMap.drawMapLine() {
        val last = markers.size - 1
        if (last > 0) {
            val startMarker = markers[last - 1].position
            val endMarker = markers[last].position
            this.addPolyline(
                PolylineOptions()
                    .add(startMarker, endMarker)
                    .color(Color.DKGRAY)
                    .width(6f)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            val geocoder = Geocoder(requireContext())
            val addressRow = binding.searchAddress.text.toString()
            val address = geocoder.getFromLocationName(addressRow, 1)
            val location = LatLng(address[0].latitude, address[0].longitude)
            googleMap.clear()
            moveToLocation(location)
        }
    }

    private fun moveToLocation(location: LatLng) {
        googleMap.addMarker(MarkerOptions().position(location))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}
