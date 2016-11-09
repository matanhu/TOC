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

import com.example.matanhuja.finalproject.MainActivity;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.R;


public class SignUpFragment extends Fragment
{
    EditText email;
    EditText password;
    Button loginButton;
    ImageButton signUpButton;
    Delegate delegate;
    ModelUser modelUser;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        email = (EditText) view.findViewById(R.id.fragment_sign_up_email_edit);
        password = (EditText) view.findViewById(R.id.fragment_sign_up_password_edit);
        loginButton = (Button) view.findViewById(R.id.fragment_sign_up_title_login);
        signUpButton = (ImageButton) view.findViewById(R.id.fragment_sign_up_button_sign_up);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_sign_up_ProgressBar);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (delegate != null)
                {
                    delegate.onLoginSelected();
                }

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                modelUser = new ModelUser(email.getText().toString().trim(),password.getText().toString().trim());
                Model.getInstance().createUser(modelUser, new Model.createUserListener()
                {
                    @Override
                    public void onSuccess(String uid)
                    {
                        if(uid != null)
                        {
                            modelUser.setUid(uid);
                            Model.getInstance().setLastUserSql(modelUser);
                            Intent homeActivity = new Intent(getActivity(), MainActivity.class);
                            homeActivity.putExtra("modelUser", modelUser);
                            getActivity().startActivityForResult(homeActivity, 1);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onError()
                    {
                        progressBar.setVisibility(View.GONE);
                        Log.d("TAG", "Create User Faild");
                    }
                });
            }
        });
        return view;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    interface Delegate
    {
        void onLoginSelected();
    }
}
