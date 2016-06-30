package com.paditech.cvmarker.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.adapter.BottomDialogAdapter;
import com.paditech.cvmarker.adapter.ResumeListAdapter;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.pdf.SimpleClean;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.FileUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.PreferenceUtils;
import com.paditech.cvmarker.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 10/6/2016.
 */
public class HomeActivity extends BaseActivity implements ResumeListAdapter.OnCreateNewClicked,
        ResumeListAdapter.OnResumeItemClicked, BottomDialogAdapter.OnDialogItemClick{

    private final static int CAMERA_REQUEST = 111;
    private final static int PICK_PICTURE_REQUEST = 222;
    private final static int PERMISSON_STORAGE = 100;

    private List<Resume> mResumeList;
    private ResumeListAdapter adapter;
    private Resume resume;
    private DialogPlus dialogPlus;

    @InjectView(R.id.grid_resume)
    GridView mGridView;

    @InjectView(R.id.resume_total)
    TextView mResumeTotal;

    @InjectView(R.id.avatar)
    ImageView mAvatar;


    private String avatarFilePath = "";

    @Override
    protected void initView() {

        mResumeList = PreferenceUtils.getListResume(this);
        if(mResumeList == null) mResumeList = new ArrayList<>();
        adapter = new ResumeListAdapter(this, mResumeList);
        adapter.setOnCreateNewClick(this);
        adapter.setmOnResumeItemClick(this);
        mGridView.setAdapter(adapter);
        mResumeTotal.setText(String.format(getString(R.string.resume_total), mResumeList.size()));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.my_resume_builder);
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.mipmap.logo);
    }

    @Override
    protected Drawable getRightDrawable() {
        return getResources().getDrawable(R.drawable.btn_settings_pressed);
    }

    @OnClick(R.id.btn_right)
    public void onSettings() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateNewClicked() {
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(20,20,20,20);
        final EditText nameInput = new EditText(this);
        nameInput.setLayoutParams(params);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_create_new));
        builder.setView(nameInput);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!StringUtils.isEmpty(nameInput.getText().toString().trim())) {
                    resume = new Resume();
                    resume.setName(nameInput.getText().toString().trim());
                    resume.setStyle(1);
                    resume.setLanguage(PreferenceUtils.getDefaultLanguage(HomeActivity.this));
                    if(PreferenceUtils.getPesonalIsDefault(HomeActivity.this)) {
                        Personal personal = PreferenceUtils.getDefaultPesonal(HomeActivity.this);
                        if(personal != null) {
                            resume.setPersonal(personal);
                        }
                    }

                    mResumeList.add(resume);
                    PreferenceUtils.saveListResume(HomeActivity.this, mResumeList);
                    Intent intent = new Intent(HomeActivity.this, FillResumeDetailActivity.class);
                    intent.putExtra(Constant.RESUME_INDEX, mResumeList.size() - 1);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    @Override
    public void onResumeItemClick(final int index) {
        BottomDialogAdapter bottomDialogAdapter = new BottomDialogAdapter(this);
        bottomDialogAdapter.setOnDialogItemClick(new BottomDialogAdapter.OnDialogItemClick() {
            @Override
            public void onDialogItemClick(int position) {
                if(dialogPlus != null) dialogPlus.dismiss();
                switch (position) {
                    case 0:
                        Intent intent = new Intent(HomeActivity.this, FillResumeDetailActivity.class);
                        intent.putExtra(Constant.RESUME_INDEX, index);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(HomeActivity.this, TemplateResumeActivity.class);
                        intent1.putExtra(Constant.PREVIEW_TYPE, 1);
                        intent1.putExtra(Constant.RESUME_INDEX, index);
                        intent1.putExtra(Constant.RESUME_NAME, mResumeList.get(index).getName());
                        startActivity(intent1);
                        break;

                    case 2:
                        String filePath = FileUtil.getPdfFile(mResumeList.get(index).getName() + ".pdf");
                        File file = new File(filePath);
                        if(!file.exists()) {
                            SimpleClean simpleClean;
                            simpleClean = new SimpleClean(HomeActivity.this, mResumeList.get(index));
                            simpleClean.setContent(filePath);
                        }
                        PDFUtils.exportToMain(HomeActivity.this, filePath);

                        break;

                    case 3:
                        String path = FileUtil.getPdfFile(mResumeList.get(index).getName() + ".pdf");
                        File f = new File(path);
                        if(f.exists()) {
                            Intent printIntent = new Intent(HomeActivity.this, PrintActivity.class);
                            printIntent.setDataAndType(Uri.fromFile(f), "application/pdf");
                            printIntent.putExtra(Constant.RESUME_NAME, f.getName());
                            startActivity(printIntent);
                        }
                        break;

                    case 4:
                        final EditText editText = new EditText(HomeActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage(getResources().getString(R.string.enter_new_name));
                        builder.setView(editText);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!StringUtils.isEmpty(editText.getText().toString())) {
                                    String oldName = mResumeList.get(index).getName();
                                    String newName = editText.getText().toString().trim();

                                    File oldFile = new File(FileUtil.getPdfFile(oldName+ ".pdf") );
                                    File newFile = new File(FileUtil.getPdfFile(newName+ ".pdf") );
                                    boolean rs = oldFile.renameTo(newFile);
                                    Log.e("RENAME", rs + "");
                                    mResumeList.get(index).setName(editText.getText().toString().trim());
                                    adapter.setmResumeList(mResumeList);
                                    PreferenceUtils.saveListResume(HomeActivity.this, mResumeList);
                                }
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
                        builder.show();

                        break;

                    case 5:
                        mResumeList = PreferenceUtils.getListResume(HomeActivity.this);
                        File fileDel = new File(FileUtil.getPdfFile(mResumeList.get(index).getName() + ".pdf"));
                        if(fileDel.exists()) {
                            boolean isdel = fileDel.delete();
                            Log.e("DELETE file ", isdel + "");
                        }
                        mResumeList.remove(index);
                        PreferenceUtils.saveListResume(HomeActivity.this, mResumeList);
                        adapter.setmResumeList(mResumeList);
                        mResumeTotal.setText(String.format(getString(R.string.resume_total), mResumeList.size()));
                        break;

                }
            }
        });
        final DialogPlusBuilder builder = DialogPlus.newDialog(HomeActivity.this)
                .setExpanded(true)
                .setCancelable(true)
                .setContentHolder(new GridHolder(3))
                .setGravity(Gravity.CENTER)
                .setAdapter(bottomDialogAdapter)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        dialog.dismiss();
                    }
                });
        dialogPlus = builder.create();
        dialogPlus.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkpermisson();
        setLanguage(PreferenceUtils.getDefaultLanguage(this));
        mResumeList = PreferenceUtils.getListResume(this);
        if(mResumeList != null) {
            adapter.setmResumeList(mResumeList);
            mResumeTotal.setText(String.format(getString(R.string.resume_total), mResumeList.size()));
            setAvatar();
        } else mResumeList = new ArrayList<>();
    }

    @Override
    public void onDialogItemClick(int position) {

    }

    @OnClick(R.id.avatar)
    public void onAvatarClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getResources().getString(R.string.capture_photo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = FileUtil.setImageUri();
                avatarFilePath = uri.getPath();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.pick_photo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"), PICK_PICTURE_REQUEST);
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
           if(requestCode == CAMERA_REQUEST) {
               avatarFilePath = FileUtil.resizeImage(new File(avatarFilePath));
               Bitmap bitmap = FileUtil.getBitmapFromFile(new File(avatarFilePath));
               mAvatar.setImageBitmap(bitmap);
               PreferenceUtils.setAvatar(this, avatarFilePath);
           } else {
               if(requestCode == PICK_PICTURE_REQUEST) {
                   try {
                       mAvatar.setImageBitmap(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData()));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   Uri uri = data.getData();
                   String avatarPath = FileUtil.getFilePathFromUri(this, uri);
                   avatarPath = FileUtil.resizeImage(new File(avatarPath));
                   PreferenceUtils.setAvatar(this, avatarPath);
               }
           }
        }

    }

    private void setAvatar() {
        String filePath = PreferenceUtils.getAvatar(this);
        Bitmap bitmap = FileUtil.getBitmapFromFile(new File(filePath));
        if(bitmap != null) {
            mAvatar.setImageBitmap(bitmap);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        LinearLayout mRootAds=(LinearLayout)findViewById(R.id.root_ads);
        adView.setAdUnitId(getResources().getString(R.string.banner_home_footer));
        mRootAds.addView(adView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AdRequest ar = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(ar);
    }

    private void checkpermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showPermissonDialog();
            }
        }
    }

    private void showPermissonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.permisson_storage_dialog));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, PERMISSON_STORAGE);
                }
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.cancel), null);
        builder.show();
    }


}
