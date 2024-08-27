package com.example.budgetapp.views.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budgetapp.views.fragments.AccountFragment
import com.example.budgetapp.R
import com.example.budgetapp.databinding.ActivityMainBinding
import com.example.budgetapp.utils.Constants
import com.example.budgetapp.viewmodels.MainViewModel
import com.example.budgetapp.views.fragments.StatsFragment
import com.example.budgetapp.views.fragments.TransactionsFragment
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: MainViewModel
    private var calendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setSupportActionBar(binding.toolBar)
        supportActionBar!!.title = "Budget App"

        Constants.setCategories()

        calendar = Calendar.getInstance()

        // Load initial fragment
        loadFragment(TransactionsFragment(), "Transactions")

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transactions -> {
                    supportFragmentManager.popBackStack()
                    updateToolbarTitle("Transactions")
                }
                R.id.stats -> {
                    loadFragment(StatsFragment(), "Statistics")
                }
                R.id.accounts -> {
                    loadFragment(AccountFragment(), "Accounts")
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        updateToolbarTitle(title)
    }

    private fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    val transactions: Unit
        get() {
            viewModel.getTransactions(calendar)
        }
}
