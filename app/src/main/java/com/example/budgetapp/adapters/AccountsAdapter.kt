package com.example.budgetapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.R
import com.example.budgetapp.databinding.RowAccountBinding
import com.example.budgetapp.models.Account

class AccountsAdapter(
    var context: Context, private var accountArrayList: ArrayList<Account>,
    private var accountsClickListener: AccountsClickListener
) :
    RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {
    interface AccountsClickListener {
        fun onAccountSelected(account: Account?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        return AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val account = accountArrayList[position]
        holder.binding.accountName.text = account.accountName
        holder.itemView.setOnClickListener {
            accountsClickListener.onAccountSelected(account)
        }
    }

    override fun getItemCount(): Int {
        return accountArrayList.size
    }

    inner class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowAccountBinding = RowAccountBinding.bind(itemView)
    }
}