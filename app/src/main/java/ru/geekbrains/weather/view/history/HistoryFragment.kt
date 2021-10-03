package ru.geekbrains.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentHistoryBinding
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
    get() {
        return _binding!!
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    private val viewModel:HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_LONG).show()
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.SuccessMain -> {
                binding.historyFragmentRecyclerview.adapter = adapter
                binding.loadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                adapter.setWeather(weather)
                showSnackBarWithoutAction(binding.root, R.string.snackbar, Snackbar.LENGTH_SHORT)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showSnackBarWithoutAction(view: View, stringId: Int, length: Int) {
        Snackbar.make(view, getString(stringId), length).show()
    }
}