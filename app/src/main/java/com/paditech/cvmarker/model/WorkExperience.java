package com.paditech.cvmarker.model;

import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 10/6/2016.
 */
public class WorkExperience {

    private String companyName;
    private String jobTitle;
    private String jobLocation;
    private String datePeriod;
    private String jobResponsibility;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(String datePeriod) {
        this.datePeriod = datePeriod;
    }

    public String getJobResponsibility() {
        return jobResponsibility;
    }

    public void setJobResponsibility(String jobResponsibility) {
        this.jobResponsibility = jobResponsibility;
    }

    public static boolean expIsEmpty(WorkExperience experience) {
        return experience == null && StringUtils.isEmpty(experience.getDatePeriod()) || StringUtils.isEmpty(experience.getCompanyName())
                || StringUtils.isEmpty(experience.getJobLocation()) || StringUtils.isEmpty(experience.getJobResponsibility()) ||
                StringUtils.isEmpty(experience.getJobTitle());
    }
}
