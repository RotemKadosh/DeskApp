
package com.example.support.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.support.R
import com.example.support.databinding.MemberFragmentBinding
import com.example.support.networking.TeamMember
import com.squareup.picasso.Picasso

class MemberFragment : Fragment() {
    private lateinit var binding: MemberFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        binding = MemberFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val teamMember: TeamMember = MemberFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = MemberViewModelFactory(teamMember, application)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MemberViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.isAvailable.observe(this, Observer {
            if(it){
                binding.availableImage?.setImageResource(R.drawable.ic_baseline_available)
            }
            else{
                binding.availableImage?.setImageResource(R.drawable.ic_baseline_block)
            }
        })

        Picasso.with(application).load(viewModel.selectedProperty.value?.imgSrcUrl).into(this.binding.mainPhotoImage)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.member_toolbar)
        toolbar.setNavigationOnClickListener{
            it.findNavController().navigate(R.id.action_back)
        }

        super.onViewCreated(view, savedInstanceState)
    }


}