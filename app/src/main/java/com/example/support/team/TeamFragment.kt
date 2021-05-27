package com.example.support.team

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.RefreshViewModel
import com.example.support.databinding.TeamFragmentBinding
import kotlinx.coroutines.*


class TeamFragment : Fragment() {

    lateinit var binding: TeamFragmentBinding
    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }
    private val refreshViewModel: RefreshViewModel by activityViewModels()

    private val viewAdapter = MemberViewAdapter(MemberViewAdapter.OnClickListener {
        viewModel.displayMemberDetails(it)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TeamFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.refreshViewModel = refreshViewModel
        binding.teamList.adapter = viewAdapter

        viewModel.navigateToSelectedMember.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(TeamFragmentDirections.actionShowDetail(it))
                viewModel.displayMemberDetailsComplete()
            }
        })
        refreshViewModel.members.observe(viewLifecycleOwner,{
            viewAdapter.submitList(it.toMutableList())
        })
        refreshViewModel.refreshData()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team_menu, menu)
        val itemSearch = menu.findItem(R.id.searc_action)
        val searchView: SearchView = itemSearch.actionView as SearchView
        setQueryTextListener(searchView)
        restoreSearchFocusIfExist(searchView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searc_action) {
            return false
        }
        else if(item.itemId == R.id.refresh_action){
            refreshViewModel.refreshData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun restoreSearchFocusIfExist(searchView: SearchView) {
        if (viewModel.searchText != null) {
            searchView.requestFocus()
            searchView.setQuery(viewModel.searchText, true)
            searchView.isIconified = false
        }
    }

    private fun setQueryTextListener(searchView: SearchView) {
        val options: Int = searchView.imeOptions
        searchView.imeOptions = options or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var queryTextListenerJob: Job? = null

            override fun onQueryTextChange(newText: String?): Boolean {
                val delayTime : Long = 500
                queryTextListenerJob?.cancel()
                queryTextListenerJob = lifecycleScope.launch{
                    delay(delayTime)
                    viewAdapter.filter.filter(newText)
                }
                viewModel.updateSearchText(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

}

