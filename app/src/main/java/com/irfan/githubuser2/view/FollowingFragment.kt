package com.irfan.githubuser2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.githubuser2.adapter.ListUserAdapter
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.databinding.FragmentFollowingBinding
import com.irfan.githubuser2.viewmodel.UserViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        startViewModel()
        return binding.root
    }

    private fun startViewModel() {
        userViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvFollowing.layoutManager = layoutManager
            setUserData(listFollowing)
        }
    }

    private fun setUserData(listUsers: List<ItemsList>) {
        val adapter = ListUserAdapter(listUsers)
        if (adapter.itemCount == 0)
            binding.status2.visibility = View.VISIBLE
        binding.rvFollowing.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}