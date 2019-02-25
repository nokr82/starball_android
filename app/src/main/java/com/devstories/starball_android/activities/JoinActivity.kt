package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_join.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class JoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    // facebook
    private var callbackManager: CallbackManager? = null
    private var accessToken: AccessToken? = null
    private var facebook_ID: String? = null
    private var facebook_NAME: String? = null

    // google
    private lateinit var mAuth: FirebaseAuth;
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        this.context = this
        progressDialog = ProgressDialog(context)


        // facebook
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        // google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        googleIV.setOnClickListener {
            /* val intent = Intent(context, Login2Activity::class.java)
             jointype = 3
             intent.putExtra("jointype",jointype)
             startActivity(intent)*/
//            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            val signInIntent = mGoogleSignInClient!!.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        facebookIV.setOnClickListener {
            /*  val intent = Intent(context, Login2Activity::class.java)
              jointype = 4
              intent.putExtra("jointype",jointype)
              startActivity(intent)*/
            disconnectFromFacebook()
        }

        emailET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {

                val email = Utils.getString(emailET)
                if(email.isNotEmpty() && Utils.isValidEmail(email)) {
                    joinTV.setBackgroundColor(Color.BLACK)
                } else {
                    joinTV.setBackgroundResource(R.drawable.background_border_strock2)
                }
            }
        })

        joinTV.setOnClickListener {

            val email = Utils.getString(emailET)

           if (email.isEmpty()) {
                dlg(getString(R.string.email_empty))
                return@setOnClickListener
            }

            if (!Utils.isValidEmail(email)) {
                dlg(getString(R.string.email_fail))
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "join_email", email)
            PrefUtils.setPreference(context, "join_join_type", 1)

            val intent = Intent(context, JoinStep1PasswdActivity::class.java)
            startActivity(intent)
        }

        loginTV.setOnClickListener {
            //            val intent = Intent(context, MainSearchActivity::class.java)
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                if(account != null) {
                    toJoinStep2Name(account.email, 3, account.id, account.displayName, account.photoUrl)
                }


            } catch (e: ApiException) {
                e.printStackTrace()
                Log.d("에러", e.toString())
            }
        }

        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    // 페이스북 로그아웃
    fun disconnectFromFacebook() {
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
            LoginManager.getInstance().logOut()
            doStartWithFacebook()
        }).executeAsync()
    }

    private fun doStartWithFacebook() {
        if (AccessToken.getCurrentAccessToken() != null) {
            this.accessToken = AccessToken.getCurrentAccessToken()
            fetchUserData()
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this@JoinActivity, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        accessToken = loginResult.accessToken
                        fetchUserData()
                    }

                    override fun onCancel() {
                        Toast.makeText(context, "페이스북 로그인 취소", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun fetchUserData() {
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->
            var id: String? = null
            var name: String? = null
            var eamil: String? = null

            try {
                if (`object`.has("id") && !`object`.isNull("id")) {
                    id = `object`.getString("id")
                }

                if (`object`.has("name") && !`object`.isNull("name")) {
                    name = `object`.getString("name")
                }

                if (`object`.has("email") && !`object`.isNull("email")) {
                    eamil = `object`.getString("email")
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            facebook_ID = id
            facebook_NAME = name

            toJoinStep2Name(eamil!!, 2, facebook_ID, facebook_NAME, null)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link, email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun toJoinStep2Name(email: String?, join_type: Int, sns_key: String?, name: String?, photo_url: Uri?) {

        PrefUtils.setPreference(context, "join_email", email)
        PrefUtils.setPreference(context, "join_join_type", join_type)
        PrefUtils.setPreference(context, "join_sns_key", sns_key)
        PrefUtils.setPreference(context, "join_name", name)
        PrefUtils.setPreference(context, "join_photo_url", photo_url.toString())

        val intent = Intent(context, JoinStep2NameActivity::class.java)
        startActivity(intent)
    }

    fun sns_join_old(
        email: String?,
        join_type: String,
        sns_key: String?,
        name: String?,
        photoUrl: Uri?
    ) {
        val params = RequestParams()
        params.put("name", name)
        params.put("join_type", join_type)
        params.put("email", email)
        params.put("sns_key", sns_key)
        params.put("photo_url", photoUrl)

        JoinAction.join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                println("response : $response")

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val data = response.getJSONObject("member")

                        data.put("autoLogin", true)

                        LoginActivity.processLoginData(context, data)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {


                        Toast.makeText(context, response!!.getString("message"), Toast.LENGTH_LONG).show()

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
//                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

//                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }


    fun dlg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}
