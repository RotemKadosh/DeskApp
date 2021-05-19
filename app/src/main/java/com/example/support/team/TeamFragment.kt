package com.example.support.team

import android.app.SearchManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.databinding.TeamFragmentBinding
import com.example.support.R
import com.example.support.networking.TeamMemberApiFilter

class TeamFragment: Fragment() {

    lateinit var binding : TeamFragmentBinding
    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TeamFragmentBinding.inflate(inflater)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team_menu, menu)
        val itemSearch = menu.findItem(R.id.searc_action)
        val searchView : SearchView = itemSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = (binding.teamList.adapter as MemberViewAdapter)
                adapter.filter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searc_action){
            return false
        }
        return super.onOptionsItemSelected(item)
    }


}

