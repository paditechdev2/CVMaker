package com.paditech.cvmarker.model;


import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 10/6/2016.
 */
public class Education {

    private String schoolName;
    private String location;
    private String degree;
    private String major;
    private String datePeriod;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(String datePeriod) {
        this.datePeriod = datePeriod;
    }

    public static boolean educationIsEmpty(Education education) {
        return education == null || StringUtils.isEmpty(education.getDatePeriod()) || StringUtils.isEmpty(education.getDegree())
        || StringUtils.isEmpty(education.getLocation()) || StringUtils.isEmpty(education.getMajor()) ||
        StringUtils.isEmpty(education.getSchoolName());
    }
}
