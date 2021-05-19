
package com.example.support.member

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.support.R
import com.example.support.databinding.MemberFragmentBinding
import com.example.support.networking.TeamMember
import com.squareup.picasso.Picasso

class MemberFragment : Fragment() {
    private lateinit var binding: MemberFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(activity).application
        binding = MemberFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val teamMember: TeamMember = MemberFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = MemberViewModelFactory(teamMember, application)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MemberViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.isAvailable.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.availableImage.setImageResource(R.drawable.ic_baseline_available)
            }
            else{
                binding.availableImage.setImageResource(R.drawable.ic_baseline_block)
            }
        })

        Picasso.with(application).load(viewModel.selectedProperty.value?.imgSrcUrl).into(this.binding.mainPhotoImage)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.member_menu, menu)
        val backItem  = menu.findItem(R.id.back_action)

        backItem.setOnMenuItemClickListener {
            this.findNavController().navigateUp()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.back_action){
            false
        }
        return super.onOptionsItemSelected(item)
    }
}