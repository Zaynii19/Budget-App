package com.example.budgetapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetapp.adapters.TransactionsAdapter
import com.example.budgetapp.databinding.FragmentTransactionsBinding
import com.example.budgetapp.utils.Constants
import com.example.budgetapp.utils.Helper
import com.example.budgetapp.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar

class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding

    private lateinit var calendar: Calendar

    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
     */
    private var viewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        calendar = Calendar.getInstance()
        updateDate()

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1)
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1)
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }


        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(parentFragmentManager, null)
        }


        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Monthly") {
                    Constants.SELECTED_TAB = 1
                    updateDate()
                } else if (tab.text == "Daily") {
                    Constants.SELECTED_TAB = 0
                    updateDate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        binding.transactionsList.setLayoutManager(LinearLayoutManager(context))

        viewModel!!.transactions.observe(viewLifecycleOwner) { value ->
            if (value != null) {
                val transactionsAdapter = TransactionsAdapter(requireActivity(), value)
                binding.transactionsList.adapter = transactionsAdapter

                if (value.isNotEmpty()) {
                    binding.emptyState.visibility = View.GONE
                } else {
                    binding.emptyState.visibility = View.VISIBLE
                }
            }
        }

        viewModel!!.totalIncome.observe(viewLifecycleOwner) { value ->
            binding.incomeLbl.text = value.toString()
        }

        viewModel!!.totalExpense.observe(viewLifecycleOwner) { value ->
            binding.expenseLbl.text = value.toString()
        }

        viewModel!!.totalAmount.observe(viewLifecycleOwner) { value ->
            binding.totalLbl.text = value.toString()
        }
        viewModel!!.getTransactions(calendar)


        return binding.root
    }

    fun updateDate() {
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.text = Helper.formatDate(calendar.time)
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        viewModel?.getTransactions(calendar)
    }
}