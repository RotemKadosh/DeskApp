package com.example.support.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.support.databinding.TeamMemberViewBinding
import com.example.support.networking.TeamMember

class MemberViewAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<TeamMember,
            MemberViewAdapter.TeamMemberViewHolder>(DiffCallback) {


    class TeamMemberViewHolder(private var binding: TeamMemberViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: TeamMember) {
            binding.member = member

            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<TeamMember>() {
        override fun areItemsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
            return oldItem === newItem
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
}

