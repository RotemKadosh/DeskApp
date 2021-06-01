package com.example.support.map

import android.os.Bundle
import android.view.*
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
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val markerOptions = viewModel.getMarkerOption()
        val cameraUpdate = viewModel.getCameraUpdate()
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(cameraUpdate)
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
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.back_refresh_menu, menu)
        val backItem = menu.findItem(R.id.back_action)

        backItem.setOnMenuItemClickListener {
            this.findNavController().navigateUp()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.back_action) {
            return false
        } else if (item.itemId == R.id.refresh_action) {
            refreshViewModel.refreshData()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}