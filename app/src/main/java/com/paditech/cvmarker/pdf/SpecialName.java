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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.model.OtherInfo;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.model.WorkExperience;
import com.paditech.cvmarker.utils.FontUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/**
 * Created by USER on 13/6/2016.
 */
public class SpecialName {

    private static final BaseColor THEME_COLOR = new BaseColor(24,84,40);
    private static final Font DEFAULT_TEXT = new Font(FontUtil.timesFont(), 11, Font.NORMAL);
    private static final Font EMAIL_TEXT = new Font(FontUtil.timesFont(), 11, Font.UNDERLINE);
    private static final Font DEFAULT_TEXT_BOLD = new Font(FontUtil.timesFont(), 11, Font.BOLD);
    private static final Font HEADER_TEXT_BOLD = new Font(FontUtil.timesFont(), 15, Font.BOLD, THEME_COLOR);
    private static final Font FIRST_CHAR_NAME = new Font(FontUtil.timesFont(), 50, Font.BOLD, new BaseColor(255,255,255));

    private static final int DEFAULT_PADDING = 15;
    private static final int DEFAULT_CONTENT_PADDING = 25;

    private Context context;
    private Resume resume;

    public SpecialName(Context context, Resume resume) {
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
            addContent(document);
            document.close(); // no need to close PDFwriter?

        } catch (DocumentException | ExceptionConverter | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addContent(Document document) {
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.RIGHT);
        cell1.setBorderColor(THEME_COLOR);
        cell1.setBorderWidth(3);
        cell1.setFixedHeight(PageSize.A4.getHeight());
        addPersonal(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        addObjective(cell2);
        addEducation(cell2);
        addExperience(cell2);
        addSkills(cell2);
        addOtherInfo(cell2);


        table.addCell(cell1);
        table.addCell(cell2);

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        /*addPersonal(document);
        addObjective(document);
        addEducation(document);
        addExperience(document);
        addSkills(document);
        addOtherInfo(document);*/
    }

    public void addPersonal(PdfPCell cell) {

        if(resume.getPersonal() == null) return;

        PdfPTable table = new PdfPTable(1);
        try {
            table.setWidths(new int[]{60});
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPCell cell1 = new PdfPCell();
        cell1.setFixedHeight(120);
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setBackgroundColor(THEME_COLOR);
        cell1.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        String name = resume.getPersonal().getName();
        String len[] = name.split(" ");
        String firstChar = String.valueOf(len[len.length - 1].charAt(0));
        Paragraph phrase = new Paragraph(firstChar, FIRST_CHAR_NAME);
        phrase.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(phrase);

        table.addCell(cell1);

        Paragraph fullName = new Paragraph(name, HEADER_TEXT_BOLD);
        fullName.setAlignment(Element.ALIGN_RIGHT);
        fullName.setIndentationRight(DEFAULT_PADDING);

        Personal personal = resume.getPersonal();
        String a = "", p = "", em = "";
        if(!StringUtils.isEmpty(personal.getAddress())) a = personal.getAddress();
        if(!StringUtils.isEmpty(personal.getPhoneNumber())) p = personal.getPhoneNumber();
        if(!StringUtils.isEmpty(personal.getEmail())) em = personal.getEmail();

        Phrase addr = new Phrase(a + "\n", DEFAULT_TEXT);
        Phrase email = new Phrase(em + "\n", EMAIL_TEXT);
        Phrase phone = new Phrase(p + "\n", DEFAULT_TEXT);

        Paragraph detail = new Paragraph();
        detail.setAlignment(Element.ALIGN_RIGHT);
        detail.setIndentationRight(DEFAULT_PADDING);
        detail.add(PDFUtils.addBlankLine());
        detail.add(addr);
        detail.add(email);
        detail.add(phone);

        cell.addElement(table);
        cell.addElement(PDFUtils.addBlankLine());
        cell.addElement(fullName);
        cell.addElement(detail);

    }

    public void addObjective(PdfPCell cell) {

        if(resume.getObjectiveList() == null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.objective), HEADER_TEXT_BOLD);
        title.setIndentationLeft(DEFAULT_PADDING);

        if(resume.getObjectiveList().size() == 1) {

        } else {
            int haft = resume.getObjectiveList().size() / 2;

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(0f);
            table.setSpacingBefore(0f);

            PdfPCell c1 = new PdfPCell();
            c1.setBorder(Rectangle.NO_BORDER);
            PdfPCell c2 = new PdfPCell();
            c2.setBorder(Rectangle.NO_BORDER);

            List list1 = new List(List.UNORDERED);
            list1.setListSymbol(getBullet());
            list1.setIndentationLeft(DEFAULT_CONTENT_PADDING);

            List list2 = new List(List.UNORDERED);
            list2.setListSymbol(getBullet());
            list2.setIndentationLeft(10);

            for(int i=0; i<= haft; i++) {
                String tx = resume.getObjectiveList().get(i);
                String rs[] = tx.split("\n");
                if(rs.length == 1) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + resume.getObjectiveList().get(i));
                    list1.add(item);
                } else {
                    for(int k =0; k<rs.length; k++) {
                        ListItem item = new ListItem();
                        item.setFont(DEFAULT_TEXT);
                        item.add("   " + rs[k]);
                        list1.add(item);
                    }
                }
            }

            for (int k= haft + 1; k< resume.getObjectiveList().size(); k++) {
                String tx = resume.getObjectiveList().get(k);
                String rs[] = tx.split("\n");
                if(rs.length == 1) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + resume.getObjectiveList().get(k));
                    list2.add(item);
                } else {
                    for(int j =0; j<rs.length; j++) {
                        ListItem item = new ListItem();
                        item.setFont(DEFAULT_TEXT);
                        item.add("   " + rs[j]);
                        list2.add(item);
                    }
                }
            }

            c1.addElement(list1);
            c2.addElement(list2);

            table.addCell(c1);
            table.addCell(c2);

            cell.addElement(title);
            cell.addElement(table);

        }
    }

    public void addEducation(PdfPCell cell) {
        if(resume.getEducationList() == null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.education), HEADER_TEXT_BOLD);
        title.setIndentationLeft(DEFAULT_PADDING);

        cell.addElement(PDFUtils.addBlankLine());
        cell.addElement(title);

        for(int i=0; i<resume.getEducationList().size(); i++) {
            Education education = resume.getEducationList().get(i);
            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(DEFAULT_CONTENT_PADDING);

            Phrase name = new Phrase(education.getSchoolName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase addr = new Phrase(education.getLocation() + "\n", DEFAULT_TEXT);
            Phrase date = new Phrase(education.getDatePeriod() + " - ", DEFAULT_TEXT_BOLD);
            Phrase degree = new Phrase(education.getDegree() + "\n", DEFAULT_TEXT);

            String majors[] = education.getMajor().split("\n");

            List unorderedList = new List(List.UNORDERED);
            unorderedList.setIndentationLeft(20);
            unorderedList.setListSymbol(getBullet());

            for (int k=0 ; k < majors.length; k++)
            {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }

            paragraph.add(name);
            paragraph.add(addr);
            paragraph.add(date);
            paragraph.add(degree);
            paragraph.add(unorderedList);

            cell.addElement(PDFUtils.addBlankLine());
            cell.addElement(paragraph);

        }
    }

    public void addExperience(PdfPCell cell) {
        if(resume.getWorkExperienceList()== null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.work_experence), HEADER_TEXT_BOLD);
        title.setIndentationLeft(DEFAULT_PADDING);

        cell.addElement(PDFUtils.addBlankLine());
        cell.addElement(title);

        for(int i=0; i<resume.getWorkExperienceList().size(); i++) {
            WorkExperience education = resume.getWorkExperienceList().get(i);
            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(DEFAULT_CONTENT_PADDING);

            Phrase name = new Phrase(education.getCompanyName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase addr = new Phrase(education.getJobLocation() + "\n", DEFAULT_TEXT);
            Phrase date = new Phrase(education.getDatePeriod() + " - ", DEFAULT_TEXT_BOLD);
            Phrase degree = new Phrase(education.getJobTitle() + "\n", DEFAULT_TEXT);

            String majors[] = education.getJobResponsibility().split("\n");

            List unorderedList = new List(List.UNORDERED);
            unorderedList.setIndentationLeft(20);
            unorderedList.setListSymbol(getBullet());

            for (int k=0 ; k < majors.length; k++)
            {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }

            paragraph.add(name);
            paragraph.add(addr);
            paragraph.add(date);
            paragraph.add(degree);
            paragraph.add(unorderedList);

            cell.addElement(PDFUtils.addBlankLine());
            cell.addElement(paragraph);


        }
    }

    public void addSkills(PdfPCell cell) {
        if(resume.getSkillList() == null) return;

        Paragraph title = new Paragraph(context.getResources().getString(R.string.skills), HEADER_TEXT_BOLD);
        title.setIndentationLeft(DEFAULT_PADDING);

        if(resume.getSkillList().size() == 1) {

        } else {
            int haft = resume.getSkillList().size() / 2;

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(0f);
            table.setSpacingBefore(0f);

            PdfPCell c1 = new PdfPCell();
            c1.setBorder(Rectangle.NO_BORDER);
            PdfPCell c2 = new PdfPCell();
            c2.setBorder(Rectangle.NO_BORDER);

            List list1 = new List(List.UNORDERED);
            list1.setListSymbol(getBullet());
            list1.setIndentationLeft(DEFAULT_CONTENT_PADDING);

            List list2 = new List(List.UNORDERED);
            list2.setListSymbol(getBullet());
            list2.setIndentationLeft(10);

            for(int i=0; i<= haft; i++) {
                String tx = resume.getSkillList().get(i);
                String rs[] = tx.split("\n");
                if(rs.length == 1) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + resume.getSkillList().get(i));
                    list1.add(item);
                } else {
                    for(int k =0; k<rs.length; k++) {
                        ListItem item = new ListItem();
                        item.setFont(DEFAULT_TEXT);
                        item.add("   " + rs[k]);
                        list1.add(item);
                    }
                }
            }

            for (int k= haft + 1; k< resume.getSkillList().size(); k++) {
                String tx = resume.getSkillList().get(k);
                String rs[] = tx.split("\n");
                if(rs.length == 1) {
                    ListItem item = new ListItem();
                    item.setFont(DEFAULT_TEXT);
                    item.add("   " + resume.getSkillList().get(k));
                    list2.add(item);
                } else {
                    for(int j =0; j<rs.length; j++) {
                        ListItem item = new ListItem();
                        item.setFont(DEFAULT_TEXT);
                        item.add("   " + rs[j]);
                        list2.add(item);
                    }
                }
            }

            c1.addElement(list1);
            c2.addElement(list2);

            table.addCell(c1);
            table.addCell(c2);

            cell.addElement(PDFUtils.addBlankLine());
            cell.addElement(title);
            cell.addElement(table);

        }
    }

    public void addOtherInfo(PdfPCell cell) {
        if(resume.getOtherInfoList()== null) return;
        Paragraph title = new Paragraph(context.getResources().getString(R.string.custom_section), HEADER_TEXT_BOLD);
        title.setIndentationLeft(DEFAULT_PADDING);

        cell.addElement(PDFUtils.addBlankLine());
        cell.addElement(title);

        for(int i=0; i<resume.getOtherInfoList().size(); i++) {
            OtherInfo education = resume.getOtherInfoList().get(i);
            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(DEFAULT_CONTENT_PADDING);

            Phrase name = new Phrase(education.getSectionName() + "\n", DEFAULT_TEXT_BOLD);
            String majors[] = education.getSectionDesc().split("\n");

            List unorderedList = new List(List.UNORDERED);
            unorderedList.setIndentationLeft(20);
            unorderedList.setListSymbol(getBullet());

            for (int k=0 ; k < majors.length; k++)
            {
                ListItem item = new ListItem();
                item.setFont(DEFAULT_TEXT);
                item.add("   " + majors[k]);
                unorderedList.add(item);
            }

            paragraph.add(name);
            paragraph.add(unorderedList);

            cell.addElement(PDFUtils.addBlankLine());
            cell.addElement(paragraph);
        }
    }


    private Chunk getBullet() {
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5, Font.BOLD, THEME_COLOR);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        return bullet;
    }
}
