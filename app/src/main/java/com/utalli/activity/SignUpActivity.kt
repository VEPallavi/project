package com.utalli.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.firebase.client.Firebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.utalli.R
import com.utalli.fragment.VerifyOTPDialogFragment
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.models.SignupRequestModel
import com.utalli.viewModels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.internal.Util
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener, VerifyOTPDialogFragment.OnButtonClick {

    var userName : String =""
    var userIdd : String = ""
    var emaiil : String =""




    override fun onDismiss() {
        bottomSheetDialogFragment = null
    }

    override fun onResendClick() {
        bottomSheetDialogFragment!!.dismiss()
        signupUser()
    }

    override fun onSubmitClick() {
        Utils.hideSoftKeyboard(this)
        bottomSheetDialogFragment!!.dismiss()



        signupViewModel!!.verifyOTP(this, SignupRequestModel(
            et_name.text.toString(),
            et_email_id.text.toString(),
            et_mobileNumber.text.toString(),
            et_newPassword.text.toString(),
            dob,
            genderValue.toString(),
            otp,
            device_token
        )
        ).observe(this, androidx.lifecycle.Observer {

         //   Utils.showLog(it.toString()!!)


            if (it.has("status") && it.get("status").asString.equals("1")) {

                Utils.showToast(this, getString(R.string.msg_user_registered))


                if(it.has("data")){

                    var dataObj = it.getAsJsonObject("data")
                    if(dataObj.has("id") && dataObj.has("u_name") && dataObj.has("u_email")){

                        userName = dataObj.get("u_name").asString
                        emaiil = dataObj.get("u_email").asString
                        userIdd = dataObj.get("id").asInt.toString()
                      //  userIdd = "userId_" +userIdd
                        Log.e("TAG", "")
                        addfirebaseUser(""+userIdd +"@utali.com")
                    }
                }




//                Handler().postDelayed(Runnable {
//
//                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//
//                }, 1000)

            } else {
                Utils.showToast(this, getString(R.string.msg_common_error))
            }
        })

    }



    var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    var genderValue : String? = null
    var signupViewModel:SignUpViewModel?= null
    var otp: String = ""
    var dob : String = ""

    var bottomSheetDialogFragment: VerifyOTPDialogFragment? = null

    //var device_token = "sdkjfdsjflksdjfklsdjfkljdsfddddddddddddddddssssssssssss"
    var device_token = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.arrow_back_black)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }


        Firebase.setAndroidContext(applicationContext)
        FirebaseApp.initializeApp(applicationContext)

        initView()
    }

    private fun initView() {

        signupViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        btn_signUp.setOnClickListener(this)
        tv_sign_in.setOnClickListener(this)
        et_dateOfBirth.setOnClickListener(this)

        cl_first_male.setOnClickListener(this)
        cl_second_female.setOnClickListener(this)
        cl_third_other.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.btn_signUp ->{
                signupUser()
            }
            R.id.tv_sign_in ->{
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.et_dateOfBirth -> {
                val datePickerDialog = DatePickerDialog(this,R.style.DialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    et_dateOfBirth.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                  //  et_dateOfBirth.setText("" + year + "-" + (monthOfYear+1) + "-" + dayOfMonth)

                    dob = ("" + year + "-" + (monthOfYear+1) + "-" + dayOfMonth)



                }, year, month, day)

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000)

                datePickerDialog.show()
            }
            R.id.cl_first_male ->{
                genderValue = "MALE"
                cl_first_male.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_bottom_blue))
                //cl_first_male.setBackgroundColor(resources.getColor(R.color.color_blue))
                cl_second_female.setBackgroundColor(resources.getColor(R.color.colorWhite))
                cl_third_other.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_top_white))

                tv_male.setTextColor(Color.parseColor("#ffffff"))
                tv_female.setTextColor(Color.parseColor("#000000"))
                tv_other.setTextColor(Color.parseColor("#000000"))

            }
            R.id.cl_second_female -> {
                genderValue = "FEMALE"
                cl_second_female.setBackgroundColor(resources.getColor(R.color.color_blue))
                cl_first_male.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_bottom_white))
                cl_third_other.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_top_white))

                tv_male.setTextColor(Color.parseColor("#000000"))
                tv_female.setTextColor(Color.parseColor("#ffffff"))
                tv_other.setTextColor(Color.parseColor("#000000"))

            }
            R.id.cl_third_other ->{
                genderValue = "OTHER"
                cl_third_other.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_top_blue))
                cl_first_male.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_rect_bottom_white))
                cl_second_female.setBackgroundColor(resources.getColor(R.color.colorWhite))

                tv_male.setTextColor(Color.parseColor("#000000"))
                tv_female.setTextColor(Color.parseColor("#000000"))
                tv_other.setTextColor(Color.parseColor("#ffffff"))
            }

        }

    }

    private fun signupUser() {

        //device_token = ""

        Utils.hideSoftKeyboard(this)

        if(checkValidations()){

            Log.e("TAG","genderValue send to server part signUp === "+genderValue.toString())

            signupViewModel!!.signupUser(this, SignupRequestModel(
                et_name.text.toString(),
                et_email_id.text.toString(),
                et_mobileNumber.text.toString(),
                et_newPassword.text.toString(),
                dob,
                genderValue.toString(),
                "",
                device_token
            )
            ).observe(this, androidx.lifecycle.Observer {

             //   Utils.showLog(it.toString())


                if(it != null && it.has("status") && it.get("status").asString.equals("1")){

                    if (it.has("otp")){
                       // Utils.showToast(this, getString(R.string.msg_otp_sent))
                        Utils.showToast(this, it.get("message").asString)
                        otp = it.get("otp").asString

                        //if (bottomSheetDialogFragment == null) {
                            bottomSheetDialogFragment = VerifyOTPDialogFragment.newInstance(otp, this)
                            bottomSheetDialogFragment!!.show(supportFragmentManager, "VerifyOTPDialogFragment")
                     //   }

                    } else {
                        Utils.showToast(this, getString(R.string.msg_common_error))
                    }

                }
                else {
                      if(it != null && it.has("message")){
                          Utils.showToast(this,it.get("message").asString)
                          Log.e("TAG","message status 0 SignUp  === "+it.get("message").asString)
                      }

                }
            })

        }

    }

    private fun checkValidations(): Boolean {


        Log.e("TAG","gender value on validation part signUp ====   "+genderValue)

        if (!Utils.isInternetAvailable(this)) {
            Utils.showToast(this, resources.getString(R.string.msg_no_internet))
            return false
        }
        else if (et_name.text!!.length < 2) {
            Utils.showToast(this, resources.getString(R.string.msg_invalid_name))
            return false
        }
        else if (!Utils.isEmailValid(et_email_id.text.toString())) {
            Utils.showToast(this, resources.getString(R.string.msg_invalid_email))
            return false
        }
        else if (et_mobileNumber.text!!.length == 0) {
            Utils.showToast(this, resources.getString(R.string.msg_empty_mobile_no))
            return false
        }

        else if(et_mobileNumber.text!!.length < 6){
            Utils.showToast(this, resources.getString(R.string.msg_invalid_mobile_no))
            return false
        }

        else if (et_newPassword.text!!.length == 0) {
            Utils.showToast(this, resources.getString(R.string.msg_empty_pass))
            return false
        }
        else if (et_newPassword.text!!.length < 6) {
            Utils.showToast(this, resources.getString(R.string.msg_invalid_pass))
            return false
        }
        else if(genderValue == null){
            Utils.showToast(this, resources.getString(R.string.msg_empty_gender))
            return false
        }
        else if(dob.equals("")){
            Utils.showToast(this, resources.getString(R.string.msg_empty_dob))
            return false
        }
        else if(!(et_reEnterNew_Password.text.toString()).equals(et_newPassword.text.toString())){
            Utils.showToast(this, resources.getString(R.string.msg_not_same_pass))
            return false
        }

        return true
    }



  /*  private fun registerUser(fireBaseId: String, name: String, email: String, urlProfileImg: String) {
        val refUsers = Firebase("https://beyond-6fdea.firebaseio.com/users/" + fireBaseId + "/credentials/")
        val map = HashMap<String, String>()
        map["email"] = email
        map["isOnline"] = "false"
        map["isTyping"] = "false"
        map["lastSeen"] = "0"
        map["name"] = name
        map["profilePicLink"] = urlProfileImg
        refUsers.setValue(map)
    }*/

    fun addfirebaseUser(email : String){
        Firebase.setAndroidContext(this)
        val password = "12345678"
        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object: OnCompleteListener<AuthResult> {
                override fun onComplete(@NonNull task: Task<AuthResult>) {
                    if (!task.isSuccessful()){
                        Log.e("Signup Error", "onCancelled", task.getException())
                        Log.e("TAG","SignUp firebase error == " + task.getException())
                    }else{
                        val user = mAuth.getCurrentUser()
                        val uid = user!!.getUid()
                        Log.e("Fire base User id", uid)
                        var pref= AppPreference.getInstance(this@SignUpActivity)
                        pref.setuserIdFirebase(uid)
                       // registerUser(userIdd, userName, emaiil)
                        registerUser(uid, userName, email)

                    }
                }
            })

    }


    private fun registerUser(fireBaseId: String, name: String, email: String) {

        val refUsers = Firebase("https://utalii-fda70.firebaseio.com/users/" + fireBaseId + "/credentials/")
        val map = HashMap<String, String>()
        map["email"] = email
        map["isOnline"] = "false"
        map["isTyping"] = "false"
        map["lastSeen"] = "0"
        map["name"] = name
        refUsers.setValue(map)

        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }


}