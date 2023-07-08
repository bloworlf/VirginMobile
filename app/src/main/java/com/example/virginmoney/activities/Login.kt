package com.example.virginmoney.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.virginmoney.R
import com.example.virginmoney.base.BaseActivity
import com.example.virginmoney.databinding.ActivityLoginBinding
import com.example.virginmoney.utils.Utils.isValidEmail
import com.example.virginmoney.utils.Utils.shake
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class Login : BaseActivity() {

    private lateinit var callbackManager: CallbackManager
    private val TAG: String = Login::class.simpleName.toString()

    lateinit var bind: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest

    lateinit var loginForm: MaterialCardView

    lateinit var toggle: TextView

    lateinit var gLogin: SignInButton
    lateinit var fLogin: LoginButton

    private var login: Boolean =
        true //true: login form is displayed, false: registration form is displayed

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        toggle = bind.signinSignupToggle
        toggle.setOnClickListener {
            login = !login
            toggleSignInSignUp()
        }

        loginForm = bind.loginForm
        toggleSignInSignUp()

        gLogin = bind.gLogin
        fLogin = bind.fLogin

        gLogin.setOnClickListener {
//            firebaseGoogleLogin()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        fLogin.setOnClickListener {
            facebookLogin()
        }
    }

    private fun setupLoginForm() {
        val layoutEmail: TextInputLayout = loginForm.findViewById(R.id.layoutEmail)
        val layoutPassword: TextInputLayout = loginForm.findViewById(R.id.layoutPassword)
        val edEmail: TextInputEditText = loginForm.findViewById(R.id.edEmail)
        val edPassword: TextInputEditText = loginForm.findViewById(R.id.edPassword)
        val btnLogin: AppCompatButton = loginForm.findViewById(R.id.login)

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (layoutEmail.error != null) {
                    layoutEmail.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty() && !p0.toString().isValidEmail()) {
                    layoutEmail.error = "Invalid email"
                }
            }
        })

        edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (layoutPassword.error != null) {
                    layoutPassword.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        btnLogin.setOnClickListener {
            layoutEmail.error = null
            layoutPassword.error = null

            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    layoutEmail.error = "Field is required"
                }
                if (password.isEmpty()) {
                    layoutPassword.error = "Field is required"
                }
                loginForm.shake()
                return@setOnClickListener
            }

            if (!email.isValidEmail()) {
                layoutEmail.error = "Invalid email"
                return@setOnClickListener
            }

            firebaseCredentialsLogin(email, password)
        }
    }

    private fun toggleSignInSignUp() {
        if (login) {
            //display login view
            toggle.text = getString(R.string.create_account_text)
            loginForm.removeAllViews()
            loginForm.addView(layoutInflater.inflate(R.layout.include_sign_in, null))

            setupLoginForm()
        } else {
            //display sign up view
            toggle.text = getString(R.string.login_email_password)
            loginForm.removeAllViews()
            loginForm.addView(layoutInflater.inflate(R.layout.include_sign_up, null))

            setupRegisterForm()
        }
    }

    private fun setupRegisterForm() {
        // TODO: check for empty fields... complete registration form
        val edFirst: TextInputEditText = loginForm.findViewById(R.id.edRegFirstName)
        val edLast: TextInputEditText = loginForm.findViewById(R.id.edRegLastName)
        val edEmail: TextInputEditText = loginForm.findViewById(R.id.edRegEmail)
        val edPassword: TextInputEditText = loginForm.findViewById(R.id.edRegPassword)
        val edPasswordConfirm: TextInputEditText =
            loginForm.findViewById(R.id.edRegPasswordConfirm)
        val btnReg: AppCompatButton = loginForm.findViewById(R.id.register)
        btnReg.setOnClickListener {
            val email: String = edEmail.text.toString().trim()
            val password: String = edPassword.text.toString().trim()

            firebaseCreateUserWithEmailPassword(email, password)
        }
    }

    private fun firebaseCreateUserWithEmailPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { it ->
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("")
                    .setPhotoUri(Uri.parse(""))
                    .build()
                //update user tou provide additional info
                it.user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener {
                        //no need success
                        goToHomeActivity()
                    }
//                    ?.addOnSuccessListener { }
//                    ?.addOnFailureListener { }
            }
            .addOnFailureListener { }
    }

    private fun facebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        fLogin.setReadPermissions("email", "public_profile")
        fLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$result")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                fLogin.shake()
            }
        })
    }

    private fun firebaseCredentialsLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { it ->
                it.user
                it.additionalUserInfo

                val intent = Intent(this@Login, Home::class.java)
                //set user maybe
                startActivity(intent)
                this@Login.finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@Login, "Couldn't sign in for $email", Toast.LENGTH_SHORT).show()
                loginForm.shake()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign in failed:\n${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            if (this::callbackManager.isInitialized) {
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    val user = auth.currentUser
//                    Toast.makeText(
//                        this,
//                        "Sign in successful: ${user?.displayName}",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    goToHomeActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Sign in failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    gLogin.shake()
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }
            }
    }

    private fun goToHomeActivity() {
        startActivity(Intent(this@Login, Home::class.java))
        this@Login.finish()
    }

}