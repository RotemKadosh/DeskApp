package com.example.support.map

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.RefreshViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker

class MapFragment : Fragment() {

    private lateinit var viewModel: MapViewModel
    private val refreshViewModel: RefreshViewModel by activityViewModels()
    private var marker : Marker? = null
    private lateinit var map : GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val markerOptions = viewModel.getMarkerOption()
        val cameraUpdate = viewModel.getCameraUpdate()
        marker = map.addMarker(markerOptions)
        map.moveCamera(cameraUpdate)
        map.uiSettings.isMapToolbarEnabled = false
        if (viewModel.isMarkerShown.value == false){
            changeMarkerVisibility(marker)
        }
        viewModel.selectedProperty.observe(viewLifecycleOwner,{
            onSelectedPropertyChange()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = MapViewModelFactory( MapFragmentArgs.fromBundle(requireArguments()).selectedProperty)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)
        refreshViewModel.members.observe(viewLifecycleOwner, {
            viewModel.updateSelectedProperty(it)
        })
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    private fun onSelectedPropertyChange() {
        viewModel.selectedProperty.value?.location?.let {
            marker?.position = it
        }
        val cameraUpdate = viewModel.getCameraUpdate()
        map.moveCamera(cameraUpdate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
        val item = menu.findItem(R.id.show_or_hide_marker_action)
        viewModel.isShowenButtonResource.observe(viewLifecycleOwner, {
            changeIcon(item)
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                this.findNavController().navigateUp()
                return true
            }
            R.id.refresh_action -> {
                refreshViewModel.refreshData()
                return true
            }
            R.id.show_or_hide_marker_action -> {
                viewModel.onCangeVisibilityClicked()
                changeMarkerVisibility(marker)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun changeIcon(item: MenuItem) {
        viewModel.isShowenButtonResource.value?.let { item.setIcon(it) }
    }

    private fun changeMarkerVisibility(marker: Marker?) {
        marker?.let{
            it.isVisible = it.isVisible != true
        }
    }

}