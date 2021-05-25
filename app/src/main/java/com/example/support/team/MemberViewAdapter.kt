package com.example.support.team

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.support.databinding.TeamMemberViewBinding
import com.example.support.networking.TeamMember
import java.util.*

class MemberViewAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<TeamMember, MemberViewAdapter.TeamMemberViewHolder>(DiffCallback), Filterable {

    private lateinit var fullList : List<TeamMember>

    class TeamMemberViewHolder(private var binding: TeamMemberViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: TeamMember) {
            binding.member = member

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TeamMember>() {
        override fun areItemsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
            return (oldItem.available == newItem.available && oldItem.imgSrcUrl == newItem.imgSrcUrl)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeamMemberViewHolder {
        return TeamMemberViewHolder(TeamMemberViewBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        val teamMember = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(teamMember)
        }
        holder.bind(teamMember)
    }

    class OnClickListener(val clickListener: (teamMember: TeamMember) -> Unit) {
        fun onClick(teamMember: TeamMember) = clickListener(teamMember)
    }

    override fun submitList(list: MutableList<TeamMember>?) {
        if (list != null) {
            fullList = list
        }
        super.submitList(list)
    }

    private fun updateFilteredList(list: MutableList<TeamMember>?) {
        super.submitList(list)
    }
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.i("filter", "perform filtering called adapter")
                val query = constraint.toString().toLowerCase(Locale.ROOT).trim()
                var filteredList = mutableListOf<TeamMember>()
                if(query.isNotBlank()){
                    fullList?.let {
                        for(item in it){
                            if(item.firstName.toLowerCase().contains(query) || item.lastName.toLowerCase().contains(query)){
                                filteredList.add(item)
                            }
                        }
                    }
                }
                else{
                    filteredList = fullList as MutableList<TeamMember>
                }
                val filterResult = FilterResults()
                filterResult.values = filteredList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                updateFilteredList(results?.values as MutableList<TeamMember>)
            }

        }
    }

}

