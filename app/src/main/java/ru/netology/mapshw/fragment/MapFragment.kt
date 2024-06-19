package ru.netology.mapshw.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.mapshw.R
import ru.netology.mapshw.databinding.MapFragmentBinding
import ru.netology.mapshw.databinding.PlaceBinding
import ru.netology.mapshw.viewmodel.MapViewModel
import com.yandex.mapkit.Animation

class MapFragment: Fragment() {

    companion object {
        const val LAT_KEY = "LAT_KEY"
        const val LONG_KEY = "LONG_KEY"
    }
    private var mapView: MapView? = null
    private lateinit var userLocation: UserLocationLayer
    private var listener = object : InputListener {
        override fun onMapTap(map: Map, point: Point)  = Unit

        override fun onMapLongTap(map: Map, point: Point) {
            AddPlaceDialog.createBundle(point.latitude, point.longitude)
                .show(childFragmentManager, null)
        }

    }

    private val locationObjectListener = object : UserLocationObjectListener {
        override fun onObjectAdded(view: UserLocationView) = Unit

        override fun onObjectRemoved(view: UserLocationView) = Unit

        override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {
            userLocation.cameraPosition()?.target?.let{
                mapView?.map?.move(CameraPosition(it, 10F, 0F, 0F))
            }
            userLocation.setObjectListener(null)
        }

    }

    private val viewModel by viewModels<MapViewModel>()

    private val placeTapListener = MapObjectTapListener { mapObject, point ->
        val id = mapObject.userData as Long
        val deleteFragment = DeletePlaceDialog.newInstance(id)
        deleteFragment.show(childFragmentManager, "deleteDialog")
        true
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {granted ->
            when {
                granted -> {
                    MapKitFactory.getInstance().resetLocationManagerToDefault()
                    userLocation.cameraPosition()?.target?.also {
                        val map = mapView?.map ?: return@registerForActivityResult
                        val cameraPosition = map.cameraPosition
                        map.move(
                            CameraPosition(
                                it,
                                cameraPosition.zoom,
                                cameraPosition.azimuth,
                                cameraPosition.tilt
                            )
                        )
                    }
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "@string/location_permission",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = MapFragmentBinding.inflate(inflater, container, false)

        mapView = binding.map.apply {
            userLocation = MapKitFactory.getInstance().createUserLocationLayer(mapWindow)
            userLocation.isVisible = true
            userLocation.isHeadingEnabled = false

            map.addInputListener(listener)

            val collection = map.mapObjects.addCollection()
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.places.collectLatest { places ->
                        collection.clear()
                        places.forEach { place ->
                            val placeBinding = PlaceBinding.inflate(layoutInflater)
                            placeBinding.title.text = place.name
                            collection.addPlacemark(
                                Point(place.latitude, place.longitude),
                                ViewProvider(placeBinding.root)
                            ).apply {
                                userData = place.id
                            }
                        }
                    }
                }
            }
            collection.addTapListener(placeTapListener)

            val arguments = arguments
            if (arguments != null && arguments.containsKey(LAT_KEY) && arguments.containsKey(LONG_KEY))
            {
                val cameraPosition = map.cameraPosition
                map.move(
                    CameraPosition(
                        Point(arguments.getDouble(LAT_KEY), arguments.getDouble(LONG_KEY)),
                        10F,
                        cameraPosition.azimuth,
                        cameraPosition.tilt
                    )
                )
                arguments.remove(LAT_KEY)
                arguments.remove(LONG_KEY)

            } else {
                userLocation.setObjectListener(locationObjectListener)
            }
        }

        binding.plus.setOnClickListener {
            binding.map.map.move(
                CameraPosition(
                    binding.map.map.cameraPosition.target,
                    binding.map.map.cameraPosition.zoom + 1, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1F),
                null
            )
        }

        binding.minus.setOnClickListener {
            binding.map.map.move(
                CameraPosition(
                    binding.map.map.cameraPosition.target,
                    binding.map.map.cameraPosition.zoom - 1, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1F),
                null,
            )
        }


        binding.location.setOnClickListener{
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }


        return binding. root

        }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        mapView = view.findViewById(R.id.map)
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView?.onStart()
        }

    override fun onStop() {
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView = null
    }
}