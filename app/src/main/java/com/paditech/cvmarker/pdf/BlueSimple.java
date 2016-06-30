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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

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
public class BlueSimple {

    private static final BaseColor THEME_COLOR = new BaseColor(17,107,122);
    private static final Font DEFAULT_TEXT = new Font(FontUtil.arialFont(), 11, Font.NORMAL);
    private static final Font EMAIL_TEXT = new Font(FontUtil.arialFont(), 11, Font.UNDERLINE, THEME_COLOR);
    private static final Font DEFAULT_TEXT_BOLD = new Font(FontUtil.arialFont(), 11, Font.BOLD);
    private static final Font DEFAULT_TEXT_ITALIC = new Font(FontUtil.arialFont(), 11, Font.ITALIC);
    private static final Font HEADER_TEXT = new Font(FontUtil.arialFont(), 12, Font.NORMAL);
    private static final Font HEADER_TEXT_BOLD = new Font(FontUtil.arialFont(), 12, Font.BOLD, THEME_COLOR);
    private static final Font TITLE_TEXT_BOLD = new Font(FontUtil.arialFont(), 25, Font.BOLD, new BaseColor(17,107,122));
    private static final Font FIRST_NAME_TEXT_BOLD = new Font(FontUtil.arialFont(), 25, Font.BOLD, new BaseColor(107,113,117));

    private Context context;
    private Resume resume;

    public BlueSimple(Context context, Resume resume) {
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
        PdfPTable table = new PdfPTable(2);

        float[] columnWidths = new float[] {20f, 80f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);

        PdfPCell cell1 = new PdfPCell();
        cell1.setNoWrap(false);
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);


        Paragraph name = new Paragraph();
        name.setFont(TITLE_TEXT_BOLD);
        name.setAlignment(Element.ALIGN_RIGHT);
        String[] fullName = resume.getPersonal().getName().split(" ");
        Phrase firstName = new Phrase(fullName[fullName.length-1], TITLE_TEXT_BOLD);
        String ln = "";
        for(int i=0; i < fullName.length -1; i++) {
            ln += fullName[i] + " ";
        }
        Phrase lastName = new Phrase(ln, FIRST_NAME_TEXT_BOLD);
        name.add(lastName);
        name.add(firstName);

        Personal personal = resume.getPersonal();
        String a = "", p = "", em = "";
        if(!StringUtils.isEmpty(personal.getAddress())) a = personal.getAddress();
        if(!StringUtils.isEmpty(personal.getPhoneNumber())) p = personal.getPhoneNumber();
        if(!StringUtils.isEmpty(personal.getEmail())) em = personal.getEmail();


        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setFont(HEADER_TEXT);

        Phrase addr = new Phrase();
        addr.add(getBullet());
        addr.add("  "+ a+"\n");
        Phrase email = new Phrase();
        Phrase ema = new Phrase("  " +em + "\n", EMAIL_TEXT);
        email.add(getBullet());
        email.add(ema);
        Phrase phone = new Phrase();
        phone.add(getBullet());
        phone.add("  "+ p +"\n");

        paragraph.add(addr);
        paragraph.add(email);
        paragraph.add(phone);


        cell1.addElement(name);
        cell1.addElement(PDFUtils.addBlankLine());
        cell1.addElement(paragraph);
        table.addCell(cell1);



        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(getLineDash());
        cell.setBorder(Rectangle.NO_BORDER);

        table.addCell(cell);



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
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.objective));

        cell.addElement(title);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);

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
        line.addElement(getLineDash());

        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(line);



        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addEducation(Document document) {
        if(resume.getEducationList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.education));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
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
        line.addElement(getLineDash());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addExperience(Document document) {
        if(resume.getWorkExperienceList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.work_experence));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);

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
        line.addElement(getLineDash());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addSkills(Document document) {
        if(resume.getSkillList() == null) return;

        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.skills));

        cell.addElement(title);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);

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
        line.addElement(getLineDash());

        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(line);



        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addOtherInfo(Document document) {
        if(resume.getOtherInfoList()== null) return;
        PdfPTable table = new PdfPTable(2);
        float[] columnWidths = new float[] {30f, 70f};
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph title = new Paragraph();
        title.setPaddingTop(50);
        title.setFont(HEADER_TEXT_BOLD);
        title.add(context.getResources().getString(R.string.custom_section));

        cell.addElement(title);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);

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
        line.addElement(getLineDash());
        table.addCell(line);

        try {
            document.add(PDFUtils.addBlankLine());
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    class CustomDashedLineSeparator extends DottedLineSeparator {
        protected float dash = 0.1f;
        protected float phase = 0.1f;

        public float getDash() {
            return dash;
        }

        public float getPhase() {
            return phase;
        }

        public void setDash(float dash) {
            this.dash = dash;
        }

        public void setPhase(float phase) {
            this.phase = phase;
        }

        public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
            canvas.saveState();
            canvas.setColorStroke(THEME_COLOR);
            canvas.setLineWidth(lineWidth);
            canvas.setLineDash(dash, gap, phase);
            drawLine(canvas, llx, urx, y);
            canvas.restoreState();
        }
    }

    private Chunk getLineDash() {
        CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
        separator.setDash(2);
        separator.setGap(4);
        separator.setLineWidth(2);
        Chunk linebreak = new Chunk(separator);
        return linebreak;
    }

    private Chunk getBullet() {
        Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 5, Font.BOLD, THEME_COLOR);
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);
        return bullet;
    }
}
