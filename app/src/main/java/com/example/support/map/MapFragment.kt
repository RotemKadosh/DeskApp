package com.example.support.map

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.RefreshViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker

class MapFragment : Fragment() {


    private lateinit var viewModel: MapViewModel
//    private lateinit var binding: MapFragmentBinding
    private val refreshViewModel: RefreshViewModel by activityViewModels()
    private var marker : Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        val markerOptions = viewModel.getMarkerOption()
        val cameraUpdate = viewModel.getCameraUpdate()
        marker = googleMap.addMarker(markerOptions)
        googleMap.moveCamera(cameraUpdate)
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
        googleMap.uiSettings.isMapToolbarEnabled = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = MapViewModel(MapFragmentArgs.fromBundle(requireArguments()).selectedProperty)
        refreshViewModel.members.observe(viewLifecycleOwner, {
            viewModel.updateSelectedProperty(it)
        })
//        binding = MapFragmentBinding.inflate(inflater)
//        binding.lifecycleOwner = this
//        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.show_or_hide_marker_action)
        viewModel.isShowenButtonResource.observe(viewLifecycleOwner, {
                changeIcon(item)
        })
        Log.d("change icon", "onPrepareOptionsMenu")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            this.findNavController().navigateUp()
            return true
        } else if (item.itemId == R.id.refresh_action) {
            refreshViewModel.refreshData()
            item.collapseActionView()
            return true
        }
        else if(item.itemId == R.id.show_or_hide_marker_action){
            viewModel.onButtonclicked()
            Log.d("icon", "onButtonclicked : " + viewModel.isMarkerShown.value.toString())
            changeMarkerVisibility(marker)
            changeIcon(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun changeIcon(item: MenuItem) {
        Log.d("change icon", "changeIcon : "+ viewModel.isShowenButtonResource.toString())
        viewModel.isShowenButtonResource.value?.let { item.setIcon(it) }
        Log.d("change icon", "setIcon to : " + viewModel.isShowenButtonResource.value?.toString() )
    }

    private fun changeMarkerVisibility(marker: Marker?) {
        marker?.let{
            it.isVisible = it.isVisible != true
        }
    }

    inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private var window: View = layoutInflater.inflate(R.layout.info_window, null)

        override fun getInfoWindow(p0: Marker): View? {
            render(window)
            return window
        }

        override fun getInfoContents(p0: Marker): View? {
            return null
        }

        fun render(view: View) {

            viewModel.infoResource.value?.let {
                view.findViewById<ImageView>(R.id.infoWindowImage).setImageResource(it)
            }
            val titleUi = view.findViewById<TextView>(R.id.title)
            titleUi.text = viewModel.fullName.value

            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            snippetUi.text = viewModel.email.value
        }
    }
}