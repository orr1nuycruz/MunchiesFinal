package com.example.john.munchies;
//Harvey
public class CreditCardClass {
    private String creditCardID;
    private String creditCardNumber;
    private String holderName;
    private String date;
    private int cvv;
    private String email;

    public CreditCardClass(){

    }


    public CreditCardClass(String creditCardID, String creditCardNumber, String holderName, String date, int cvv, String email) {
        this.creditCardID = creditCardID;
        this.creditCardNumber = creditCardNumber;
        this.holderName = holderName;
        this.date = date;
        this.cvv = cvv;
        this.email = email;
    }


    public String getCreditCardID() {
        return creditCardID;
    }

    public void setCreditCardID(String creditCardID) {
        this.creditCardID = creditCardID;
    }


    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
