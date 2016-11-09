package com.example.matanhuja.finalproject.LoginPage;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.matanhuja.finalproject.MainActivity;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.R;

public class LoginActivity extends Activity implements LoginFragment.Delegate, SignUpFragment.Delegate
{
    LoginFragment loginFragment;
    SignUpFragment signUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Check if the user was already logged ing
        if(Model.getInstance().checkLastUserSql())
        {
            ModelUser modelUser = Model.getInstance().getLastUserSql();
            Log.d("TAG", "Login User Successed and the UID is: " + modelUser.getUid());
            Intent homeActivity = new Intent(this, MainActivity.class);
            homeActivity.putExtra("modelUser", modelUser);
            startActivityForResult(homeActivity,1);
        }
        //If not get login page
        else
        {
            getLoginPage();
        }
    }

    //Change fragments when signup is pressed
    public void onSignUpSelected()
    {
        signUpFragment = new SignUpFragment();
        signUpFragment.setDelegate(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.activity_login_container, signUpFragment, "y");
        transaction.addToBackStack("SignUp");
        transaction.show(signUpFragment);
        transaction.hide(loginFragment);
        transaction.commit();
    }

    //Change fragments when login is pressed
    public void onLoginSelected()
    {
        int count = getFragmentManager().getBackStackEntryCount();
        getFragmentManager().popBackStack();
    }

    public void getLoginPage()
    {
        loginFragment = new LoginFragment();
        loginFragment.setDelegate(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.activity_login_container, loginFragment, "y");
        transaction.show(loginFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
        {
            onBackPressed();
        }
        if (resultCode == 2)
        {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
