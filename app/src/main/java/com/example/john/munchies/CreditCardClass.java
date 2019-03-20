package com.example.john.munchies;

public class CreditCardClass {

    private String creditCardNumber;
    private String holderName;
    private String date;
    private String cvv;
    private String email;

    public CreditCardClass( String creditCardNumber, String holderName, String date, String cvv, String email) {
        this.creditCardNumber = creditCardNumber;
        this.holderName = holderName;
        this.date = date;
        this.cvv = cvv;
        this.email = email;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
