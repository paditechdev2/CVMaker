package com.paditech.cvmarker.model;


import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 10/6/2016.
 */
public class OtherInfo {

    private String sectionName;
    private String sectionDesc;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionDesc() {
        return sectionDesc;
    }

    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
    }

    public static boolean othersIsEmpty(OtherInfo otherInfo) {
        return otherInfo == null || StringUtils.isEmpty(otherInfo.getSectionName())
                || StringUtils.isEmpty(otherInfo.getSectionDesc());
    }
}
