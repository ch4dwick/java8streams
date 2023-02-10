package javastreams.models;

import java.math.BigDecimal;

public class SettlementRecord {
    private Retailer retailer;
    private BigDecimal loanAmount = new BigDecimal(0);
    private boolean filterOut = false;
    
    public Retailer getRetailer() {
        return retailer;
    }
    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    public void setFilterOut(boolean filterOut) {
        this.filterOut = filterOut;
    }
    public boolean isFilterOut() {
        return filterOut;
    }
    
}
