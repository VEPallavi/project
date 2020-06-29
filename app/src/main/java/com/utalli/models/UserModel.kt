package com.utalli.models

class UserModel (
    var id: Int,
    var u_name: String,
    var mobile_no: String,
    var verification_code: String,
    var is_verified: String,
    var u_email : String,
    var u_address : String="",
    var dob: String,
    var gender: String,
    var profile_img : String = "",
    var payment : String="",
    var emry_contact : String=""
)