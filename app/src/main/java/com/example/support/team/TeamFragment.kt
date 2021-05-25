package com.example.support.team

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.databinding.TeamFragmentBinding
import kotlinx.coroutines.*


class TeamFragment : Fragment() {

    lateinit var binding: TeamFragmentBinding
    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this).get(TeamViewModel::class.java)
    }
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
            //Todo - handle refresh action logic here
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
     private fun updateSearchText(newText : String?){
         if (newText?.isEmpty() == true) {
             viewModel.searchText = null
         } else {
             viewModel.searchText = newText
         }
     }
    private fun setQueryTextListener(searchView: SearchView) {
        val options: Int = searchView.imeOptions
        searchView.imeOptions = options or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var queryTextListenerJob: Job? = null

            override fun onQueryTextChange(newText: String?): Boolean {
                queryTextListenerJob?.cancel()
                queryTextListenerJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    viewAdapter.filter.filter(newText)
                }
                updateSearchText(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

}

