package com.example.android.cruxinduction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static final int RUNTIME_PERMISSION_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pick = (Button) findViewById(R.id.pick);
        Button fav = (Button) findViewById(R.id.fav);
        AndroidRuntimePermission();
       // AndroidRuntimePermission2();

     pick.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(isReadPermissionGranted()&&isWriteStoragePermissionGranted())
             {
                 Intent i = new Intent(Intent.ACTION_PICK,
                         android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(i, 2);
             }

         }
     });
     fav.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i = new Intent(MainActivity.this,ImagesActivity.class);
             startActivity(i);
         }
     });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {
            String filename;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);

            //file path of captured image
            File f = new File(filePath);
            filename = f.getName();
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
          /*  int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);*/


          /*  if (bitmap != null) {
                ImageView image = (ImageView) findViewById(R.id.rotate);
                image.setImageBitmap(scaled);
            }

        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
                    //onPhotoTaken();
                    break;

            }

        }*/
            Savefile(bitmap);
        }
    }
    public void AndroidRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)&&
                    (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) )
             {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)&&shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},

                                    RUNTIME_PERMISSION_CODE
                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);
                    AlertDialog dialog = alert_builder.create();
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }
        }
    }
  /*  public void AndroidRuntimePermission2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission((Manifest.permission.WRITE_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    3
                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);
                    AlertDialog dialog = alert_builder.create();
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3
                    );
                }
            }
        }
    }*/

    public boolean isReadPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("write","Permission is granted2");
                return true;
            } else {

                Log.v("write","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RUNTIME_PERMISSION_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("write","Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RUNTIME_PERMISSION_CODE: {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);

            }

        }
    }


    public void Savefile(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/fav_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

