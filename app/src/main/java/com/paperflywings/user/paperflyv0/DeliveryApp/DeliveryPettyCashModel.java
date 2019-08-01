package com.paperflywings.user.paperflyv0.DeliveryApp;

public class DeliveryPettyCashModel {
    private String id;
    private String emp_code;
    private String recv_amount;
    private String recv_comment;
    private String recv_date;
    private String recv_by;
    private String expense;
    private String expense_purpose;
    private String expense_comment;
    private String expense_date;
    private String expense_by;
    private String purposeId;
    private String purpose;

    public DeliveryPettyCashModel(String purposeId, String purpose) {
        this.purposeId = purposeId;
        this.purpose = purpose;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    private int openingBalance;

    public int getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(int openingBalance) {
        this.openingBalance = openingBalance;
    }

    public int getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(int closingBalance) {
        this.closingBalance = closingBalance;
    }

    private int closingBalance;

    public DeliveryPettyCashModel(String id, String emp_code, String recv_amount, String recv_comment, String recv_date, String recv_by, String expense, String expense_purpose, String expense_comment, String expense_date, String expense_by) {
      this.id = id;
      this.emp_code = emp_code;
      this.recv_amount = recv_amount;
      this.recv_comment = recv_comment;
      this.recv_date = recv_date;
      this.recv_by = recv_by;
      this.expense = expense;
      this.expense_purpose = expense_purpose;
      this.expense_comment = expense_comment;
      this.expense_date = expense_date;
      this.expense_by = expense_by;
    }

    public DeliveryPettyCashModel(int openingBalance, int closingBalance) {
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getRecv_amount() {
        return recv_amount;
    }

    public void setRecv_amount(String recv_amount) {
        this.recv_amount = recv_amount;
    }

    public String getRecv_comment() {
        return recv_comment;
    }

    public void setRecv_comment(String recv_comment) {
        this.recv_comment = recv_comment;
    }

    public String getRecv_date() {
        return recv_date;
    }

    public void setRecv_date(String recv_date) {
        this.recv_date = recv_date;
    }

    public String getRecv_by() {
        return recv_by;
    }

    public void setRecv_by(String recv_by) {
        this.recv_by = recv_by;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getExpense_purpose() {
        return expense_purpose;
    }

    public void setExpense_purpose(String expense_purpose) {
        this.expense_purpose = expense_purpose;
    }

    public String getExpense_comment() {
        return expense_comment;
    }

    public void setExpense_comment(String expense_comment) {
        this.expense_comment = expense_comment;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public String getExpense_by() {
        return expense_by;
    }

    public void setExpense_by(String expense_by) {
        this.expense_by = expense_by;
    }


}


