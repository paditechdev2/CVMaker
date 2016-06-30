package com.paditech.cvmarker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.itextpdf.text.Paragraph;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.model.OtherInfo;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.model.WorkExperience;
import com.paditech.cvmarker.pdf.AttachAvatarSimple;
import com.paditech.cvmarker.pdf.BlueSimple;
import com.paditech.cvmarker.pdf.ClassicSimple;
import com.paditech.cvmarker.pdf.EleganeBlue;
import com.paditech.cvmarker.pdf.RedDashLineSimple;
import com.paditech.cvmarker.pdf.RedSimpleStyle;
import com.paditech.cvmarker.pdf.SimpleClean;
import com.paditech.cvmarker.pdf.SpecialName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by USER on 13/6/2016.
 */
public class PDFUtils {

    public static Resume setDefaultResume () {
        Resume resume = new Resume();
        Personal personal = new Personal();
        personal.setName("My Name");
        personal.setPhoneNumber("0123456789");
        personal.setAddress("Cable Street");
        personal.setEmail("mymail@email.com");
        resume.setPersonal(personal);

        List<Education> educations = new ArrayList<>();
        Education e1 = new Education();
        e1.setDatePeriod("1/1/2016");
        e1.setDegree("University");
        e1.setLocation("New York");
        e1.setMajor("Infomation Technology");
        e1.setSchoolName("Oxfort");

        Education e2 = new Education();
        e2.setDatePeriod("1/1/2017");
        e2.setDegree("University");
        e2.setLocation("New York");
        e2.setMajor("Infomation Technology");
        e2.setSchoolName("Oxfort");
        educations.add(e1);
        educations.add(e2);

        resume.setEducationList(educations);

        List<String> objectives = new ArrayList<>();
        objectives.add("This is my first objective, it's very huge for me");
        objectives.add("This is my second objective, it's very huge for me");
        resume.setObjectiveList(objectives);

        List<WorkExperience> expers = new ArrayList<>();
        WorkExperience w1 = new WorkExperience();
        w1.setDatePeriod("1/1/2014");
        w1.setCompanyName("Paditech");
        w1.setJobLocation("Deverloper");
        w1.setJobResponsibility("Android Deverloper");
        w1.setJobTitle("Job title");

        expers.add(w1);
        expers.add(w1);
        resume.setWorkExperienceList(expers);

        List<String> skills = new ArrayList<>();
        skills.add("This is my first skill");
        skills.add("This is my second skill");
        resume.setSkillList(skills);

        List<OtherInfo> others = new ArrayList<>();
        OtherInfo o = new OtherInfo();
        o.setSectionDesc("This is the content of first other section");
        o.setSectionName("First other section");
        others.add(o);
        others.add(o);
        resume.setOtherInfoList(others);
        return resume;
    }

    public static void exportToMain(Context context, String filePath) {
        File filelocation = new File(filePath);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        context.startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    public static File getFileStyle(Context context, String filePath, Resume resume, int style) {
        File fileSimple = new File(filePath);
        if(style == 1) {
            SimpleClean simpleClean;
            simpleClean = new SimpleClean(context, resume);
            simpleClean.setContent(filePath);
            return fileSimple;
        }
        if(style == 2) {
            RedSimpleStyle simpleClean;
            simpleClean = new RedSimpleStyle(context, resume);
            simpleClean.setContent(filePath);
            return fileSimple;
        }
        if(style == 3) {
            ClassicSimple classicSimple = new ClassicSimple(context, resume);
            classicSimple.setContent(filePath);
            return fileSimple;
        }
        if(style == 4) {
            AttachAvatarSimple avatarSimple = new AttachAvatarSimple(context, resume);
            avatarSimple.setContent(filePath);
            return fileSimple;
        }
        if(style == 5) {
            RedDashLineSimple redDashLineSimple = new RedDashLineSimple(context, resume);
            redDashLineSimple.setContent(filePath);
            return fileSimple;
        }
        if(style == 6) {
            BlueSimple blueSimple = new BlueSimple(context, resume);
            blueSimple.setContent(filePath);
            return fileSimple;
        }
        if(style == 7) {
            SpecialName specialName = new SpecialName(context, resume);
            specialName.setContent(filePath);
            return fileSimple;
        }
        if(style == 8) {
            EleganeBlue eleganeBlue = new EleganeBlue(context, resume);
            eleganeBlue.setContent(filePath);
            return fileSimple;
        }
        return fileSimple;
    }

    public static Paragraph addBlankLine() {
        Paragraph paragraph = new Paragraph();
        paragraph.add("\n");
        return paragraph;
    }

    public static String getStyleName(int style) {
        switch (style) {
            case 1: return Constant.SIMPLE_CLEAN;
            case 2: return Constant.RED_SIMPLE_CLEAN;
            case 3: return Constant.CLASSIC_SIMPLE;
            case 4: return Constant.AVATAR_SIMPLE;
            case 5: return Constant.RED_DASH_LINE;
            case 6: return Constant.BLUE_SIMPLE;
            case 7: return Constant.SPECIAL_NAME;
            case 8: return Constant.ELEGANE_BLUE;
        }
        return "";
    }


}
