package vn.techres.supplier.helper

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDate(day: Int, month: Int, year: Int): String {
        var strDay = day.toString() + ""
        var strMonth = month.toString() + ""
        if (day == 0) strDay = ""
        if (month == 0) strMonth = ""
        if (day < 10) strDay = "0$strDay"
        if (month < 10) strMonth = "0$strMonth"
        return if (month == 0) year.toString() + "" else if (day == 0) "$strMonth/$year" else "$strDay/$strMonth/$year"
    }

    //System.out.println(df.format(c.getTime()));
    val currentMonthFirstDate: String
        get() {
            val c = Calendar.getInstance()
            c[Calendar.DAY_OF_MONTH] = 1
            val df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            //System.out.println(df.format(c.getTime()));
            return df.format(c.time)
        }

    //        System.out.println("Today            : " + sdf.format(today));
//        System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
    val lastDayOfMonth: String
        get() {
            val today = Date()
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.MONTH, 1)
            calendar[Calendar.DAY_OF_MONTH] = 1
            calendar.add(Calendar.DATE, -1)
            val lastDayOfMonth = calendar.time
            val sdf: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            //        System.out.println("Today            : " + sdf.format(today));
//        System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
            return sdf.format(lastDayOfMonth)
        }

    fun formatDateTime(minute: Int, hour: Int, day: Int, month: Int, year: Int): String {
        var strDay = day.toString() + ""
        var strMonth = month.toString() + ""
        var strMinute = minute.toString() + ""
        var strHour = hour.toString() + ""
        if (day == 0) strDay = ""
        if (month == 0) strMonth = ""
        if (day < 10) strDay = "0$strDay"
        if (month < 10) strMonth = "0$strMonth"
        if (minute < 10) strMinute = "0$strMinute"
        if (hour < 10) strHour = "0$strHour"
        return "$strDay/$strMonth/$year $strHour:$strMinute"
    }

    fun formatDateTime(date: String, time: String): String {
        return "$date $time"
    }

    fun formatYear(year: Int): String {
        return "Năm $year"
    }

    //    public static int getQuarter(int month)
    //    {
    //        return (month-1)/3+1;
    //    }
    fun getQuarter(month: Int): String {
        return if (month < 4) {
            1.toString() + ""
        } else if (month < 7) {
            2.toString() + ""
        } else if (month < 10) 3.toString() + "" else 4.toString() + ""
    }

    val todayDateString: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(today())
        }

    private fun today(): Date {
        val cal = Calendar.getInstance()
        return cal.time
    }

    val yesterdayDateString: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(yesterday())
        }

    private fun yesterday(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }

    fun getDateInWeek(input: String?): String {
        try {
            val date1 = SimpleDateFormat("dd/MM/yyyy").parse(input)
            var simpleDateformat = SimpleDateFormat("E") // the day of the week abbreviated
            // System.out.println(simpleDateformat.format(date1));
            simpleDateformat =
                SimpleDateFormat("EEEE") // the day of the week spelled out completely
            //            System.out.println(simpleDateformat.format(date1));
            return simpleDateformat.format(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
}