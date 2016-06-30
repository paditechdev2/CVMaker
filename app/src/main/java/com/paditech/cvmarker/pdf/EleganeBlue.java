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
import com.paditech.cvmarker.utils.PreferenceUtils;
import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 13/6/2016.
 */
public class EleganeBlue {

    private static final BaseColor THEME_COLOR = new BaseColor(12,135,111);
    private static final Font DEFAULT_TEXT = new Font(FontUtil.arialFont(), 11, Font.NORMAL);
    private static final Font DEFAULT_TEXT_BOLD = new Font(FontUtil.arialFont(), 11, Font.BOLD);
    private static final Font DEFAULT_TEXT_ITALIC = new Font(FontUtil.arialFont(), 11, Font.ITALIC);
    private static final Font HEADER_TEXT_BOLD = new Font(FontUtil.tahomaFont(), 15, Font.NORMAL, THEME_COLOR);
    private static final Font TITLE_TEXT_BOLD = new Font(FontUtil.tahomaFont(), 25, Font.BOLD, new BaseColor(0,0,0));

    private Context context;
    private Resume resume;

    public EleganeBlue(Context context, Resume resume) {
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

    public void addContent(Document document) {
        addPersonal(document);
        addObjective(document);
        addEducation(document);
        addExperience(document);
        addSkills(document);
        addOtherInfo(document);
    }

    public void addPersonal(Document document) {

        if(resume.getPersonal() == null) return;
        PdfPTable table = new PdfPTable(3);

        float[] columnWidths = new float[] {10f, 80f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setLockedWidth(true);

        PdfPCell c1 = new PdfPCell();
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setBackgroundColor(THEME_COLOR);
        c1.setFixedHeight(50);

        PdfPCell c3 = new PdfPCell();
        c3.setBorder(Rectangle.NO_BORDER);
        c3.setBackgroundColor(THEME_COLOR);

        PdfPCell c2 = new PdfPCell();
        c2.setBorder(Rectangle.NO_BORDER);

        Paragraph name = new Paragraph(resume.getPersonal().getName().toUpperCase() +"\n", TITLE_TEXT_BOLD);
        name.setIndentationLeft(30);
        PdfPTable t = new PdfPTable(3);
        t.setWidthPercentage(100);
        try {
            t.setWidths(new int[]{35,20,45});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        t.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        Personal personal = resume.getPersonal();
        String a = "", p = "", em = "";
        if(!StringUtils.isEmpty(personal.getAddress())) a = personal.getAddress();
        if(!StringUtils.isEmpty(personal.getPhoneNumber())) p = personal.getPhoneNumber();
        if(!StringUtils.isEmpty(personal.getEmail())) em = personal.getEmail();


        PdfPCell email = new PdfPCell();
        email.setBorder(Rectangle.RIGHT);
        email.setBorderColor(THEME_COLOR);
        email.setVerticalAlignment(Element.ALIGN_CENTER);
        email.setBorderWidth(2);
        Paragraph phrase = new Paragraph(em, DEFAULT_TEXT_BOLD);
        phrase.setIndentationLeft(30);
        phrase.setAlignment(Element.ALIGN_MIDDLE);
        email.addElement(phrase);

        PdfPCell sdt = new PdfPCell();
        sdt.setBorder(Rectangle.RIGHT);
        sdt.setBorderColor(THEME_COLOR);
        sdt.setVerticalAlignment(Element.ALIGN_CENTER);
        sdt.setBorderWidth(2);
        Paragraph phrase1 = new Paragraph(p, DEFAULT_TEXT_BOLD);
        phrase1.setAlignment(Element.ALIGN_MIDDLE);
        phrase1.setIndentationLeft(10);
        sdt.addElement(phrase1);

        PdfPCell addr = new PdfPCell();
        addr.setVerticalAlignment(Element.ALIGN_CENTER);
        addr.setBorder(Rectangle.NO_BORDER);
        Paragraph phrase2 = new Paragraph(a, DEFAULT_TEXT_BOLD);
        phrase2.setAlignment(Element.ALIGN_MIDDLE);
        phrase2.setIndentationLeft(10);
        addr.addElement(phrase2);

        t.addCell(email);
        t.addCell(sdt);
        t.addCell(addr);

        c2.addElement(name);
        c2.addElement(PDFUtils.addBlankLine());
        c2.addElement(t);


        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);



        try {
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public void addObjective(Document document) {

        if(resume.getObjectiveList() == null) return;

        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {25f, 75f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(2);
        cell.setBorderColor(THEME_COLOR);
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_MIDDLE);
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.objective));

        cell.addElement(title);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPaddingLeft(20);

        List unorderedList = new List(List.UNORDERED);
        unorderedList.setListSymbol(getBullet());

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

        cell2.addElement(unorderedList);

        PdfPCell line = new PdfPCell();
        line.setColspan(2);
        line.setBorder(Rectangle.NO_BORDER);
        line.addElement(PDFUtils.addBlankLine());

        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(line);



        try {
            document.add(PDFUtils.addBlankLine());
            document.add(PDFUtils.addBlankLine());
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addEducation(Document document) {
        if(resume.getEducationList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {25f, 75f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(2);
        cell.setBorderColor(THEME_COLOR);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.education));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPaddingLeft(20);
        cell1.setBorder(Rectangle.NO_BORDER);

        for(int i=0; i<resume.getEducationList().size(); i++) {
            Education education = resume.getEducationList().get(i);
            Paragraph paragraph = new Paragraph();

            Phrase name = new Phrase(education.getSchoolName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase addr = new Phrase(education.getLocation() + "\n", DEFAULT_TEXT);
            Phrase date = new Phrase(education.getDatePeriod() + " - ", DEFAULT_TEXT_ITALIC);
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

            cell1.addElement(paragraph);

        }

        table.addCell(cell1);
        PdfPCell line = new PdfPCell();
        line.setBorder(Rectangle.NO_BORDER);
        line.setColspan(2);
        line.addElement(PDFUtils.addBlankLine());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addExperience(Document document) {
        if(resume.getWorkExperienceList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {25f, 75f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(2);
        cell.setBorderColor(THEME_COLOR);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.work_experence));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setPaddingLeft(20);

        for(int i=0; i<resume.getWorkExperienceList().size(); i++) {
            WorkExperience education = resume.getWorkExperienceList().get(i);
            Paragraph paragraph = new Paragraph();

            Phrase name = new Phrase(education.getCompanyName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase addr = new Phrase(education.getJobLocation() + "\n", DEFAULT_TEXT);
            Phrase date = new Phrase(education.getDatePeriod() + " - ", DEFAULT_TEXT_ITALIC);
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

            cell1.addElement(paragraph);

        }

        table.addCell(cell1);
        PdfPCell line = new PdfPCell();
        line.setBorder(Rectangle.NO_BORDER);
        line.setColspan(2);
        line.addElement(PDFUtils.addBlankLine());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addSkills(Document document) {
        if(resume.getSkillList() == null) return;

        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {25f, 75f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(2);
        cell.setBorderColor(THEME_COLOR);
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_MIDDLE);
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.skills));

        cell.addElement(title);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPaddingLeft(20);

        List unorderedList = new List(List.UNORDERED);
        unorderedList.setListSymbol(getBullet());

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

        cell2.addElement(unorderedList);

        PdfPCell line = new PdfPCell();
        line.setColspan(2);
        line.setBorder(Rectangle.NO_BORDER);
        line.addElement(PDFUtils.addBlankLine());

        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(line);



        try {
            document.add(PDFUtils.addBlankLine());
            document.add(PDFUtils.addBlankLine());
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addOtherInfo(Document document) {
        if(resume.getOtherInfoList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {25f, 75f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(2);
        cell.setBorderColor(THEME_COLOR);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.custom_section));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setPaddingLeft(20);

        for(int i=0; i<resume.getOtherInfoList().size(); i++) {
            OtherInfo education = resume.getOtherInfoList().get(i);
            Paragraph paragraph = new Paragraph();

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

            cell1.addElement(paragraph);

        }

        table.addCell(cell1);
        PdfPCell line = new PdfPCell();
        line.setBorder(Rectangle.NO_BORDER);
        line.setColspan(2);
        line.addElement(PDFUtils.addBlankLine());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
            document.add(PDFUtils.addBlankLine());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private Chunk getBullet() {
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5, Font.BOLD, THEME_COLOR);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        return bullet;
    }
}
