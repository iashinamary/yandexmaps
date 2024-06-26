package ru.netology.mapshw.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.netology.mapshw.R
import ru.netology.mapshw.adapter.PlaceAdapter
import ru.netology.mapshw.databinding.PlacesFragmentBinding
import ru.netology.mapshw.dto.Place
import ru.netology.mapshw.utils.OnInteractionListener
import ru.netology.mapshw.viewmodel.MapViewModel

class PlacesFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PlacesFragmentBinding.inflate(inflater, container, false)

        val viewModel by viewModels<MapViewModel>()

        val adapter = PlaceAdapter(object: OnInteractionListener {
            override fun onClick(place: Place) {
                findNavController().navigate(
                    R.id.action_listFragment_to_mapFragment, bundleOf(
                        MapFragment.LAT_KEY to place.latitude,
                        MapFragment.LONG_KEY to place.longitude
                    )
                )
            }

            override fun onDelete(place: Place) {
                DeletePlaceDialog.newInstance(id = place.id).show(childFragmentManager, "deleteFragment")
            }

            override fun onEdit(place: Place) {
                AddPlaceDialog.createBundle(lat = place.latitude, long = place.longitude, id = place.id).show(childFragmentManager, "addPlaceFragment")
            }
        })

        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.places.collectLatest { places ->
                adapter.submitList(places)
                binding.empty.isVisible = places.isEmpty()
            }
        }

        return binding.root
    }
}