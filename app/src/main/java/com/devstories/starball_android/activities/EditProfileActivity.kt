package com.devstories.starball_android.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.Actions.MemberAction
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.CharmAdapter
import com.devstories.starball_android.adapter.LanguageAdapter
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class EditProfileActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var ProfileAdapter: ProfileAdapter

    private val SELECT_LANGUAGE_REQUST_CODE = 1004
    private val SELECT_PICTURE_REQUEST = 1002
    private val SELECT_NATION = 1005
    private val SELECT_TRAVEL = 1006

    private var pictures = arrayListOf<JSONObject>()

    private val CHARM_POINT = 111
    private val WE_MEET = 112
    private val WANT_MEET = 113


    private val HEIGHT_SELECT = 101
    private val REGION_SELECT = 102
    private val POLICY_SELECT = 103
    private val BABY_SELECT = 104
    private val ANIMAL_SELECT = 105
    private val SMOKE_SELECT = 106
    private val DRINK_SELECT = 107
    private val HEALTH_SELECT = 108
    private val SPORTS_SELECT = 109
    private val WORK_SELECT = 110

    var option_list = ArrayList<String>()

    var adapterData = ArrayList<String>()
    var adapterData2 = ArrayList<String>()
    var adapterData3 = ArrayList<String>()
    var adapterData4 = ArrayList<String>()


    lateinit var languageAdapter: LanguageAdapter
    lateinit var charmAdapter: CharmAdapter
    lateinit var meetAdapter: CharmAdapter
    lateinit var wantmeetAdapter: CharmAdapter


    var year: Int = 1
    var month: Int = 1
    var day: Int = 1


    var step = -1

    //회원정보
    var email = ""
    var height = -1
    var intro = ""
    var nation = ""
    var region = ""
    var policy = ""
    var baby = ""
    var animal = ""
    var smoke = ""
    var drink = ""
    var health = ""
    var sport = ""
    var work = ""

    var vvip_yn = ""
    var know_yn = ""
    var save_yn = ""
    var limit_yn = ""
    var cross_yn = ""
    var ghost_yn = ""
    var savejoin_yn = ""
    var phone_yn = ""
    var email_yn = ""
    var facebook_yn = ""
    var insta_yn = ""
    var travel = ""
    var travel_cal = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        this.context = this
        progressDialog = ProgressDialog(context)


        ProfileAdapter = ProfileAdapter(context, R.layout.item_profile_img, 20)
        profileGV.adapter = ProfileAdapter


        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageGV.adapter = languageAdapter
        languageGV.setOnItemClickListener { parent, view, position, id ->
            adapterData.removeAt(position)

            languageAdapter.notifyDataSetChanged()
        }

        charmAdapter = CharmAdapter(context, R.layout.item_charm_point, adapterData2)
        charmpointGV.adapter = charmAdapter


        meetAdapter = CharmAdapter(context, R.layout.item_charm_point, adapterData3)
        meeetpointGV.adapter = meetAdapter

        wantmeetAdapter = CharmAdapter(context, R.layout.item_charm_point, adapterData4)
        wantmeetGV.adapter = wantmeetAdapter


        val joinLanguage = PrefUtils.getStringPreference(context, "join_language", "")
        if (joinLanguage.isNotEmpty()) {
            val splited = joinLanguage.split(",")
            for (language in splited) {
                adapterData.add(language.trim())
            }

            languageAdapter.notifyDataSetChanged()
        }

        click()
    }

    fun click() {
        nextTV.setOnClickListener {
            edit_info()
        }
        languageLL.setOnClickListener {
            val intent = Intent(context, DlgSelectLanguageActivity::class.java)
            startActivityForResult(intent, SELECT_LANGUAGE_REQUST_CODE)
        }

        heightLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            startActivityForResult(intent, HEIGHT_SELECT)
        }
        regionLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 2
            intent.putExtra("step", step)
            startActivityForResult(intent, REGION_SELECT)

        }
        policyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 3
            intent.putExtra("step", step)
            startActivityForResult(intent, POLICY_SELECT)
        }
        babyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 4
            intent.putExtra("step", step)
            startActivityForResult(intent, BABY_SELECT)
        }
        animalLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 5
            intent.putExtra("step", step)
            startActivityForResult(intent, ANIMAL_SELECT)
        }
        smokeLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 6
            intent.putExtra("step", step)
            startActivityForResult(intent, SMOKE_SELECT)
        }
        drinkLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 7
            intent.putExtra("step", step)
            startActivityForResult(intent, DRINK_SELECT)
        }
        healthLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 8
            intent.putExtra("step", step)
            startActivityForResult(intent, HEALTH_SELECT)
        }
        sportLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 9
            intent.putExtra("step", step)
            startActivityForResult(intent, SPORTS_SELECT)
        }
        workLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 10
            intent.putExtra("step", step)
            startActivityForResult(intent, WORK_SELECT)
        }

        backIV.setOnClickListener {
            finish()
        }

        charmRL.setOnClickListener {
            charmAdapter.clear()
            val intent = Intent(context, CharmPointActivity::class.java)
            startActivityForResult(intent, CHARM_POINT)

        }

        meetRL.setOnClickListener {
            meetAdapter.clear()
            val intent = Intent(context, WeMeetActivity::class.java)
            startActivityForResult(intent, WE_MEET)
        }
        peopleRL.setOnClickListener {
            wantmeetAdapter.clear()
            val intent = Intent(context, CharmPointActivity::class.java)
            intent.putExtra("type", 2)
            startActivityForResult(intent, WANT_MEET)
        }


        phoneLL.setOnClickListener {
            val intent = Intent(context, PhoneCertiActivity::class.java)
            startActivity(intent)
        }
        saveLL.setOnClickListener {
            val intent = Intent(context, SaveJoinActivity::class.java)
            startActivity(intent)
        }
        emailLL.setOnClickListener {
            val intent = Intent(context, EmailConnectActivity::class.java)
            startActivity(intent)
        }
        ghostIV.setOnClickListener {
            setmenu()
            ghost_yn = "Y"
            ghostIV.setImageResource(R.mipmap.comm_check_on)
        }
        nomalIV.setOnClickListener {
            setmenu()
            ghost_yn = "N"
            nomalIV.setImageResource(R.mipmap.comm_check_on)
        }
        crossLL.setOnClickListener {
            if (cross_yn == "Y") {
                cross_yn = "N"
                crossIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                cross_yn = "Y"
                crossIV.setImageResource(R.mipmap.comm_check_on)
            }

        }
        limitLL.setOnClickListener {
            if (limit_yn == "Y") {
                limit_yn = "N"
                limitIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                limit_yn = "Y"
                limitIV.setImageResource(R.mipmap.comm_check_on)
            }
        }
        saveviewLL.setOnClickListener {
            if (save_yn == "Y") {
                save_yn = "N"
                saveviewIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                save_yn = "Y"
                saveviewIV.setImageResource(R.mipmap.comm_check_on)
            }
        }
        vvipLL.setOnClickListener {
            if (vvip_yn == "Y") {
                vvip_yn = "N"
                vvipIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                vvip_yn = "Y"
                vvipIV.setImageResource(R.mipmap.comm_check_on)
            }
        }
        knowLL.setOnClickListener {
            if (know_yn == "Y") {
                know_yn = "N"
                knowIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                know_yn = "Y"
                knowIV.setImageResource(R.mipmap.comm_check_on)
            }
        }

        nationRL.setOnClickListener {
            val intent = Intent(context, DlgSelectNationActivity::class.java)
            startActivityForResult(intent, SELECT_NATION)
        }
        travelRL.setOnClickListener {
            val intent = Intent(context, DlgSelectNationActivity::class.java)
            startActivityForResult(intent, SELECT_TRAVEL)
        }

        calRL.setOnClickListener {
            datedlg()
        }

        get_info()
    }

    fun datedlg() {
        DatePickerDialog(context, dateSetListener, year, month, day).show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        dateTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun setmenu() {
        ghostIV.setImageResource(R.mipmap.comm_check_off)
        nomalIV.setImageResource(R.mipmap.comm_check_off)
    }

    fun get_info() {

        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.get_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {

                        val member = response.getJSONObject("member")
                        email = Utils.getString(member, "email")


                        height = Utils.getInt(member, "height")
                        intro = Utils.getString(member, "intro")
                        nation = Utils.getString(member, "nation")
                        region = Utils.getString(member, "region")
                        policy = Utils.getString(member, "policy")
                        baby = Utils.getString(member, "baby")
                        drink = Utils.getString(member, "drink")
                        smoke = Utils.getString(member, "smoke")
                        health = Utils.getString(member, "health")
                        sport = Utils.getString(member, "sport")
                        animal = Utils.getString(member, "animal")
                        work = Utils.getString(member, "work")


                        vvip_yn = Utils.getString(member, "vvip_yn")
                        know_yn = Utils.getString(member, "know_yn")
                        save_yn = Utils.getString(member, "save_yn")
                        cross_yn = Utils.getString(member, "cross_yn")
                        limit_yn = Utils.getString(member, "limit_yn")
                        ghost_yn = Utils.getString(member, "ghost_yn")
                        savejoin_yn = Utils.getString(member, "savejoin_yn")
                        phone_yn = Utils.getString(member, "phone_yn")
                        email_yn = Utils.getString(member, "email_yn")
                        facebook_yn = Utils.getString(member, "facebook_yn")
                        insta_yn = Utils.getString(member, "insta_yn")
                        travel = Utils.getString(member, "travel")
                        travel_cal = Utils.getString(member, "travel_cal")

                        if (nation != ""){
                            nationTV.text = nation
                        }


                        if (travel != "") {
                            travelTV.text = travel
                        }

                        if (travel_cal != "") {
                            dateTV.text = travel_cal
                        }



                        introET.setText(intro)


                        if (ghost_yn == "Y") {
                            setmenu()
                            ghostIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            setmenu()
                            nomalIV.setImageResource(R.mipmap.comm_check_on)
                        }


                        if (cross_yn == "Y") {
                            crossIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            crossIV.setImageResource(R.mipmap.comm_check_off)
                        }

                        if (limit_yn == "Y") {
                            limitIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            limitIV.setImageResource(R.mipmap.comm_check_off)
                        }

                        if (save_yn == "Y") {
                            saveviewIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            saveviewIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (vvip_yn == "Y") {
                            vvipIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            vvipIV.setImageResource(R.mipmap.comm_check_off)
                        }

                        if (know_yn == "Y") {
                            knowIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            knowIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (height!= -1){
                            heightTV.text = height.toString()
                            heightIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            heightIV.setImageResource(R.mipmap.plus_op)
                        }

                        if (region!=""){
                            regionTV.text = region
                            regionIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            regionIV.setImageResource(R.mipmap.plus_op)
                        }
                        if (policy!=""){
                            policyTV.text = policy
                            policyIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            policyIV.setImageResource(R.mipmap.plus_op)
                        }

                        if (baby!=""){
                            babyTV.text = baby
                            babyIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            babyIV.setImageResource(R.mipmap.plus_op)
                        }

                        if (animal!=""){
                            animalTV.text = animal
                            animalIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            animalIV.setImageResource(R.mipmap.plus_op)
                        }

                        if (smoke!=""){
                            smokeTV.text = smoke
                            smokeIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            smokeIV.setImageResource(R.mipmap.plus_op)
                        }
                        if (drink!=""){
                            drinkTV.text = drink
                            drinkIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            drinkIV.setImageResource(R.mipmap.plus_op)
                        }
                        if (health!=""){
                            healthTV.text = health
                            healthIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            healthIV.setImageResource(R.mipmap.plus_op)
                        }

                        if (sport !=""){
                            sportTV.text = sport
                            sportIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            sportIV.setImageResource(R.mipmap.plus_op)
                        }
                        if (work !=""){
                            workTV.text = work
                            workIV.setImageResource(R.mipmap.setting_drop_right)
                        }else{
                            workIV.setImageResource(R.mipmap.plus_op)
                        }







                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {

                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun edit_info() {
        var intro = Utils.getString(introET)
        var join_language = PrefUtils.setPreference(context, "join_language", adapterData.joinToString())
        var nation = Utils.getString(nationTV)
        var travel = Utils.getString(travelTV)
        var travel_cal = Utils.getString(dateTV)


        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("intro", intro)
        params.put("language", join_language)
        params.put("nation", nation)
        params.put("travel", travel)
        params.put("travel_cal", travel_cal)
        params.put("vvip_yn", vvip_yn)
        params.put("know_yn", know_yn)
        params.put("save_yn", save_yn)
        params.put("limit_yn", limit_yn)
        params.put("cross_yn", cross_yn)
        params.put("ghost_yn", ghost_yn)
        params.put("savejoin_yn", savejoin_yn)
        params.put("phone_yn", phone_yn)
        params.put("email_yn", email_yn)
        params.put("facebook_yn", facebook_yn)
        params.put("insta_yn", insta_yn)



        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {

                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CHARM_POINT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        var champoints = data.getSerializableExtra("charmPoint") as ArrayList<String>
                        if (champoints.size > 0) {
                            for (i in 0..(champoints.size - 1)) {
                                val champoint = champoints[i]
                                //배열로 입력저장은 [] 이걸 넣어준다\
                                adapterData2.add(champoint)
                            }
                        }
                        charmAdapter.notifyDataSetChanged()
                    }
                }
            }
            WE_MEET -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        var meetpoints = data.getSerializableExtra("meetPoint") as ArrayList<String>
                        if (meetpoints.size > 0) {
                            for (i in 0..(meetpoints.size - 1)) {
                                val meetpoint = meetpoints[i]
                                //배열로 입력저장은 [] 이걸 넣어준다\
                                adapterData3.add(meetpoint)
                            }
                        }
                        meetAdapter.notifyDataSetChanged()
                    }
                }
            }
            WANT_MEET -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        var wantmeetpoints = data.getSerializableExtra("charmPoint") as ArrayList<String>
                        if (wantmeetpoints.size > 0) {
                            for (i in 0..(wantmeetpoints.size - 1)) {
                                val wantmeetpoint = wantmeetpoints[i]
                                //배열로 입력저장은 [] 이걸 넣어준다\
                                adapterData4.add(wantmeetpoint)
                            }
                        }
                        wantmeetAdapter.notifyDataSetChanged()
                    }
                }
            }
            SELECT_LANGUAGE_REQUST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        adapterData.add(data.getStringExtra("selectedLanguage"))
                        languageAdapter.notifyDataSetChanged()
                    }
                }
            }
            SELECT_NATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        nationTV.text = data.getStringExtra("selectedNation")
                    }
                }
            }
            SELECT_TRAVEL -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        travelTV.text = data.getStringExtra("selectedNation")
                    }
                }
            }
            HEIGHT_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            REGION_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            POLICY_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            BABY_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            ANIMAL_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            SMOKE_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            DRINK_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            HEALTH_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            SPORTS_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }
            WORK_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        get_info()
                    }
                }
            }


        }
    }

}
