package com.paditech.cvmarker.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.exception.FileNotFoundException;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.FileUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.PreferenceUtils;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 13/6/2016.
 */
public class TemplateResumeActivity extends BaseActivity implements OnPageChangeListener {

    @InjectView(R.id.pdfView)
    PDFView mPdfView;

    @InjectView(R.id.text_position)
    TextView mTextPosition;

    private String filePath = "";
    private int type = 0;
    private Resume resume;
    private int index = 0;
    private int style = 1;

    @Override
    protected void initView() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(Constant.PREVIEW_TYPE);
            index = bundle.getInt(Constant.RESUME_INDEX);
            resume = PreferenceUtils.getResume(this, index);
            style = bundle.getInt(Constant.RESUME_STYLE);
        }
        if (resume != null) {
            setLanguage(resume.getLanguage());
        } else {
            setLanguage(PreferenceUtils.getDefaultLanguage(this));
        }

        mPdfView.useBestQuality(true);
        File fileSimple;
        if (type == 0) {
            mTextPosition.setVisibility(View.VISIBLE);
            String simple = FileUtil.getInternalFilePath(this);
            fileSimple = new File(simple);
            try {
                mPdfView.fromFile(fileSimple)
                        .defaultPage(style)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .onPageChange(this)
                        .load();
            } catch (FileNotFoundException e) {
            }

        } else if (type == 1) {
            mTextPosition.setVisibility(View.GONE);
            LoadFileAsync async = new LoadFileAsync();
            async.onPreExecute();
            File file = (File) async.doInBackground(null);
            async.onPostExecute(file);

        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_template_resume;
    }

    @Override
    protected String getHeaderTitle() {
        if (type == 1) {
            if (getIntent().getExtras() != null)
                return getIntent().getExtras().getString(Constant.RESUME_NAME);
        }
        return getResources().getString(R.string.template_resume);
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.mipmap.ic_back);
    }

    @Override
    protected Drawable getRightDrawable() {
        if (type == 0) return getResources().getDrawable(R.mipmap.ic_done);
        else return getResources().getDrawable(R.mipmap.ic_export_white);
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        mTextPosition.setText(page + "/" + pageCount);
    }

    @OnClick(R.id.btn_right)
    public void export() {
        if (type == 1) exportSendMail();
        else {
            PreferenceUtils.saveStyle(this, index, mPdfView.getCurrentPage() + 1);
            finish();
        }

    }

    @OnClick(R.id.btn_left)
    public void onBack() {
        finish();
    }

    private void exportSendMail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PDFUtils.exportToMain(TemplateResumeActivity.this, filePath);
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.print), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(filePath);
                if (file.exists()) {
                    Intent printIntent = new Intent(TemplateResumeActivity.this, PrintActivity.class);
                    printIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    printIntent.putExtra(Constant.RESUME_NAME, file.getName());
                    startActivity(printIntent);
                }
            }
        });

        builder.show();
    }

    private class LoadFileAsync extends AsyncTask {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TemplateResumeActivity.this);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            resume = PreferenceUtils.getResume(TemplateResumeActivity.this, index);
            String simple = FileUtil.getPdfFile(resume.getName() + ".pdf");
            final File fileSimple = PDFUtils.getFileStyle(TemplateResumeActivity.this, simple, resume, resume.getStyle());
            filePath = fileSimple.getPath();

            return fileSimple;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                mPdfView.fromFile((File) o)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .load();
            } catch (FileNotFoundException e) {
            }
            dialog.dismiss();
        }
    }

}
