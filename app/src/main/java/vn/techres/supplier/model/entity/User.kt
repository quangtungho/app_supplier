package vn.techres.supplier.model.entity

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class User : Serializable {
    ////GETTER

    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("user_id")
    var user_id: Int = 0

    @JsonProperty("_id")
    var _id: String = ""

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("first_name")
    var first_name: String = ""

    @JsonProperty("last_name")
    var last_name: String = ""

    @JsonProperty("full_name")
    var full_name: String = ""

    @JsonProperty("email")
    var email: String = ""

    @JsonProperty("phone")
    var phone: String = ""

    @JsonProperty("supplier_employee_position")
    var supplier_employee_position: String = ""

    @JsonProperty("avatar")
    var avatar: String = ""

    @JsonProperty("birthday")
    var birthday: String = ""

    @JsonProperty("address_full_text")
    var address_full_text: String = ""

    @JsonProperty("street_name")
    var street_name: String = ""

    @JsonProperty("city_id")
    var city_id: Int = 0

    @JsonProperty("district_id")
    var district_id: Int = 0

    @JsonProperty("ward_id")
    var ward_id: Int = 0

    @JsonProperty("company_name")
    var company_name: String = ""

    @JsonProperty("company_tax_number")
    var company_tax_number: String = ""

    @JsonProperty("company_phone")
    var company_phone: String = ""

    @JsonProperty("company_address")
    var company_address: String = ""

    @JsonProperty("permissions")
    var permissions: List<String>? = null

    @JsonProperty("username")
    var username: String = ""


    @JsonProperty("password")
    var password: String = ""

    @JsonProperty("employee_role_id")
    var employeeRoleId: String = ""

    @JsonProperty("employee_role_name")
    var employeeRoleName: String = ""

    @JsonProperty("employee_role_description")
    var employeeRoleDescription: String = ""

    @JsonProperty("access_token")
    var access_token: String = ""

    @JsonProperty("refresh_token")
    var refresh_token: String = ""

    @JsonProperty("device_uid")
    var device_uid: String = ""

    @JsonProperty("node_access_token")
    var nodeAccessToken: String = ""

    @JsonProperty("node_refresh_token")
    var node_refresh_token: String = ""

    @JsonProperty("token_type")
    var token_type: String = ""

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String = ""

    @JsonProperty("branch_id")
    var branch_id: Int = 0

    @JsonProperty("branch_name")
    var branch_name: String = ""

    @JsonProperty("expires_in")
    var expires_in: String = ""

    @JsonProperty("gender")
    var gender: Int = 0

    @JsonProperty("is_enable_change_password")
    var is_enable_change_password: Int = 0


    @JsonProperty("address")
    var address: String = ""

    @JsonProperty("working_from")
    var working_from: String = ""

    @JsonProperty("token_firebase")
    var token_firebase: String = ""

    @JsonProperty("status")
    var status: Int = 0


    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("supplier_role_id")
    var supplier_role_id: Int = 0

    @JsonProperty("supplier_role_name")
    var supplier_role_name: String = ""


    @JsonProperty("is_checked")
    var is_checked = 0 // 1: checked, 0: uncheck : Only local

    val userDefault: User
        get() {
            val user = User()
            user.id = 0
            user.access_token = ""
            return user
        }
}
