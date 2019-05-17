package com.example.chen.simpleparkingapp.controller.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.ActivityManager;
import com.example.chen.simpleparkingapp.base.CommonConstant;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.fragment.MineFragment;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppRequestUrl;
import com.example.chen.simpleparkingapp.utils.UploadFileUtil;
import com.example.chen.simpleparkingapp.view.dialog.EditSexDialog;
import com.example.chen.simpleparkingapp.view.dialog.PhotoDialog;
import com.example.chen.simpleparkingapp.viewmodel.PersonInfoViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, EditSexDialog.OnSexChangedListner, PhotoDialog.PhotoSelectorListener {

    private PersonInfoViewModel presentViewModel;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //修改昵称
    private final int EDIT_USER_NAME = 10002;
    private ImageView ivIcon;
    private TextView tvName, tvSex;
    private EditSexDialog editSexDialog;
    private PhotoDialog photoDialog;
    private User user;
    //调用照相机返回图片文件
    private File tempFile;
    private File file;
    private UploadFileUtil uploadFileUtil;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UploadFileUtil.UPLOAD_FAIL:
                    Bundle data = msg.getData();
                    if (data != null) {
                        String message = data.getString("message");
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtils.show(PersonInfoActivity.this, message);
                        }
                    }
                    break;
                case UploadFileUtil.UPLOAD_SUCCESS:
                    if (!TextUtils.isEmpty(headIcon)){
                        user.setIcon(headIcon);
                    }
                    if (gender != 0) {
                        user.setGender(gender);
                        UserCenter.getInstance().setUser(user);
                    }
                    setData();
                    ToastUtils.show(PersonInfoActivity.this, "修改成功");
                    dismissProgress();
                    MineFragment.isEdit = true;
                    break;
            }
        }
    };
    private String headIcon;
    private int gender;//修改的性别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ActivityManager.addActivity(this);
        user = UserCenter.getInstance().getUser();
        uploadFileUtil = new UploadFileUtil(handler);
        initView();
        setData();
    }

    private void initView() {
        initTitle();
        editSexDialog = new EditSexDialog(this);
        editSexDialog.setOnSexChangedListener(this);
        photoDialog = new PhotoDialog(this);
        photoDialog.setPhotoSelectorListener(this);
        ivIcon = findViewById(R.id.iv_icon);
        findViewById(R.id.ll_name).setOnClickListener(this);
        findViewById(R.id.rl_icon).setOnClickListener(this);
        findViewById(R.id.ll_sex).setOnClickListener(this);
        findViewById(R.id.ll_et_psw).setOnClickListener(this);
        findViewById(R.id.tv_exit).setOnClickListener(this);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_person_information);
        TextView tvTitle = titleView.findViewById(R.id.tv_title_middle);
        tvTitle.setText("个人信息");
        titleView.findViewById(R.id.ll_title_left).setOnClickListener(this);
    }

    //设置信息
    private void setData() {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getIcon())) {
                Glide.with(this).load(user.getIcon()).error(R.drawable.icon_no_login).bitmapTransform(new CropCircleTransformation(this)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().into(ivIcon);
            }
            String userName = user.getNickName();
            if (!TextUtils.isEmpty(userName)) {
                tvName.setText(userName);
            }
            if (user.getGender() != null) {
                tvSex.setText(user.getGender() == CommonConstant.MAN ? "男" : "女");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.rl_icon://头像
                if (!photoDialog.isShowing()) {
                    photoDialog.show();
                }
                break;
            case R.id.ll_name://修改昵称
                Route.route().nextController(this, EditPersonalNameActivity.class.getName(), EDIT_USER_NAME);
                break;
            case R.id.ll_sex://修改性别
                if (!editSexDialog.isShowing()) {
                    if (user != null && user.getGender() != null) {
                        editSexDialog.setSex(user.getGender());
                    }
                    editSexDialog.show();
                }
                break;
            case R.id.ll_et_psw://修改密码
                Route.route().nextController(this, EditPswActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tv_exit://退出登录
                logout();
                break;
        }
    }

    //退出登录
    private void logout() {
        UserCenter.getInstance().setUser(null);
        ActivityManager.finishAllActivity();
        Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
    }

    @Override
    public void onSexChange(boolean isChange, int sex) {
        if (isChange) {//修改性别
            editInfo(sex, "");
        }
    }

    //修改用户信息
    private void editInfo(int sex, String icon) {
        showDialog();
        gender = sex;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("nickName", "");
        params.put("gender", sex == 0 ? "" : ("" + sex));
        ArrayList<File> files = new ArrayList<>();
        File file = new File(icon);
        if (file.exists()) {
            files.add(file);
        }
        uploadFileUtil.uploadFileToServer(AppRequestUrl.EDIT_USER_INFO, files, params, "icon");
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (PersonInfoViewModel) baseViewModel;
    }


    //拍照
    @Override
    public void takePhotoFromCamera() {
        checkCameraPermission();
    }

    //从相册选择
    @Override
    public void takePhotoFromPic() {
        checkWritePermission();
    }

    //检查读写权限
    private void checkWritePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            getPic();
                        } else {
                            ToastUtils.show(PersonInfoActivity.this, "请在权限中心打开【存储】的权限");
                        }
                    }
                });
            } else {
                getPic();
            }
        } else {
            getPic();
        }
    }

    private void getPic() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    //检查相册的权限
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                RxPermissions.getInstance(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    getPicFromCamera();
                                } else {
                                    ToastUtils.show(PersonInfoActivity.this, "请在权限中心打开【相机】的权限");
                                }
                            }
                        });
            } else {
                getPicFromCamera();
            }
        } else {
            getPicFromCamera();
        }
    }

    //拍照
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
//        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        tempFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.chen.simpleparkingapp.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "user_head.jpg");
        intent.putExtra("output", Uri.fromFile(file));

        if (!isIntentAvailable(this, intent)) {
        } else {
            try {
                startActivityForResult(intent, CROP_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean isIntentAvailable(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   //调用相机后返回
                    if (resultCode == RESULT_OK) {
                        //用相机返回的照片去调用剪裁也需要对Uri进行处理
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(this, "com.example.chen.simpleparkingapp.fileprovider", tempFile);
                            cropPhoto(contentUri);
                        } else {
                            cropPhoto(Uri.fromFile(tempFile));
                        }
                    }
                    break;
                case ALBUM_REQUEST_CODE:    //调用相册后返回
                    if (resultCode == RESULT_OK) {
                        Uri uri = intent.getData();
                        cropPhoto(uri);
                    }
                    break;
                case CROP_REQUEST_CODE:     //调用剪裁后返回
                    if (file != null && file.exists()) {
                        //设置到ImageView上
                        headIcon = file.getAbsolutePath();
                        editInfo(0, headIcon);
                    }
                    break;
                case EDIT_USER_NAME://用户昵称
                    String nickName = intent.getStringExtra("nickName");
                    tvName.setText(nickName);
                    MineFragment.isEdit = true;
                    user.setNickName(nickName);
                    UserCenter.getInstance().setUser(user);
                    break;
            }
        }

    }

}
