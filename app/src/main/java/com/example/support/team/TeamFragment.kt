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

    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TeamFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.teamList.adapter = MemberViewAdapter(MemberViewAdapter.OnClickListener {
            viewModel.displayMemberDetails(it)
        })

        viewModel.navigateToSelectedMember.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(TeamFragmentDirections.actionShowDetail(it))
                viewModel.displayMemberDetailsComplete()
            }
        })

        return binding.root
    }

//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.team_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        viewModel.updateFilter(
//            when (item.itemId) {
//                R.id.occupied_option -> TeamMemberApiFilter.SHOW_BLOCK
//                R.id.available_option -> TeamMemberApiFilter.SHOW_AVAILABLE
//                else -> TeamMemberApiFilter.SHOW_ALL
//            }
//        )
//        return true
//    }
}

