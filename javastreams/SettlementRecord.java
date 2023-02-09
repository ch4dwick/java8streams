package javastreams;

import java.math.BigDecimal;

public class SettlementRecord {
    private Retailer retailer;
    private BigDecimal loanAmount = new BigDecimal(0);
    public boolean filterOut = false;
    
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
    public boolean isFilterOut() {
        return filterOut;
    }
    
}
