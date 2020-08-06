package com.example.miniquiz.modal;

public class Resultt {
    public String TotalNo,CorrectNo,WrongNo;

    public Resultt(String totalNo, String correctNo, String wrongNo) {
        TotalNo = totalNo;
        CorrectNo = correctNo;
        WrongNo = wrongNo;
    }

    public String getTotalNo() {
        return TotalNo;
    }

    public void setTotalNo(String totalNo) {
        TotalNo = totalNo;
    }

    public String getCorrectNo() {
        return CorrectNo;
    }

    public void setCorrectNo(String correctNo) {
        CorrectNo = correctNo;
    }

    public String getWrongNo() {
        return WrongNo;
    }

    public void setWrongNo(String wrongNo) {
        WrongNo = wrongNo;
    }
}
