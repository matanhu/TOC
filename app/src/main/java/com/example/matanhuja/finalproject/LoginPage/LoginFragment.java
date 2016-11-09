package com.example.matanhuja.finalproject.LoginPage;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.matanhuja.finalproject.MainActivity;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.R;

public class LoginFragment extends Fragment
{
    EditText email;
    EditText password;
    ImageButton loginButton;
    Button signUpButton;
    Delegate delegate;
    ModelUser modelUser;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_login,container, false);
        email = (EditText) view.findViewById(R.id.fragment_login_email_edit);
        password = (EditText) view.findViewById(R.id.fragment_login_password_edit);
        loginButton = (ImageButton) view.findViewById(R.id.fragment_login_button_login);
        signUpButton = (Button) view.findViewById(R.id.fragment_login_title_sign_up);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_login_ProgressBar);

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(delegate != null)
                {
                    delegate.onSignUpSelected();
                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                modelUser = new ModelUser(email.getText().toString().trim(), password.getText().toString().trim());
                Model.getInstance().loginUser(modelUser, new Model.loginUserListener()
                {
                    @Override
                    public void onAuthenticated(String uid)
                    {
                        modelUser.setUid(uid);
                        Model.getInstance().setLastUserSql(modelUser);
                        Log.d("TAG", "Login User Successed and the UID is: " + uid);
                        Intent homeActivity = new Intent(getActivity(), MainActivity.class);
                        homeActivity.putExtra("modelUser", modelUser);
                        getActivity().startActivityForResult(homeActivity, 1);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAuthenticationError()
                    {
                        Toast.makeText(getActivity(),"Login User Failed", Toast.LENGTH_LONG).show();
                        Log.d("TAG", "Login User Failed");
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        return view;
    }

    interface Delegate
    {
        void onSignUpSelected();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
