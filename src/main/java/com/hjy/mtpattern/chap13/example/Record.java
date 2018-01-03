package com.hjy.mtpattern.chap13.example;

import java.util.Date;

/**
 * Created by hjy on 18-1-2.
 */
public class Record {

    private int id;
    private String productId;
    private String packageId;
    private String msisdn;


    private Date operationTime;
    private Date effectiveDate;
    private Date dueDate;

    private int operationType;

    public int targetFileIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Record other = (Record) obj;
        if (id != other.id)
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Record [id=" + id + ", productId=" + productId + ", packageId="
                + packageId + ", msisdn=" + msisdn + ", operationTime=" + operationTime
                + ", effectiveDate=" + effectiveDate + ", dueDate=" + dueDate
                + ", operationType=" + operationType + "]";
    }

}
