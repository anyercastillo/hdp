package com.anyer.hdp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.anyer.hdp.databinding.FragmentDetailsBluetoothBinding
import com.anyer.hdp.ui.devices.DevicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel: DevicesViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBluetoothBinding
    private lateinit var address: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        address = DetailsFragmentArgs.fromBundle(requireArguments()).address

        binding = FragmentDetailsBluetoothBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.button.setOnClickListener {
            viewModel.readBodySensorLocation(address)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDevice(address).observe(viewLifecycleOwner, Observer {
            binding.device = it
        })

        viewModel.readBodySensorLocation(address)
        viewModel.subscribeToHearRateMeasurement(address)
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.disconnectDevice(address)
    }
}