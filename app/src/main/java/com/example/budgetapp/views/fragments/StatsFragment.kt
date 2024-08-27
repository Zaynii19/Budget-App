package com.example.budgetapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentStatsBinding
import com.example.budgetapp.utils.Constants
import com.example.budgetapp.utils.Constants.SELECTED_STATS_TYPE
import com.example.budgetapp.utils.Helper
import com.example.budgetapp.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar
import kotlin.math.abs

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var calendar: Calendar


    /*
    0 = Daily
    1 = Monthly
     */
    private var viewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        calendar = Calendar.getInstance()
        updateDate()

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.income_selector)
            binding.expenseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))

            SELECTED_STATS_TYPE = Constants.INCOME
            updateDate()
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_selector)
            binding.expenseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))

            SELECTED_STATS_TYPE = Constants.EXPENSE
            updateDate()
        }

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1)
            } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1)
            } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Monthly") {
                    Constants.SELECTED_TAB_STATS = 1
                    updateDate()
                } else if (tab.text == "Daily") {
                    Constants.SELECTED_TAB_STATS = 0
                    updateDate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })


        val pie: Pie = AnyChart.pie()

        viewModel!!.categoriesTransactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions!!.size > 0) {
                binding.emptyState.setVisibility(View.GONE)
                binding.anyChart.visibility = View.VISIBLE

                val data: MutableList<DataEntry> = ArrayList()

                val categoryMap: MutableMap<String, Double> = HashMap()

                for (transaction in transactions) {
                    val category: String = transaction.category!!
                    val amount: Double = transaction.amount

                    if (categoryMap.containsKey(category)) {
                        var currentTotal = categoryMap[category]!!
                        currentTotal += abs(amount)

                        categoryMap[category] = currentTotal
                    } else {
                        categoryMap[category] = abs(amount)
                    }
                }

                for ((key, value) in categoryMap) {
                    data.add(ValueDataEntry(key, value))
                }
                pie.data(data)
            } else {
                binding.emptyState.setVisibility(View.VISIBLE)
                binding.anyChart.visibility = View.GONE
            }
        }

        viewModel!!.getTransactions(calendar, SELECTED_STATS_TYPE)

        binding.anyChart.setChart(pie)


        return binding.getRoot()
    }

    fun updateDate() {
        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            binding.currentDate.text = Helper.formatDate(calendar.time)
        } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        viewModel!!.getTransactions(calendar, SELECTED_STATS_TYPE)
    }
}