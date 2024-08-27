package com.example.budgetapp.views.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetapp.R
import com.example.budgetapp.adapters.AccountsAdapter
import com.example.budgetapp.adapters.CategoryAdapter
import com.example.budgetapp.databinding.FragmentAddTransactionBinding
import com.example.budgetapp.databinding.ListDialogBinding
import com.example.budgetapp.models.Account
import com.example.budgetapp.models.Category
import com.example.budgetapp.models.Transaction
import com.example.budgetapp.utils.Constants
import com.example.budgetapp.utils.Helper
import com.example.budgetapp.views.activities.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTransactionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    lateinit var transaction: Transaction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(inflater)


        transaction = Transaction()

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background =  ContextCompat.getDrawable(requireContext(), R.drawable.income_selector)
            binding.expenseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))
            transaction.type = Constants.INCOME
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_selector)
            binding.expenseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))
            transaction.type = Constants.EXPENSE
        }

        binding.date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { datePicker: DatePicker, _: Int, _: Int, _: Int ->
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_MONTH] = datePicker.dayOfMonth
                calendar[Calendar.MONTH] = datePicker.month
                calendar[Calendar.YEAR] = datePicker.year

                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                val dateToShow: String = Helper.formatDate(calendar.time)

                binding.date.setText(dateToShow)

                transaction.date = calendar.time
                transaction.id = calendar.time.time
            }
            datePickerDialog.show()
        }

        binding.category.setOnClickListener {
            val dialogBinding: ListDialogBinding = ListDialogBinding.inflate(inflater)
            val categoryDialog =
                AlertDialog.Builder(context).create()
            categoryDialog.setView(dialogBinding.getRoot())


            val categoryAdapter: CategoryAdapter? = context?.let {
                Constants.categories?.let { it1 ->
                    CategoryAdapter(
                        it,
                        it1,
                        object : CategoryAdapter.CategoryClickListener {
                            override fun onCategoryClicked(category: Category?) {
                                binding.category.setText(category!!.categoryName)
                                transaction.category = category.categoryName
                                categoryDialog.dismiss()
                            }
                        })
                }
            }
            dialogBinding.recyclerView.setLayoutManager(GridLayoutManager(context, 3))
            dialogBinding.recyclerView.setAdapter(categoryAdapter)
            categoryDialog.show()
        }

        binding.account.setOnClickListener {
            val dialogBinding: ListDialogBinding = ListDialogBinding.inflate(inflater)
            val accountsDialog =
                AlertDialog.Builder(context).create()
            accountsDialog.setView(dialogBinding.getRoot())

            val accounts: ArrayList<Account> = ArrayList()
            accounts.add(Account(0, "Cash"))
            accounts.add(Account(0, "Bank"))
            accounts.add(Account(0, "PayTM"))
            accounts.add(Account(0, "EasyPaisa"))
            accounts.add(Account(0, "Other"))

            val adapter: AccountsAdapter? =
                context?.let {
                    AccountsAdapter(it, accounts, object : AccountsAdapter.AccountsClickListener {
                        override fun onAccountSelected(account: Account?) {
                            binding.account.setText(account!!.accountName)
                            transaction.account = account.accountName
                            accountsDialog.dismiss()
                        }
                    })
                }
            dialogBinding.recyclerView.setLayoutManager(LinearLayoutManager(context))
            dialogBinding.recyclerView.setAdapter(adapter)
            accountsDialog.show()
        }

        binding.saveTransactionBtn.setOnClickListener {
            val amount: Double = binding.amount.getText().toString().toDouble()
            val note: String = binding.note.getText().toString()

            if (transaction.type.equals(Constants.EXPENSE)) {
                transaction.amount = amount * -1
            } else {
                transaction.amount = amount
            }

            transaction.note = note

            (activity as MainActivity?)!!.viewModel.addTransaction(transaction)
            (activity as MainActivity?)?.transactions
            dismiss()
        }

        return binding.getRoot()
    }
}