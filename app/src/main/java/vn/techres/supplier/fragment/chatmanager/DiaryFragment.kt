package vn.techres.supplier.fragment.chatmanager

import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import vn.techres.supplier.databinding.FragmentPhonebookBinding
import vn.techres.supplier.model.datamodel.DataContact
import vn.techres.supplier.model.eventbussms.EvenBusListContact

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DiaryFragment : Fragment() {
    var binding: FragmentPhonebookBinding? = null
    var listCallLog = ArrayList<DataContact>()
    var listAll= ArrayList<DataContact>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPhonebookBinding.inflate(layoutInflater)
        onBody()
        return binding!!.root
    }

    private fun onBody() {
//        getCallDetails()
    }
    private fun getCallDetails() {
        listCallLog = ArrayList()
        val cursor: Cursor? = requireActivity().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, CallLog.Calls.DATE + " DESC"
        )
        val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        val name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
        cursor.getColumnIndex(CallLog.Calls.TYPE)
        cursor.getColumnIndex(CallLog.Calls.DATE)
        cursor.getColumnIndex(CallLog.Calls.DURATION)
        while (cursor.moveToNext()) {
            val phNumber = cursor.getString(number)
            val phName = cursor.getString(name)
            Timber.d("Phone : %s", phNumber)
            Timber.d("name : %s", phName)
            val contact: DataContact = if (phName != null) {
                DataContact(phNumber, phName)
            } else {
                DataContact(phNumber, "")
            }
            listCallLog.add(contact)
            var status = 0
            for (s in listAll) {
                if (s.phone.contains(phNumber)) {
                    status = 1
                }
            }
            if (status != 1) {
                listAll.add(contact)
            }
            //            String callDate = cursor.getString(date);
//            Date callDayTime = new Date(Long.valueOf(callDate));
//            String callDuration = cursor.getString(duration);
//            int dircode = Integer.parseInt(callType);
//            switch (dircode) {
//                case CallLog.Calls.OUTGOING_TYPE:
//                    dir = "OUTGOING";
//                    break;
//                case CallLog.Calls.INCOMING_TYPE:
//                    dir = "INCOMING";
//                    break;
//
//                case CallLog.Calls.MISSED_TYPE:
//                    dir = "MISSED";
//                    break;
//            }
//            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
//                    + dir + " \nCall Date:--- " + callDayTime
//                    + " \nCall duration in sec :--- " + callDuration);
//            stringBuffer.append("\n----------------------------------");
        }
        cursor.close()
        Handler().postDelayed({
            EventBus.getDefault().post(EvenBusListContact(listCallLog, 2))
        }, 3000)
    }
}