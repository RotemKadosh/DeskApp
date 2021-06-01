package com.example.support.member

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.RefreshViewModel
import com.example.support.databinding.MemberFragmentBinding
import com.example.support.networking.TeamMember

class MemberFragment : Fragment() {
    private lateinit var binding: MemberFragmentBinding
    private val refreshViewModel: RefreshViewModel by activityViewModels()
    private lateinit var viewModel: MemberViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MemberFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val teamMember: TeamMember = MemberFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = MemberViewModelFactory(teamMember)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MemberViewModel::class.java)
        refreshViewModel.members.observe(viewLifecycleOwner, {
            viewModel.updateSelectedProperty(it)
        })

        viewModel.locationSelected.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(MemberFragmentDirections.actionShowLocation(it))
                viewModel.navigateToMemberLocationComplete()
            }
        })

        binding.locationText?.setOnClickListener {
            viewModel.navigateToMemberLocation()
        }

        binding.viewModel = viewModel
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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