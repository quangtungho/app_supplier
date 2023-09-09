package vn.techres.supplier.fragment.chatmanager

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import vn.techres.supplier.adapter.chatadapter.ContactAdapter
import vn.techres.supplier.databinding.FragmentPhonebookBinding
import vn.techres.supplier.model.datamodel.DataContact
import vn.techres.supplier.model.eventbussms.EvenBusListContact
import vn.techres.supplier.model.eventbussms.EventBusPermission

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class PhonebookFragment : Fragment() {
    var binding: FragmentPhonebookBinding? = null
    private var position = 0
    private var listContacts = ArrayList<DataContact>()
    var listAll = ArrayList<DataContact>()
    var list = ArrayList<DataContact>()
    private val totalRecord = 30
    private var totalPage = 0
    private var record = 0
    var contactAdapter: ContactAdapter? = null
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPhonebookBinding.inflate(layoutInflater)
        onBody()
        return binding!!.root
    }
    @SuppressLint("NewApi")
    private fun onBody() {
        setAdapter()
    }
    private fun setAdapter() {
        binding!!.rclContact.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        contactAdapter = this.context?.let { ContactAdapter(it) }
        binding!!.rclContact.adapter =
            ConcatAdapter(contactAdapter)
        binding!!.rclContact.setHasFixedSize(true)
        contactAdapter!!.setDataSource(listContacts)

    }
    @SuppressLint("NotifyDataSetChanged", "Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun syncContacts() {
        position = 0
        listContacts = ArrayList()
        listAll = ArrayList()
        list = ArrayList()
        val contentResolver: ContentResolver = this.requireActivity().contentResolver
        var contactId: String
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val hasPhoneNumber = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                ).trim { it <= ' ' }
                    .toInt()
                if (hasPhoneNumber > 0) {
                    contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null
                    )
                    if (phones!!.moveToNext()) {
                        val phoneNumber =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val phoneName =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        val contact = DataContact(phoneNumber, phoneName)
                        list.add(contact)
                    }
                    phones.close()
                }
            }
        }
        cursor.close()
        Handler().postDelayed({
            EventBus.getDefault().post(EvenBusListContact(list, 1))
        }, 2000)
        when {
            list.size == 30 -> {
                listContacts.addAll(list)
                position = 30
            }
            list.size < 30 -> {
                for (i in list.indices) {
                    listContacts.add(list.get(position))
                    position += 1
                }
            }
            else -> {
                for (i in 0 until totalRecord) {
                    listContacts.add(list.get(position))
                    position += 1
                }
            }
        }
        if (list.size > totalRecord) {
            if (list.size / totalRecord == 0) {
                totalPage = list.size / totalRecord
                record = 0
            } else {
                totalPage = list.size / totalRecord + 1
                record = list.size % totalRecord
            }
        }
        listAll.addAll(list)

        Handler().postDelayed({
            binding!!.swipeRefreshLayout.isRefreshing = false
        }, 1000)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe
    fun onStatusPermission(event: EventBusPermission) {
        syncContacts()
    }
}