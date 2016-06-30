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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.FontUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 13/6/2016.
 */
public class RedSimpleStyle {

    private static final BaseColor THEME_COLOR = new BaseColor(186,6,39);
    private static final Font DEFAULT_TEXT = new Font(FontUtil.arialFont(), 11, Font.NORMAL);
    private static final Font DEFAULT_TEXT_BOLD = new Font(FontUtil.arialFont(), 11, Font.BOLD);
    private static final Font HEADER_TEXT = new Font(FontUtil.arialFont(), 12, Font.NORMAL);
    private static final Font HEADER_TEXT_BOLD = new Font(FontUtil.arialFont(), 12, Font.BOLD, THEME_COLOR);
    private static final Font TITLE_TEXT_BOLD = new Font(FontUtil.aachenbFont(), 20, Font.BOLD, new BaseColor(255,255,255));

    private Context context;
    private Resume resume;

    public RedSimpleStyle(Context context, Resume resume) {
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
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        try {
            table.setWidths(new float[] {45f, 55f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell1 = new PdfPCell();
        cell1.setNoWrap(false);
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(THEME_COLOR);

        Paragraph name = new Paragraph();
        name.setFont(TITLE_TEXT_BOLD);
        name.setAlignment(Element.ALIGN_CENTER);
        name.add(resume.getPersonal().getName());
        cell1.addElement(name);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setNoWrap(false);
        cell2.setBorder(Rectangle.NO_BORDER);

        Personal personal = resume.getPersonal();
        String a = "", p = "", em = "";
        if(!StringUtils.isEmpty(personal.getAddress())) a = personal.getAddress();
        if(!StringUtils.isEmpty(personal.getPhoneNumber())) p = personal.getPhoneNumber();
        if(!StringUtils.isEmpty(personal.getEmail())) em = personal.getEmail();


        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setFont(HEADER_TEXT);
        paragraph.add(a+"\n");
        paragraph.add(em+"\n");
        paragraph.add(p);

        cell2.addElement(paragraph);
        table.addCell(cell2);
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public void addObjective(Document document) {

        if(resume.getObjectiveList() == null) return;

        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {10f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        cell.setPaddingBottom(5);


        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.objective));

        cell.addElement(title);

        PdfPCell cell1 = new PdfPCell(new Paragraph("  "));
        cell1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);

        List unorderedList = new List(List.UNORDERED);
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        unorderedList.setListSymbol(bullet);

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

        table.addCell(cell);
        table.addCell(cell1);
        table.addCell(cell2);



        try {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addEducation(Document document) {
        if(resume.getEducationList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {10f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        cell.setPaddingBottom(5);


        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.education));

        cell.addElement(title);
        table.addCell(cell);

        for(int i=0; i<resume.getEducationList().size(); i++) {
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(Rectangle.NO_BORDER);

            Paragraph name = new Paragraph();
            Phrase p1 = new Phrase(resume.getEducationList().get(i).getSchoolName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase p2 = new Phrase(resume.getEducationList().get(i).getLocation(), DEFAULT_TEXT);
            name.add(p1);
            name.add(p2);

            Paragraph date = new Paragraph();
            date.setFont(DEFAULT_TEXT_BOLD);
            date.add(resume.getEducationList().get(i).getDatePeriod() + ": ");

            Paragraph locate = new Paragraph();
            locate.setFont(DEFAULT_TEXT);
            locate.add(resume.getEducationList().get(i).getLocation() + " \n");

            Paragraph degree = new Paragraph();
            degree.setFont(DEFAULT_TEXT);
            degree.add(resume.getEducationList().get(i).getDegree());

            Paragraph major = new Paragraph();
            major.setFont(DEFAULT_TEXT);
            major.add(resume.getEducationList().get(i).getMajor());

            cell1.addElement(date);
            cell1.addElement(degree);

            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.addElement(name);
            cell2.addElement(major);

            table.addCell(cell1);
            table.addCell(cell2);
        }

       /* PdfPCell cell1 = new PdfPCell(new Paragraph("  "));
        cell1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        */

        try {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addExperience(Document document) {
        if(resume.getWorkExperienceList() == null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {10f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        cell.setPaddingBottom(5);


        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.work_experence));

        cell.addElement(title);
        table.addCell(cell);

        for(int i= 0; i< resume.getWorkExperienceList().size(); i++) {
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(Rectangle.NO_BORDER);

            Paragraph name = new Paragraph();
            Phrase p1 = new Phrase(resume.getWorkExperienceList().get(i).getCompanyName() + " - ", DEFAULT_TEXT_BOLD);
            Phrase p2 = new Phrase(resume.getWorkExperienceList().get(i).getJobLocation(), DEFAULT_TEXT);
            name.add(p1);
            name.add(p2);

            Paragraph date = new Paragraph();
            date.setFont(DEFAULT_TEXT_BOLD);
            date.add(resume.getWorkExperienceList().get(i).getDatePeriod() + ": ");

            Paragraph degree = new Paragraph();
            degree.setFont(DEFAULT_TEXT);
            degree.add(resume.getWorkExperienceList().get(i).getJobTitle());

            Paragraph major = new Paragraph();
            major.setFont(DEFAULT_TEXT);
            major.add(resume.getWorkExperienceList().get(i).getJobResponsibility());

            cell1.addElement(date);
            cell1.addElement(degree);

            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.addElement(name);
            cell2.addElement(major);

            table.addCell(cell1);
            table.addCell(cell2);
        }

       /* PdfPCell cell1 = new PdfPCell(new Paragraph("  "));
        cell1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        */

        try {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addSkills(Document document) {
        if(resume.getSkillList() == null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {10f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        cell.setPaddingBottom(5);


        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.skills));

        cell.addElement(title);

        PdfPCell cell1 = new PdfPCell(new Paragraph("  "));
        cell1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);

        List unorderedList = new List(List.UNORDERED);
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        unorderedList.setListSymbol(bullet);
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

        table.addCell(cell);
        table.addCell(cell1);
        table.addCell(cell2);

        try {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addOtherInfo(Document document) {
        if(resume.getOtherInfoList() == null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {10f, 20f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        cell.setPaddingBottom(5);


        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.custom_section));

        cell.addElement(title);
        table.addCell(cell);

        for(int i=0; i<resume.getOtherInfoList().size(); i++) {
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(Rectangle.NO_BORDER);

            Paragraph date = new Paragraph();
            date.setFont(DEFAULT_TEXT_BOLD);
            date.add(resume.getOtherInfoList().get(i).getSectionName() + ": ");

            Paragraph major = new Paragraph();
            major.setFont(DEFAULT_TEXT);
            major.add(resume.getOtherInfoList().get(i).getSectionDesc());

            cell1.addElement(date);

            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.addElement(major);

            table.addCell(cell1);
            table.addCell(cell2);
        }

        try {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
