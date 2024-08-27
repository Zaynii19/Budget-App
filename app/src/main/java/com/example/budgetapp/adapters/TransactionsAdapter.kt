package com.example.budgetapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.R
import com.example.budgetapp.databinding.RowTransactionBinding
import com.example.budgetapp.models.Category
import com.example.budgetapp.models.Transaction
import com.example.budgetapp.utils.Constants
import com.example.budgetapp.utils.Helper
import com.example.budgetapp.views.activities.MainActivity
import io.realm.RealmResults

class TransactionsAdapter(var context: Context, private var transactions: RealmResults<Transaction>?) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction: Transaction? = transactions?.get(position)

        holder.binding.transactionAmount.text = transaction!!.amount.toString()
        holder.binding.accountLbl.text = transaction.account

        holder.binding.transactionDate.text = Helper.formatDate(transaction.date)
        holder.binding.transactionCategory.text = transaction.category

        val transactionCategory: Category? = Constants.getCategoryDetails(transaction.category)

        holder.binding.categoryIcon.setImageResource(transactionCategory!!.categoryImage)
        holder.binding.categoryIcon.setBackgroundTintList(
            context.getColorStateList(
                transactionCategory.categoryColor
            )
        )

        holder.binding.accountLbl.setBackgroundTintList(
            context.getColorStateList(
                Constants.getAccountsColor(
                    transaction.account
                )
            )
        )

        if (transaction.type == Constants.INCOME) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor))
        } else if (transaction.type == Constants.EXPENSE) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor))
        }

        holder.itemView.setOnLongClickListener {
            val deleteDialog = AlertDialog.Builder(context).create()
            deleteDialog.setTitle("Delete Transaction")
            deleteDialog.setMessage("Are you sure to delete this transaction?")
            deleteDialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "Yes"
            ) { _: DialogInterface?, _: Int ->
                (context as MainActivity).viewModel.deleteTransaction(
                    transaction
                )
            }
            deleteDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "No"
            ) { _: DialogInterface?, _: Int ->
                deleteDialog.dismiss()
            }
            deleteDialog.show()
            false
        }
    }

    override fun getItemCount(): Int {
        return transactions!!.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowTransactionBinding = RowTransactionBinding.bind(itemView)
    }
}