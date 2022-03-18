package com.irfan.githubuser2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.githubuser2.adapter.ListUserAdapter
import com.irfan.githubuser2.databinding.FragmentFollowerBinding
import com.irfan.githubuser2.response.ItemsList
import com.irfan.githubuser2.viewmodel.UserViewModel

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        startViewModel()
        return binding.root
    }

    private fun startViewModel() {
        userViewModel.listFollower.observe(viewLifecycleOwner) { listFollower ->
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvFollower.layoutManager = layoutManager
            setUserData(listFollower)
        }
    }

    private fun setUserData(listUsers: List<ItemsList>) {
        val adapter = ListUserAdapter(listUsers)
        if (adapter.itemCount == 0)
            binding.status.visibility = View.VISIBLE
        binding.rvFollower.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}