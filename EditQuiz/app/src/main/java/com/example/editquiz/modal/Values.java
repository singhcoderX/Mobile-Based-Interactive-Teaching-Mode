package com.example.editquiz.modal;

public class Values {
    String RollNo,CORRECT,TOTAL,WRONG;
    public String to_String(){
        String ans ="RollNo: "+RollNo;
        ans+=" Total: "+TOTAL+" Correct: "+CORRECT+" Wrong: "+WRONG+"\n";
        return ans;
    }
    public Values(String RollNo, String CORRECT, String TOTAL, String WRONG) {
        RollNo = RollNo;
        CORRECT = CORRECT;
        TOTAL = TOTAL;
        WRONG = WRONG;
    }
    public Values(){}
    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String RollNo) {
        RollNo = RollNo;
    }

    public String getCORRECT() {
        return CORRECT;
    }

    public void setCORRECT(String CORRECT) {
        CORRECT = CORRECT;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        TOTAL = TOTAL;
    }

    public String getWRONG() {
        return WRONG;
    }

    public void setWRONG(String WRONG) {
        WRONG = WRONG;
    }
}
