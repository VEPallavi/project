package com.utalli.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.utalli.BuildConfig
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern





class Utils {
    companion object {

        var progressDialog: ProgressDialog? = null


        fun convertTimeIntoDate(time: String): String {
            var date = Date(time.toLong())
            var sdf = SimpleDateFormat("dd MMM yyyy")

            return sdf.format(date)

        }


        fun showProgressDialog(mContext: Context) {
            if (progressDialog != null && progressDialog!!.isShowing)
                progressDialog!!.dismiss()


            progressDialog = ProgressDialog(mContext)
            progressDialog!!.setMessage("Please wait")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()


        }

        fun hideProgressDialog() {
            if (progressDialog != null && progressDialog!!.isShowing)
                progressDialog!!.dismiss()

        }


        fun isInternetAvailable(mContext: Context?): Boolean {
            if (mContext != null) {
                val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                @SuppressLint("MissingPermission") val netInfo = cm.activeNetworkInfo
                return netInfo != null && netInfo.isConnectedOrConnecting
            } else
                return false
        }


        fun hideSoftKeyboard(activity: Context) {
            try {
                val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow((activity as Activity).currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun isEmailValid(email: String): Boolean {
            var isValid = false

            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (matcher.matches()) {
                isValid = true
            }
            return isValid
        }


        fun showToast(mContext: Context, mesage: String) {
            Toast.makeText(mContext.applicationContext, mesage, Toast.LENGTH_LONG).show()

        }

        fun showLog(message: String) {
            if (BuildConfig.DEBUG)
                Log.e("Utalli", "Log message : " + message)

        }


        @Throws(ParseException::class)
        fun formatToYesterdayOrToday(datee: String): String {
            var dd: Date? = null

            /*  try {
                  val sdf = SimpleDateFormat("dd/MM/yyyy")
                  dd = sdf.parse(datee)
              } catch (ex: ParseException) {
                  // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
              }*/

            Log.e("TAG", "tvMsgDay date ===== " + datee)  // 03/06/2019
            Log.e("TAG", "tvMsgDay ddd  ===== " + dd)  //Mon Jun 03 00:00:00 GMT+05:30 2019



            val tempDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(datee)


            val ttDate = SimpleDateFormat("EEE hh:mma MMM d, yyyy", Locale.ENGLISH).format(tempDate)

            val dateTime = SimpleDateFormat("EEE hh:mma MMM d, yyyy").parse(ttDate)


            Log.e("TAG", "tvMsgDay dateTime  ===== " + dateTime)
            val calendar = Calendar.getInstance()
            calendar.time = dateTime
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance()
            yesterday.add(Calendar.DATE, -1)
            val timeFormatter = SimpleDateFormat("hh:mm a")

            return if (calendar.get(Calendar.YEAR) === today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) === today.get(Calendar.DAY_OF_YEAR)) {
                "Today " //+ timeFormatter.format(dateTime)
            } else if (calendar.get(Calendar.YEAR) === yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) === yesterday.get(Calendar.DAY_OF_YEAR)) {
                "Yesterday " //+ timeFormatter.format(dateTime)
            } else {
                datee
            }


        }

        fun getHumanReadableTimeFromUTCString(time: String): String? {
            val utcFormat = SimpleDateFormat("HH:mm:ss")
            val humanFormat = SimpleDateFormat("HH:mm")
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                return humanFormat.format(utcFormat.parse(time))
            } catch (e: ParseException) {
                e.printStackTrace()
                return null
            }
        }

        fun getTourDaysCount(startDate: String?, endDate:String?) : Int
        {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd")
            var calenderDate = Calendar.getInstance().time
            var tourStartDate = dateFormat.parse(startDate)
            var tourEndDate = dateFormat.parse(endDate)

            //in milliseconds
            var diff = tourEndDate.getTime() - calenderDate.getTime()
            //val diff = (tourEndDate.getTime() - calenderDate.getTime()) / 86400000
            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            //var tourDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return diffDays.toInt()
        }


        fun returmWindowHeight(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun returmWindowWidth(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }



    }
}