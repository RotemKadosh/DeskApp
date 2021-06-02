package com.example.support.map

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.RefreshViewModel
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment() {


    private lateinit var viewModel: MapViewModel
    private val refreshViewModel: RefreshViewModel by activityViewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        val markerOptions = viewModel.getMarkerOption()
        val cameraUpdate = viewModel.getCameraUpdate()
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(cameraUpdate)
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
        inflater.inflate(R.menu.refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        return super.onOptionsItemSelected(item)
    }

}