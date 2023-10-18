package com.arslan.taskmaster.controller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    lateinit var navGraph: NavController
    val sharedPref = SharedPrefUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        initListeners()
        changeHeaderText()
    }

    private fun initListeners() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_fragment) as NavHostFragment
        navGraph = navHostFragment.navController

        binding.logoutBtn.setOnClickListener(this)
    }


    @SuppressLint("SetTextI18n")
    private fun changeHeaderText() {
        navGraph.addOnDestinationChangedListener { _, _, _ ->
            when (navGraph.currentDestination?.id) {
                R.id.dashBoardFragment -> {
                    binding.headerTxt.text = "All Items"
                }
                R.id.addItemFragment -> {
                    binding.headerTxt.text = "Add Item"
                }
                R.id.itemEditFragment -> {
                    binding.headerTxt.text = "Edit Item"
                }
                R.id.itemDetailFragment -> {
                    binding.headerTxt.text = "View Item Details"
                }
            }}

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.logout_btn ->{
                hideKeyboard()
                if (checkNetworkConnectivity(this)){
                    ShowProgressDialog(this,"Logout","Please wait...")
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    HideProgressDialog()
                    if (auth.currentUser == null) {
                        sharedPref.removeSharedPrefValue(this, SAVE_LOGIN)
                        navigate<LoginActivity>()
                    } else {
                        showToast(this, LOGOUT_MSG)
                    }
                } else {
                    showToast(this, CHECK_INTERNET)
                }

            }
        }
    }

}