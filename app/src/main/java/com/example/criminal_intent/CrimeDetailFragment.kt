package com.example.criminal_intent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminal_intent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Contract
import java.util.UUID
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM. dd"
private const val TAG = "CrimeDetailFragment"
class CrimeDetailFragment : Fragment() {
    private val selectSuspect = registerForActivityResult(ActivityResultContracts.PickContact()) {
        uri: Uri? ->
            uri?.let{parseContactSelection(it)}
    }

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
//            crimeDate.apply{
//                isEnabled = false
//            }

            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
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

            setFragmentResultListener(
                DatePickerFragment.REQUEST_KEY_DATE
            ) {
                _, bundle ->
                val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
                crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
            }
        }
    }

    private fun updateUi(crime: Crime) {
        binding.apply{
            if(crimeTitle.text.toString() != crime.title){
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeDate.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
            }
            crimeSolved.isChecked = crime.isSolved
            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply{
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject)
                    )
                }
                // startActivity(reportIntent)
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.crime_report)
                )
                startActivity(chooserIntent)
            }

            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }

        }

    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if(crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()

        val suspectText = if(crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect)
        }

        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )
    }

    private fun parseContactSelection(contractUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        // create a cursor object to navigate through the information in QueryFields
        // this will point to one row and one column. the row is the contact. the column
        // is the contact name.
        val queryCursor = requireActivity().contentResolver.query(contractUri, queryFields, null, null, null)

        //moveToFirst does two things: it moves to the front of the data ans it
        //returns true if there is data there
        queryCursor?.use{ cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime{ oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
            }
        }
    }
}