package com.example.criminal_intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminal_intent.databinding.ListItemCrimeBinding
import java.util.UUID

class CrimeHolder (
    val binding : ListItemCrimeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(crime : Crime, onCrimeClicked : (crimeId: UUID) -> Unit) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            //Toast.makeText(binding.root.context, "${crime.title} clicked", Toast.LENGTH_SHORT).show()
            onCrimeClicked(crime.id)
        }
        binding.crimeSolved.visibility = if(crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class CrimeListAdapter (
    private val crimes: List<Crime>,
    private val onClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.apply {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = crime.date.toString()
        }
        holder.bind(crime, onClicked)
    }

}