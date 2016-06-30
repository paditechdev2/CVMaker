package com.paditech.cvmarker.model;

import java.util.List;

/**
 * Created by USER on 10/6/2016.
 */
public class Resume {
    private Personal personal;
    private List<String> objectiveList;
    private List<WorkExperience> workExperienceList;
    private List<Education> educationList;
    private List<String> skillList;
    private List<OtherInfo> otherInfoList;
    private String name;
    private String dateCreated;
    private String language;
    private int style;

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public List<String> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<String> objectiveList) {
        this.objectiveList = objectiveList;
    }

    public List<WorkExperience> getWorkExperienceList() {
        return workExperienceList;
    }

    public void setWorkExperienceList(List<WorkExperience> workExperienceList) {
        this.workExperienceList = workExperienceList;
    }

    public List<Education> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<Education> educationList) {
        this.educationList = educationList;
    }

    public List<String> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<String> skillList) {
        this.skillList = skillList;
    }

    public List<OtherInfo> getOtherInfoList() {
        return otherInfoList;
    }

    public void setOtherInfoList(List<OtherInfo> otherInfoList) {
        this.otherInfoList = otherInfoList;
    }


}
