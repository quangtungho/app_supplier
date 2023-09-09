package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemContactBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.datamodel.DataContact

class ContactAdapter(var context: Context) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataContact>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    fun setDataSource(dataSource: ArrayList<DataContact>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemContactBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataSource[position]
        with(holder) {
            val phone: String = data.phone.replaceFirst("0", "+84")
            if (data.name != "") {
                binding.txtName.visibility = View.VISIBLE
                binding.txtName.text = data.name
                var a = -1
                var cutName = ""
                a = data.name.indexOf(" ")
                cutName = if (a != -1) {
                    data.name[0] + "" + data.name[a + 1]
                } else {
                    data.name[0] + ""
                }
                binding.avatar.text = cutName
            } else {
                binding.txtName.visibility = View.GONE
                binding.avatar.text = phone.substring(phone.length - 2)
            }
            binding.txtPhone.text = phone
            itemView.setOnClickListener {
                binding.imgCheck.setImageDrawable(
                    AppUtils.getDrawablebyResource(
                        itemView.context,
                        R.drawable.ic_correct_check
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}