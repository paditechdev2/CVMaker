package com.paditech.cvmarker.model;

import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 10/6/2016.
 */
public class Personal {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static boolean personalIsEmpty(Personal personal) {
        return personal == null || (StringUtils.isEmpty(personal.getName()) && StringUtils.isEmpty(personal.getAddress())
        && StringUtils.isEmpty(personal.getEmail()) && StringUtils.isEmpty(personal.getPhoneNumber()));
    }
}
