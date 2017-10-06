package com.xinlan.imageeditlibrary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kimeeo.kAndroid.core.utils.NetworkUtilities;

public class InternetErrorActivity extends AppBaseActivity {
    private View.OnClickListener onRetryClick = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            retry(true);
        }
    };
    private Handler handler;

    protected void retry(boolean showFailMSG) {
        if(isConnected())
        {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        else
        {
            if(showFailMSG)
                Toast.makeText(this, R.string.internet_connection_fail_msg, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_error);

        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(onRetryClick);
        handler = new Handler();
        handler.postDelayed(runnable,getCheckInterval());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_24dp_inset));
    }

    protected long getCheckInterval() {
        return 5000;
    }

    Runnable runnable = new Runnable()
    {

        @Override
        public void run() {
            if(isConnected())
            {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            else
            handler.postDelayed(runnable,getCheckInterval());
        }
    };

    public boolean isConnected() {
        try {
            return NetworkUtilities.isConnected(this);
        } catch (Exception var2) {
            return false;
        }
    }
}