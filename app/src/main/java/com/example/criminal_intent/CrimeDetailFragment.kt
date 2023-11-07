package com.example.criminal_intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.criminal_intent.databinding.FragmentCrimeDetailBinding
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

    private lateinit var crime: Crime
    private val args: CrimeDetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // we do NOT call setContentView(R.layout.activity_main)
        crime = Crime(
            id = UUID.randomUUID(),
            title = "Bad Stuff Crime",
            date = Date(),
            isSolved = false
        )
        Log.d(TAG, "The crime ID: ${args.crimeId}")
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
                crime = crime.copy(title = text.toString())
                //crime.title = text.toString()
            }

            crimeDate.setText(crime.title)
            crimeDate.setOnClickListener{ view : View ->
                crimeDate.setText(crime.isSolved.toString())
            }

            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                crime = crime.copy(isSolved= isChecked)
            }
        }
    }
}