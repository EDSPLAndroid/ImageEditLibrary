package com.xinlan.imageeditlibrary;

import android.os.Bundle;
import android.util.Patterns;

import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;

public class Splash extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //startApp();
    }

    protected void loadData() {

        /*enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        String data=response.body().string();

                            Gson gson = new Gson();
                            try {

                                Intent intent = new Intent(Splash.this, getMainClass());

                                if(getMainClassParam()!=null)
                                {
                                    intent.putExtra(ConstantUtil.BUNDLE,getMainClassParam());
                                }

                                startActivity(intent);
                            }catch (Exception e)
                            {
                                System.out.println(e);
                            }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/
    }

    protected Bundle getMainClassParam()
    {
        if(getIntent().getExtras().getBundle("bundle").containsKey(ConstantUtil.URL))
        {
            String imgLink = getIntent().getExtras().getBundle("bundle").getString(ConstantUtil.URL);

            if(Patterns.WEB_URL.matcher(imgLink).matches())
            {
                String imgURL = (imgLink != null)?imgLink:"";


                Bundle bundle = new Bundle();

                //For Image fetch from URL
                bundle.putString(ConstantUtil.URL,imgURL);

                //For Image fetch from FILE

        /*File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502868302331.jpg");
        //File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502956859656.jpg");
        //File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502957644243.jpg");
        bundle.putString(ConstantUtil.FILE,new Gson().toJson(file));*/
                return bundle;
            }
            else
            {
                finish();
                return null;
            }
        }
        return null;
    }

    protected Class<?> getMainClass(){
        return ImageEditHomeActivity.class;
    }
}
