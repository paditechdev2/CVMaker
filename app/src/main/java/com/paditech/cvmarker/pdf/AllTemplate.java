package com.paditech.cvmarker.pdf;

import android.content.Context;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.PdfWriter;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.PDFUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by USER on 13/6/2016.
 */
public class AllTemplate {

    private Context context;
    private Resume resume;

    public AllTemplate(Context context) {
        this.context = context;
        resume = PDFUtils.setDefaultResume();
    }

    public void setContent(String filePath) {
        SimpleClean simpleClean = new SimpleClean(context, resume);
        RedSimpleStyle redSimpleStyle = new RedSimpleStyle(context, resume);
        ClassicSimple classicSimple = new ClassicSimple(context, resume);
        AttachAvatarSimple avatarSimple = new AttachAvatarSimple(context, resume);
        RedDashLineSimple redDashLineSimple = new RedDashLineSimple(context, resume);
        BlueSimple blueSimple = new BlueSimple(context, resume);
        SpecialName specialName = new SpecialName(context, resume);
        EleganeBlue eleganeBlue = new EleganeBlue(context, resume);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(filePath));

            document.open();
            simpleClean.addPersonal(document);
            simpleClean.addObjective(document);
            simpleClean.addEducation(document);
            simpleClean.addExperience(document);
            simpleClean.addSkills(document);
            simpleClean.addOtherInfo(document);

            document.newPage();

            redSimpleStyle.addPersonal(document);
            redSimpleStyle.addObjective(document);
            redSimpleStyle.addEducation(document);
            redSimpleStyle.addExperience(document);
            redSimpleStyle.addSkills(document);
            redSimpleStyle.addOtherInfo(document);

            document.newPage();
            classicSimple.addPersonal(document);
            classicSimple.addObjective(document);
            classicSimple.addEducation(document);
            classicSimple.addExperience(document);
            classicSimple.addSkills(document);
            classicSimple.addOtherInfo(document);

            document.newPage();
            avatarSimple.addPersonal(document);
            avatarSimple.addObjective(document);
            avatarSimple.addEducation(document);
            avatarSimple.addExperience(document);
            avatarSimple.addSkills(document);
            avatarSimple.addOtherInfo(document);

            document.newPage();
            redDashLineSimple.addContent(document);

            document.newPage();
            blueSimple.addContent(document);

            document.newPage();
            specialName.addContent(document);

            document.newPage();
            eleganeBlue.addContent(document);

            document.close(); // no need to close PDFwriter?

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionConverter e) {
            e.printStackTrace();
        }
    }


}
