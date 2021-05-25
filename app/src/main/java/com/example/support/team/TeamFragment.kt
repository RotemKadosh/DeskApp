package com.example.support.team

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.databinding.TeamFragmentBinding


class TeamFragment: Fragment() {

    lateinit var binding : TeamFragmentBinding
    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }

    private val viewAdapter = MemberViewAdapter(MemberViewAdapter.OnClickListener {
        viewModel.displayMemberDetails(it)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TeamFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.teamList.adapter = viewAdapter

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
        val options: Int = searchView.imeOptions
        searchView.imeOptions = options or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewAdapter.filter.filter(newText)
                if(newText?.isEmpty() == true){
                    viewModel.searchText = null
                }
                else{
                    viewModel.searchText = newText
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        if (viewModel.searchText != null){
            searchView.requestFocus()
            searchView.setQuery(viewModel.searchText, true )
            searchView.isIconified = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searc_action){
            return false
        }
        return super.onOptionsItemSelected(item)
    }


}

