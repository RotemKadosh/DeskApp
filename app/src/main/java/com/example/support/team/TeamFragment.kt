package com.example.support.team

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.databinding.TeamFragmentBinding
import com.example.support.R
import com.example.support.networking.TeamMemberApiFilter

class TeamFragment: Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TeamFragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.teamList.adapter = MemberViewAdapter(MemberViewAdapter.OnClickListener {
            viewModel.displayMemberDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMember.observe(this, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(TeamFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMemberDetailsComplete()
            }
        })


        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.occupied_option -> TeamMemberApiFilter.SHOW_BLOCK
                R.id.available_option -> TeamMemberApiFilter.SHOW_AVAILABLE
                else -> TeamMemberApiFilter.SHOW_ALL
            }
        )
        return true
    }
}

