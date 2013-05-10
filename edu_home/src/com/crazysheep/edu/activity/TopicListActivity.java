package com.crazysheep.edu.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.crazysheep.edu.R;
import com.crazysheep.edu.adapter.PhotoAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 主题列表 相册照片列表
 *
 * @author ivan
 */
public class TopicListActivity extends ActionBarActivity implements
        OnItemClickListener, OnClickListener {

    private ArrayList<Photo> photos = new ArrayList<Photo>();
    private GridView gridView;
    private PhotoAdapter adapter;

    public final static String EXTRA_FILENAME = "extra_filename";
    public final static String EXTRA_MSG = "extra_msg";
    public final static String EXTRA_ALBUM = "extra_album";
    public final static String EXTRA_STATE = "extra_state";
    private Album album;

    private TakePhotoUtils takePhoto;

    boolean refresh = false;

    public static void startActivity(Activity context, String msg, Album album,
                                     int requestCode) {
        Intent intent = new Intent(context, TopicListActivity.class);
        intent.putExtra(EXTRA_MSG, msg);
        intent.putExtra(EXTRA_ALBUM, album);
        context.startActivityForResult(intent, requestCode);
        // context.startActivity(intent);
    }

    @Override
    public void finish() {
        if (refresh)
            setResult(RESULT_OK);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        String msg = getIntent().getStringExtra(EXTRA_MSG);
        // name.setText(msg);

        setTitle(msg);
        setHomeActionListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightAction("上传", new OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.photo).setVisibility(View.VISIBLE);
            }
        });
        album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);

        takePhoto = new TakePhotoUtils(this);

        gridView = (GridView) findViewById(R.id.grid_view);
        adapter = new PhotoAdapter(this, photos);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        updateAlbumPhotoLis();
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            // 调用Gallery返回的
            case TakePhotoUtils.PHOTO_PICKED_WITH_DATA:
                if (data != null) {
                    System.err.println("PHOTO_PICKED_WITH_DATA");
                    UploadAlbumPhotos(takePhoto.getImageArgs(this, data.getData()));
                    // takePhoto.doCropPhoto(data.getData());
                }
                break;
            // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
            case TakePhotoUtils.CAMERA_WITH_DATA:
                Uri imageUri = takePhoto.getImageFilePath();
                if (imageUri != null) {
                    UploadAlbumPhotos(takePhoto.getImageArgs(this, imageUri));
                    // takePhoto.doCropPhoto(imageUri);
                }
                break;
            case TakePhotoUtils.PHOTO_RESULT_DATA:// 返回处理结果
                // Bundle extras = data.getExtras();
                // if (extras != null) {
                // Bitmap photo = extras.getParcelable("data");
                // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // (0 - 100)压缩文件
                // photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                // String filePath = takePhoto.saveBitmap(photo);
                //
                // }
                break;
        }
    }

    private void UploadAlbumPhotos(String resume_file) {
        // File file = new File(resume_file);
        // if (!file.isFile()) {
        // UIUtils.showToast(this, "图片地址不对");
        // return;
        // }
        Bitmap bitmap = CommonUtils.decodeSampledBitmapFromFile(resume_file,
                600, 800);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 90, bos);
        byte[] bitmapdata = bos.toByteArray();
        final ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        final ProgressDialog progress = UIUtils.newProgressDialog(this,
                "上传中...");
        JsonHandler handler = new JsonHandler(this) {
            @Override
            public void onStart() {
                super.onStart();
                findViewById(R.id.photo).setVisibility(View.GONE);
                UIUtils.safeShow(progress);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtils.safeDismiss(progress);
                try {
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                LogUtils.I(LogUtils.UPLOAD_PHOTO, response.toString());
                Toast.makeText(TopicListActivity.this, "上传成功",
                        Toast.LENGTH_SHORT).show();
                response = response.optJSONObject("photofileinfo");
                Photo photo = new Photo(response);
                photos.add(photo);
                adapter.notifyDataSetInvalidated();
                refresh = true;
            }
        };
        User user = AppConfig.getAppConfig(this).getUser();

        // ContentResolver resolver = getActivity().getContentResolver();
        // try {
        // InputStream
        // inStream=resolver.openInputStream(Uri.parse(resume_file));
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }
        APIService.UploadAlbumPhotos(album.albumID, album.gid, user.classID,
                user.memberid, bs, handler);

    }

    private void updateAlbumPhotoLis() {
        final ProgressDialog progress = UIUtils
                .newProgressDialog(this, "请稍等..");
        JsonHandler handler = new JsonHandler(this) {
            @Override
            public void onStart() {
                super.onStart();
                UIUtils.safeShow(progress);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtils.safeDismiss(progress);
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                LogUtils.I(LogUtils.PhotoList, response.toString());
                JSONArray array = response.optJSONArray("photofileinfos");
                if (array != null) {
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        Photo photo = new Photo(array.optJSONObject(i));
                        photos.add(photo);
                    }

                    adapter.notifyDataSetInvalidated();
                }
            }
        };
//        User user = AppConfig.getAppConfig(this).getUser();
        APIService.GetAlbumPhotoList(album.albumID, handler);
//        APIService.GetAlbumPhotoList(album.albumID, album.gid, user.classID,
//                user.memberid, handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabhost_take:// 拍照
                takePhoto.doTakePhoto();
                break;
            case R.id.tabhost_pick:// 相册
                takePhoto.doPickPhotoFromGallery();
                break;
            case R.id.tabhost_cancel:
                findViewById(R.id.photo).setVisibility(View.GONE);
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        PhotoActivity.startActivity(this, album.albumID, position, photos);
    }
}
