package com.como.service.receiveapprequests;

public class User {
    private String username;
    private String password;
    private String title;
    private String firstName;
    private String lastName;
    private String mobile;
    private String mobileCountryCode;
    private String email;
    private int redemptionPoints;

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getTitle(){
        return title;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getMobile(){
        return mobile;
    }

    public String getMobileCountryCode(){
        return mobileCountryCode;
    }

    public String getEmail(){
        return email;
    }

    public int getRedemptionPoints(){
        return redemptionPoints;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public void setMobileCountryCode(String mobileCountryCode){
        this.mobileCountryCode = mobileCountryCode;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setRedemptionPoints(int redemptionPoints){
        this.redemptionPoints = redemptionPoints;
    }
}
