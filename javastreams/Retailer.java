package javastreams;

public class Retailer {
    private Long id;
    private String storeName;
    private String name;
    private BankDetails bankDetails = new BankDetails();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    

    Retailer (String name) {
        this.name = name;
    }

    Retailer() {

    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    class BankDetails {
        private Long id;
        private String bankName;
        private String accountNo;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getBankName() {
            return bankName;
        }
        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
        public String getAccountNo() {
            return accountNo;
        }
        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }
    }

}
