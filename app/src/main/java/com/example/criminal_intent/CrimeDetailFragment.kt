package com.example.criminal_intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.criminal_intent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.Date

private const val TAG = "CrimeDetailFragment"
class CrimeDetailFragment : Fragment() {
    // to make something null-able (can be null), use ?
    private var _binding : FragmentCrimeDetailBinding? = null
    private val binding : FragmentCrimeDetailBinding
        get() = checkNotNull(_binding) {
            "Error: Can we see the view?"
        }

    //private lateinit var crime: Crime
    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflates the view for us
//        return super.onCreateView(inflater, container, savedInstanceState)
        // last parameter is do we want to light up the view
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{// all binding / wireups go here
            crimeTitle.doOnTextChanged{ text, _, _, _ ->
                //crime.title = text.toString()
                crimeDetailViewModel.updateCrime{oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

//            crimeDate.setText(crime.title)
//            crimeDate.setOnClickListener{ view : View ->
//                crimeDate.setText(crime.isSolved.toString())
//            }
            crimeDate.apply{
                isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                crimeDetailViewModel.updateCrime{oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    crimeDetailViewModel.crime.collect{crime ->
                        // let is a scoping function, going to let you pass in an argument as if it were an object
                        crime?.let{updateUi(it)}
                    }
                }
            }
        }
    }

    private fun updateUi(crime: Crime) {
        binding.apply{
            if(crimeTitle.text.toString() != crime.title){
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
        }
    }
}