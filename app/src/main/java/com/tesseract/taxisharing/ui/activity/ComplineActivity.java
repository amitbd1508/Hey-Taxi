package com.tesseract.taxisharing.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.frosquivel.magicaltakephoto.MagicalTakePhoto;
import com.tesseract.taxisharing.R;

import java.io.InputStream;


public class ComplineActivity extends AppCompatActivity {
    EditText des, contact;
    ImageView imageView;
    Button take,chose;
    MagicalTakePhoto magicalTakePhoto ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        magicalTakePhoto =  new MagicalTakePhoto(this,1010);
        des= (EditText) findViewById(R.id.et_complain_body);
        contact= (EditText) findViewById(R.id.et_complain_phone);
        take= (Button) findViewById(R.id.take);
        chose= (Button) findViewById(R.id.chose);
        ImageView imageView= (ImageView) findViewById(R.id.imageViewxx);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 109);
            }
        });
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                magicalTakePhoto.selectedPicture("Select Photo");
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==109)
        {
            if (data != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                imageView.setImageBitmap(photo); /* this is image view where you want to set image*/

                Log.d("camera ---- > ", "" + data.getExtras().get("data"));


            }
        }
        if(resultCode!=1010) return;
        magicalTakePhoto.resultPhoto(requestCode, resultCode, data);
        //example to get photo
        imageView.setImageBitmap(magicalTakePhoto.getMyPhoto());
    }
}