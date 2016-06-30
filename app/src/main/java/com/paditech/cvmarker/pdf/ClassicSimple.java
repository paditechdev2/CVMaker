package com.paditech.cvmarker.pdf;

import android.content.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.model.OtherInfo;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.model.WorkExperience;
import com.paditech.cvmarker.utils.FileUtil;
import com.paditech.cvmarker.utils.FontUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 13/6/2016.
 */
public class ClassicSimple {

    private static final Font DEFAULT_TEXT = new Font(FontUtil.arialFont(), 11, Font.NORMAL);
    private static final Font DEFAULT_TEXT_BOLD = new Font(FontUtil.arialFont(), 11, Font.BOLD);
    private static final Font HEADER_TEXT_BOLD = new Font(FontUtil.tahomaFont(), 12, Font.BOLD);
    private static final Font TITLE_TEXT_BOLD = new Font(FontUtil.tahomaFont(), 20, Font.BOLD);
    private static final Font EMAIL_TEXT_LINK = new Font(FontUtil.arialFont(), 11, Font.UNDERLINE, BaseColor.BLUE);


    private Context context;
    private Resume resume;

    public ClassicSimple(Context context, Resume resume) {
        this.context = context;
        this.resume = resume;
    }

    public void setContent(String filePath) {
        Document document = new Document();
        if(resume == null) return;
        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(filePath));

            document.open();
            addPersonal(document);
            addObjective(document);
            addEducation(document);
            addExperience(document);
            addSkills(document);
            addOtherInfo(document);
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

    public void addPersonal(Document document) {
        if(resume.getPersonal() == null) return;
        Personal personal = resume.getPersonal();
        Paragraph name = new Paragraph(personal.getName(), TITLE_TEXT_BOLD);
        name.setAlignment(Element.ALIGN_CENTER);

        String a = "", p = "", em = "";
        if(!StringUtils.isEmpty(personal.getAddress())) a = personal.getAddress();
        if(!StringUtils.isEmpty(personal.getPhoneNumber())) p = personal.getPhoneNumber();
        if(!StringUtils.isEmpty(personal.getEmail())) em = personal.getEmail();


        Paragraph addr = new Paragraph();
        addr.setAlignment(Element.ALIGN_CENTER);
        addr.setFont(DEFAULT_TEXT);
        addr.add("\n");
        addr.add(a+"\n");
        addr.add(p);

        Paragraph email = new Paragraph(em, EMAIL_TEXT_LINK);
        email.setAlignment(Element.ALIGN_CENTER);

        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setPercentage(80);
        line.setLineWidth(2);

        email.add(PDFUtils.addBlankLine());
        email.add(line);
        email.add(PDFUtils.addBlankLine());
        try {
            document.add(name);
            document.add(addr);
            document.add(email);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addObjective(Document document) {

        if(resume.getObjectiveList() == null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.objective), HEADER_TEXT_BOLD );
        title.setIndentationLeft(55);

        Paragraph content = new Paragraph();
        content.setIndentationLeft(70);
        content.setFont(DEFAULT_TEXT);

        List unorderedList = new List(List.UNORDERED);
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        unorderedList.setListSymbol(bullet);
        unorderedList.setIndentationLeft(100);

        for (int i=0 ; i < resume.getObjectiveList().size(); i++)
        {
            String tx = resume.getObjectiveList().get(i);
            String rs[] = tx.split("\n");
            if(rs.length == 1) {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + resume.getObjectiveList().get(i));
                unorderedList.add(item);
            } else {
                for(int k =0; k<rs.length; k++) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + rs[k]);
                    unorderedList.add(item);
                }
            }
        }

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(title);
            document.add(unorderedList);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addEducation(Document document) {
        if(resume.getEducationList()== null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.education), HEADER_TEXT_BOLD );
        title.setIndentationLeft(55);

        Paragraph paragraph = new Paragraph();
        paragraph.setIndentationLeft(100);

        for (int i=0; i<resume.getEducationList().size(); i++) {
            Education education = resume.getEducationList().get(i);
            Paragraph date = new Paragraph(education.getDatePeriod() + ": " + education.getDegree() +"   -   ", DEFAULT_TEXT_BOLD);
            Phrase name = new Phrase();
            Phrase phrase = new Phrase(education.getSchoolName()+", ", DEFAULT_TEXT_BOLD);
            Phrase phrase1 = new Phrase(education.getLocation(), DEFAULT_TEXT);
            name.add(phrase);
            name.add(phrase1);


            List unorderedList = new List(List.UNORDERED);
            Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
            Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
            unorderedList.setListSymbol(bullet);
            unorderedList.setIndentationLeft(20);

            String[] majors = education.getMajor().split("\n");
            for(int k=0; k<majors.length; k++) {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }
            date.add(name);
            date.add(unorderedList);
            paragraph.add(date);
            paragraph.add(PDFUtils.addBlankLine());
        }

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(title);
            document.add(paragraph);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addExperience(Document document) {
        if(resume.getWorkExperienceList()== null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.work_experence), HEADER_TEXT_BOLD );
        title.setIndentationLeft(55);

        Paragraph paragraph = new Paragraph();
        paragraph.setIndentationLeft(100);

        for (int i=0; i<resume.getWorkExperienceList().size(); i++) {
            WorkExperience experience = resume.getWorkExperienceList().get(i);
            Paragraph date = new Paragraph(experience.getDatePeriod() + ": " + experience.getJobTitle() +"   -   ", DEFAULT_TEXT_BOLD);
            Phrase name = new Phrase();
            Phrase phrase = new Phrase(experience.getCompanyName()+", ", DEFAULT_TEXT_BOLD);
            Phrase phrase1 = new Phrase(experience.getJobLocation(), DEFAULT_TEXT);
            name.add(phrase);
            name.add(phrase1);

            List unorderedList = new List(List.UNORDERED);
            Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
            Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
            unorderedList.setListSymbol(bullet);
            unorderedList.setIndentationLeft(20);

            String[] majors = experience.getJobResponsibility().split("\n");
            for(int k=0; k<majors.length; k++) {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }
            date.add(name);
            date.add(unorderedList);
            paragraph.add(date);
            paragraph.add(PDFUtils.addBlankLine());
        }

        try {
            document.add(title);
            document.add(paragraph);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addSkills(Document document) {
        if(resume.getSkillList() == null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.skills), HEADER_TEXT_BOLD );
        title.setIndentationLeft(55);

        Paragraph content = new Paragraph();
        content.setIndentationLeft(70);
        content.setFont(DEFAULT_TEXT);

        List unorderedList = new List(List.UNORDERED);
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        unorderedList.setListSymbol(bullet);
        unorderedList.setIndentationLeft(100);

        for (int i=0 ; i < resume.getSkillList().size(); i++)
        {
            String tx = resume.getSkillList().get(i);
            String rs[] = tx.split("\n");
            if(rs.length == 1) {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + resume.getSkillList().get(i));
                unorderedList.add(item);
            } else {
                for(int k =0; k<rs.length; k++) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + rs[k]);
                    unorderedList.add(item);
                }
            }
        }

        try {
            document.add(title);
            document.add(unorderedList);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addOtherInfo(Document document) {
        if(resume.getOtherInfoList()== null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.custom_section), HEADER_TEXT_BOLD );
        title.setIndentationLeft(55);

        Paragraph paragraph = new Paragraph();
        paragraph.setIndentationLeft(100);

        for (int i=0; i<resume.getOtherInfoList().size(); i++) {
            OtherInfo experience = resume.getOtherInfoList().get(i);
            Paragraph date = new Paragraph(experience.getSectionName() + ": " , DEFAULT_TEXT_BOLD);

            List unorderedList = new List(List.UNORDERED);
            Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
            Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
            unorderedList.setListSymbol(bullet);
            unorderedList.setIndentationLeft(20);

            String[] majors = experience.getSectionDesc().split("\n");
            for(int k=0; k<majors.length; k++) {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }
            date.add(unorderedList);
            paragraph.add(date);
            paragraph.add(PDFUtils.addBlankLine());
        }

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(title);
            document.add(paragraph);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
