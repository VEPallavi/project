package com.utalli.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.transition.TransitionManager
import com.utalli.R
import kotlinx.android.synthetic.main.my_profile_activity.*
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.client.Firebase
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.utalli.helpers.AppPreference
import com.utalli.helpers.RealPathUtil
import com.utalli.helpers.Utils
import com.utalli.models.UpdateProfileRequestModel
import com.utalli.models.UserModel
import com.utalli.viewModels.MyProfileViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyProfileActivity : AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    var outputFileUri: Uri? = null
    val CAMERA_REQUEST = 101
    val GALLERY_REQUEST = 102
    var imageFilePath: String = ""
    var user : UserModel ?= null
    var id: Int = 0
    var myProfileViewModel : MyProfileViewModel?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_activity)


        Firebase.setAndroidContext(applicationContext)
        FirebaseApp.initializeApp(applicationContext)

        initViews()

    }

    private fun initViews() {

        user = AppPreference.getInstance(this).getUserData() as UserModel
        id = AppPreference.getInstance(this).getId() as Int

        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel::class.java)

        iv_profile_image.setImageResource(R.mipmap.ic_profile_placeholder)

        iv_image_picker.setOnClickListener(this)
        tv_settings.setOnClickListener(this)
        tv_payment.setOnClickListener(this)
        iv_backArrow.setOnClickListener(this)
        tv_logout.setOnClickListener(this)
        iv_editProfile_icon.setOnClickListener(this)
        tv_txt_save.setOnClickListener(this)


        if(user != null){
            et_user_name.setText(user!!.u_name)
            et_user_number.setText(user!!.mobile_no)
            et_email_id.setText(user!!.u_email)
            et_address.setText(user!!.u_address)
            et_emergency_contact_number.setText(user!!.emry_contact)
            tv_payment.setText(user!!.payment)
            Glide.with(this)
                .load(user!!.profile_img)
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                .into(iv_profile_image)


            Log.e("TAG","iddddddd profile 1======  " +id)

        }
        else{
            logout()
        }


    }



    override fun onMenuItemClick(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.item_camera -> {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    //Create a file to store the image
                    var photoFile: File? = null;
                    try {
                        photoFile = createImageFile();
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                    }
                    if (photoFile != null) {
                        outputFileUri = FileProvider.getUriForFile(this,getPackageName() + ".provider", photoFile)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    }
                }

                return true
            }
            R.id.item_gallery -> {

                try {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryIntent.type = "image/*"
                    startActivityForResult(galleryIntent, GALLERY_REQUEST)
                } catch (e: Exception) {
                    Utils.showToast(this, "No Gallery Found..")
                }

                return true
            }
        }
        return false
    }



    fun openImagePickerMenu() {
        var popUpMenu = PopupMenu(this, iv_image_picker)
        try {
            val fields = popUpMenu.javaClass.getDeclaredFields()
            for (field in fields) {
                if ("mPopup" == field.getName()) {
                    field.setAccessible(true)
                    val menuPopupHelper = field.get(popUpMenu)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.getName())
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        popUpMenu.setOnMenuItemClickListener(this)
        popUpMenu.inflate(R.menu.image_picker_menu)
        popUpMenu.gravity = Gravity.START
        popUpMenu.show()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_image_picker -> {

                if (checkPermission()){
                    openImagePickerMenu()
                }
                else {
                    requestPermission()
                }
            }

            R.id.iv_backArrow -> {
                finish()
            }

            R.id.tv_logout -> {
                 logout()
            }


            R.id.iv_editProfile_icon -> {
                if (tv_txt_save.visibility == View.VISIBLE) {

                    changeViewsEditProperty(false)
                    TransitionManager.beginDelayedTransition(cl_edit)
                    tv_txt_save.visibility = View.GONE
                } else {
                    changeViewsEditProperty(true)
                    TransitionManager.beginDelayedTransition(cl_edit)
                    tv_txt_save.visibility = View.VISIBLE
                }

            }

            R.id.tv_txt_save ->{
                uploadProileData()
            }



            R.id.tv_payment -> {
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            }

            R.id.tv_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }





    fun requestPermission() {

        var permissions = arrayOf(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 123);
    }


    fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                return true
            } else {
                return false
            }

        } else {
            return true
        }


    }


    fun changeViewsEditProperty(editable: Boolean) {

        if (editable) {
            et_user_name.isEnabled = true
            et_user_number.isEnabled = true
            et_email_id.isEnabled = true
            et_address.isEnabled = true
            et_emergency_contact_number.isEnabled = true

        } else {
            et_user_name.isEnabled = false
            et_user_number.isEnabled = false
            et_email_id.isEnabled = false
            et_address.isEnabled = false
            et_emergency_contact_number.isEnabled = false
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) run {

            try {
                //val selectedFilePath = RealPathUtil.getPath(this@ProfileActivity, uri)

                val compressedImage = compressImage(imageFilePath)


              /*  Glide.with(this)
                    .load(compressedImage)
                    .apply(RequestOptions().placeholder(R.drawable.dummy_icon).error(R.drawable.dummy_icon))
                    .into(iv_profile_image)*/

                uploadImage(compressedImage)


            } catch (e: IOException) {
                e.printStackTrace()
            }


        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) run {
            val uri = data!!.getData()
            try {
                val selectedFilePath = RealPathUtil.getPath(this@MyProfileActivity, uri)

                val compressedImage = compressImage(selectedFilePath)

           /*     Glide.with(this)
                    .load(compressedImage)
                    .apply(RequestOptions().placeholder(R.drawable.dummy_icon).error(R.drawable.dummy_icon))
                    .into(iv_profile_image)*/

                 uploadImage(compressedImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



     fun uploadImage(imageUri: String) {

         pb_image.visibility = View.VISIBLE
         var file = File(imageUri)
         var requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
         var img = MultipartBody.Part.createFormData("image", file.name,requestFile)

         myProfileViewModel!!.updateProfilePic(this, img).observe(this, androidx.lifecycle.Observer {

             if(it != null && it.has("status") && it.get("status").asString.equals("1")){


                 pb_image.visibility = View.GONE

                 if(it.has("data") && it.get("data") is JsonObject){

                     var dataObj = it.getAsJsonObject("data")

                     if(dataObj.has("profile_img")){

                         user!!.profile_img = dataObj.get("profile_img").asString

                         AppPreference.getInstance(this).setUserData(Gson().toJson(user))

                         Utils.showToast(this, it.get("message").asString)

                         Glide.with(this)
                             .load(user!!.profile_img)
                             .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                             .into(iv_profile_image)

                         // register the profile to the firebase database
                         registerUser(AppPreference.getInstance(this).getuserIdFirebase(), user!!.profile_img)

                     }

                 }
             } else {

                 pb_image.visibility = View.GONE
             }

         })


    }



    fun uploadProileData() {

        myProfileViewModel!!.updateProfile(this, UpdateProfileRequestModel(
            et_user_name.text.toString(),
            et_email_id.text.toString(),
            et_user_number.text.toString(),
            user!!.dob,
            user!!.gender,
            "2345",
            //tv_payment.text.toString(),
            et_emergency_contact_number.text.toString(),
            et_address.text.toString(),
            id

        )).observe(this, androidx.lifecycle.Observer {


            if(it != null && it.has("status") && it.get("status").asString.equals("1")){

                Utils.showToast(this, it.get("message").asString)

                if(it.has("data") && it.get("data") is JsonObject){

                    AppPreference.getInstance(this).setUserData(it.get("data").toString())

                    tv_txt_save.visibility = View.GONE
                    et_user_name.isEnabled = false
                    et_user_number.isEnabled = false
                    et_email_id.isEnabled = false
                    et_address.isEnabled = false
                    et_emergency_contact_number.isEnabled = false

                }

            }

        })

    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (grantResults.size > 0) {

            var CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            var readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            var writeExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

            if (CameraPermission && readExternalStorage && writeExternalStorage) {
                openImagePickerMenu()
            } else {
                Utils.showToast(this, getString(R.string.msg_incomplete_permission))
            }
        }


    }


    fun compressImage(imageUri: String): String {

        var filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        var options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 816.0f
        val maxWidth = 612.0f

        var imgRatio = (actualWidth / actualHeight).toFloat()


        var maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90F)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180F)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270F)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap!!.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename

    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        var height = options.outHeight;
        var width = options.outWidth;
        var inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            var heightRatio = Math.round((height.toFloat() / reqHeight.toFloat()))
            var widthRatio = Math.round((width.toFloat() / reqWidth.toFloat()))

            if (heightRatio < widthRatio)
                inSampleSize = heightRatio
            else
                inSampleSize = widthRatio


        }
        var totalPixels = width * height;
        var totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    fun getRealPathFromURI(contentURI: String): String {
        var contentUri = Uri.parse(contentURI);
        var filePathColumn = arrayOf(MediaStore.Images.Media.DATA)


        var cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            var index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    fun getFilename(): String {
        var file = File(Environment.getExternalStorageDirectory().getPath(), resources.getString(R.string.app_name) + "/Images")
        if (!file.exists()) {
            file.mkdirs()
        }
        var uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg")
        return uriSting
    }


    private fun createImageFile(): File {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date());
        var imageFileName = "IMG_" + timeStamp + "_";
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      //  var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        var image = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */);
        imageFilePath = image.getAbsolutePath();
        return image;
    }



    private fun registerUser(fireBaseId: String, urlProfileImg: String) {
      //  var fireBaseId = "userId_"+fireBaseId
        val refUsers = Firebase("https://utalii-fda70.firebaseio.com/users/" + fireBaseId + "/credentials/profilePicLink")
        refUsers.setValue(urlProfileImg)
    }




     fun logout() {
        AppPreference.getInstance(this).setUserData("")
         AppPreference.getInstance(this).setId(0)
         AppPreference.getInstance(this).setAuthToken("")
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}
