package com.example.virginmoney.ui.activities

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
import androidx.core.os.bundleOf
import com.example.virginmoney.App.Companion.analytics
import com.example.virginmoney.App.Companion.auth
import com.example.virginmoney.R
import com.example.virginmoney.ui.base.BaseActivity
import com.example.virginmoney.databinding.ActivityLoginBinding
import com.example.virginmoney.ui.dialogs.CustomDialog
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
//import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.regex.Pattern


class Login : BaseActivity() {

    private lateinit var callbackManager: CallbackManager
    private val TAG: String = Login::class.simpleName.toString()

    lateinit var bind: ActivityLoginBinding

//    private lateinit var auth: FirebaseAuth

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

//        auth = auth
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
        val layoutFirstLayout: TextInputLayout = loginForm.findViewById(R.id.layoutRegFirstName)
        val layoutLastLayout: TextInputLayout = loginForm.findViewById(R.id.layoutRegLastName)
        val layoutEmailLayout: TextInputLayout = loginForm.findViewById(R.id.layoutRegEmail)
        val layoutPasswordLayout: TextInputLayout = loginForm.findViewById(R.id.layoutRegPassword)
        val layoutPasswordConfirmLayout: TextInputLayout =
            loginForm.findViewById(R.id.layoutRegPasswordConfirm)

        val edFirst: TextInputEditText = loginForm.findViewById(R.id.edRegFirstName)
        val edLast: TextInputEditText = loginForm.findViewById(R.id.edRegLastName)
        val edEmail: TextInputEditText = loginForm.findViewById(R.id.edRegEmail)
        val edPassword: TextInputEditText = loginForm.findViewById(R.id.edRegPassword)
        val edPasswordConfirm: TextInputEditText =
            loginForm.findViewById(R.id.edRegPasswordConfirm)
        val btnReg: AppCompatButton = loginForm.findViewById(R.id.register)

        val letter: Pattern = Pattern.compile("[a-zA-Z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val special: Pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (layoutEmailLayout.error != null) {
                    layoutEmailLayout.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty() && !p0.toString().isValidEmail()) {
                    layoutEmailLayout.error = "Invalid email"
                }
            }
        })

        edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val hasLetter = letter.matcher(p0.toString())
                val hasDigit = digit.matcher(p0.toString())
                val hasSpecial = special.matcher(p0.toString())

                if (p0.isNullOrEmpty()) {
                    layoutPasswordLayout.error = "You must provide a password."
                } else if (p0.length < 8 || !hasLetter.find() || !hasDigit.find() || !hasSpecial.find()) {
                    layoutPasswordLayout.error =
                        "Password must be 8 chars long, contain uppercase/lowercase, number and special char"
                } else {
                    layoutPasswordLayout.error = null
                }
            }
        })

        edPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty() && edPassword.text.toString().trim().isNotEmpty()) {
                    layoutPasswordConfirmLayout.error = "Password mismatch!"
                } else if (edPassword.text.toString().trim() != edPasswordConfirm.text.toString()
                        .trim()
                ) {
                    layoutPasswordConfirmLayout.error = "Password mismatch!"
                } else {
                    layoutPasswordConfirmLayout.error = null
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        btnReg.setOnClickListener {
            var valid = true

            layoutFirstLayout.error = null
            layoutLastLayout.error = null
            layoutEmailLayout.error = null
            layoutPasswordLayout.error = null
            layoutPasswordConfirmLayout.error = null

            val firstName: String = edFirst.text.toString().trim()
            val lastName: String = edLast.text.toString().trim()
            val email: String = edEmail.text.toString().trim()
            val password: String = edPassword.text.toString().trim()
            val passwordConfirm: String = edPasswordConfirm.text.toString().trim()

            if (firstName.isEmpty()) {
                layoutFirstLayout.error = "This field is required"
                valid = false
            } else if (!firstName.matches(Regex("[a-zA-z\\-]+"))) {
                layoutFirstLayout.error = "This first name has wrong character"
            }
            if (lastName.isEmpty()) {
                layoutLastLayout.error = "This field is required"
                valid = false
            } else if (!lastName.matches(Regex("[a-zA-z\\-]+"))) {
                layoutLastLayout.error = "This first name has wrong character"
            }
            if (email.isEmpty()) {
                layoutEmailLayout.error = "This field is required"
                valid = false
            } else if (!email.isValidEmail()) {
                layoutEmailLayout.error = "Verify your email address"
            }
            if (password.isEmpty()) {
                layoutPasswordLayout.error = "This field is required"
                valid = false
            }
            if (passwordConfirm.isEmpty()) {
                layoutPasswordConfirmLayout.error = "This field is required"
                valid = false
            }

            if (password != passwordConfirm) {
                valid = false
            }

            if (!valid) {
                loginForm.shake()
                return@setOnClickListener
            }
            firebaseCreateUserWithEmailPassword(
                email = email,
                password = password,
                displayName = "$firstName $lastName"
            )
        }
    }

    private fun firebaseCreateUserWithEmailPassword(
        email: String,
        password: String,
        displayName: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { it ->
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(""))
                    .build()
                //update user tou provide additional info
                it.user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { _ ->
                        //user needs to receive email verification
                        it.user?.sendEmailVerification()
                        CustomDialog(this@Login, false)
                            .setTitle("Email Verification")
                            .setMessage("You will shortly receive an email to activate your account.")
                            .setPositive("Understood") {
                                toggleSignInSignUp()
                            }
                            .show()
                        auth.signOut()
//                        goToHomeActivity()
                    }
            }
            .addOnFailureListener { }
    }

    private fun facebookLogin() {
        callbackManager = CallbackManager.Factory.create()

//        fLogin.setReadPermissions("email", "public_profile")
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
//                it.user
//                it.additionalUserInfo
                if (it.user == null) {
                    return@addOnSuccessListener
                }
                if (!it.user!!.isEmailVerified) {
                    CustomDialog(this@Login, false)
                        .setTitle("Email Verification")
                        .setMessage("You need to verify your email to get access to the app.")
                        .setPositive("Understood") {}
                        .setNeutral("Re-send email") { _ ->
                            it.user!!.sendEmailVerification()
                        }
                        .show()
                    auth.signOut()
                    return@addOnSuccessListener
                }

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

    @Deprecated("Deprecated in Java")
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

                    if (auth.currentUser!!.isEmailVerified) {
                        goToHomeActivity()
                    } else {
                        auth.currentUser!!.sendEmailVerification()
                        CustomDialog(this@Login, false)
                            .setTitle("Email Verification")
                            .setMessage("You will shortly receive an email to activate your account.")
                            .setPositive("Understood") {}
                            .show()
                        auth.signOut()
                    }
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
//                    val user = auth.currentUser
//                    updateUI(user)
                    goToHomeActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@Login, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun goToHomeActivity() {
        analytics.logEvent(
            FirebaseAnalytics.Event.LOGIN,
            bundleOf(
                FirebaseAnalytics.Param.DESTINATION to Home::class.simpleName
            )
        )
        startActivity(Intent(this@Login, Home::class.java))
        this@Login.finish()
    }

}