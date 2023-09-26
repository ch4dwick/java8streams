package javastreams;

import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collector;

import javastreams.models.Retailer;
import javastreams.models.SettlementFile;
import javastreams.models.SettlementRecord;
import javastreams.models.SettlementSummary;

/**
 * Advanced Streaming by aggregating object data.
 */
public class StreamBasics2 {
    static Logger logger = Logger.getLogger(StreamBasics2.class.getName());
    public static void main(String ...args) {
        logger.info("Advanced object processing");
        SettlementFile file = initRecords();

        // Change this to affect aggregation behavior.
        boolean isStrict = false;
        boolean sorted = true;

        final List<SettlementSummary> summaries = file.getSettlementRecords()
            // Step 1
            .stream() // Step 2
            // Filter out non-special retailers or not.
            // One interesting feature of this lambda is that you
            // can get two data sets simply by passing isStrict = false
            // without the need to create a separate stream without a filter
            .filter(sr -> (isStrict) ? sr.isFilterOut() : !sr.isFilterOut()) // Step 3
            /**
             * Step 3
             * The first parameter of the group will be the key inthe resulting HashMap. Group by bank if special. 
             * Group by retailer id for regular.
             */
            .collect(
                // Step 4a. First, we designate the account number as the grouping key.
                groupingByConcurrent(sr -> sr.getRetailer().getBankDetails().getAccountNo(), 
                Collector.of(SettlementSummary::new, // Step 4b. Create a new Collector defining:, 4c The target object (SettlementSummary)
                StreamBasics2::myAccumulator,
                /** 
                 * Step 4d. 
                 * The accumulator or the intermediate logic that maps the SettlementRecord data to the SettlementSummary 
                 * object then adds all theloan amounts (the first two mapping may seem redundant in a loop but Java uses 
                 * this to identify related records). In other scenarios, this is used to map/transform one object to another. 
                 * It is important to remember that at this point SettlementSummary addition in this part are for records sharing 
                 * the same account number.
                 */
                StreamBasics2::myCombiner))
                ) // Step 4e. The combiner that sums up all the loan amounts in SettlementSummary objects from the accumulator.
                /** Step 5.
                 * The last four lines converts the result to a collection, convert it back to a stream so we can sort 
                 * the output (note that we need to create our own sorting logic since this is a more complex object), 
                 * then generate the list again. (The first collect() call creates a ConcurrentMap<> result which we 
                 * don’t want in this use case).
                 */
            .values() // This line onwards converts to List.
            .stream()
            .sorted((a,b) -> (sorted) ? a.getRetailer().compareTo(b.getRetailer()) : 0)
            .collect(toList());

        summaries.forEach(r -> {
            System.out.println("Retailer: " + r.getRetailer() 
            + ", Store: " + r.getStore() 
            + ", Account No: " + r.getBank().getAccountNo()
            + ", Bank: " + r.getBank().getBankName()
            + ", Loan Amount: " + r.getBank().getLoanAmount().toString());
        });
   
    }
   
    // Intermediate operator for partial results
    public static SettlementSummary myAccumulator(SettlementSummary ss, SettlementRecord sr) {
        ss.getBank().setAccountNo(sr.getRetailer().getBankDetails().getAccountNo());
        ss.getBank().setBankName(sr.getRetailer().getBankDetails().getBankName());
        ss.getBank().setLoanAmount(ss.getBank().getLoanAmount().add(sr.getLoanAmount()));
        ss.setRetailer(sr.getRetailer().getName());
        ss.setStore(sr.getRetailer().getStoreName());
        return ss;
    }
   
    // Combine all the results
    public static SettlementSummary myCombiner(SettlementSummary src,SettlementSummary dest) {
        dest.getBank().setLoanAmount(dest.getBank().getLoanAmount().add(src.getBank().getLoanAmount()));
        return dest;
    }
   
    /**
     * Mock data initialization.
     * @return SettlementFile pre-populated data.
     */
    public static SettlementFile initRecords() {
        // Structure 1. Same retailer multiple entries
        SettlementFile file = new SettlementFile();

        Retailer r = new Retailer("Retailer 1");
        r.setId(1l);
        r.getBankDetails().setId(1l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("122230000887");
        SettlementRecord rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(1000.00));
        file.getSettlementRecords().add(rec);
    
    
        r = new Retailer("Retailer 1");
        r.setId(1l);
        r.getBankDetails().setId(1l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("122230000887");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(2000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 1");
        r.setId(1l);
        r.getBankDetails().setId(1l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("112230005407");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(1000.00));
        file.getSettlementRecords().add(rec);
    
    
        r = new Retailer("Retailer 1");
        r.setId(1l);
        r.getBankDetails().setId(1l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("101230005895");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(1000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 1");
        r.setId(1l);
        r.getBankDetails().setId(1l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("112230005407");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(1000.00));
        file.getSettlementRecords().add(rec);
    
        // Structure 2. Retailers flagged as strict and different banks
        r = new Retailer("Retailer 2");
        r.setId(2l);
        r.getBankDetails().setId(2l);
        r.setStoreName("Store 02");
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("0002");
        rec = new SettlementRecord();
        rec.setFilterOut(true);
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(2000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 2");
        r.setId(2l);
        r.getBankDetails().setId(3l);
        r.setStoreName("Store 03");
        r.getBankDetails().setBankName("Bank 2");
        r.getBankDetails().setAccountNo("0003");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setFilterOut(true);
        rec.setLoanAmount(BigDecimal.valueOf(2000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 2");
        r.setId(2l);
        r.getBankDetails().setId(2l);
        r.getBankDetails().setBankName("Bank 1");
        r.getBankDetails().setAccountNo("309200004767");
        r.setStoreName("Store 02");
        rec = new SettlementRecord();
        rec.setFilterOut(true);
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(2000.00));
        file.getSettlementRecords().add(rec);
    
        // Structure 3. Another Routine entry.
        r = new Retailer("Retailer 3");
        r.setId(3l);
        r.getBankDetails().setId(4l);
        r.getBankDetails().setBankName("Bank 3");
        r.getBankDetails().setAccountNo("0004");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(3000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 3");
        r.setId(3l);
        r.getBankDetails().setId(4l);
        r.getBankDetails().setBankName("Bank 3");
        r.getBankDetails().setAccountNo("0004");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(3000.00));
        file.getSettlementRecords().add(rec);
    
        // Structure 4. Different retailer same bank accounts
        r = new Retailer("Retailer 3");
        r.setId(3l);
        r.getBankDetails().setId(4l);
        r.getBankDetails().setBankName("Bank 3");
        r.getBankDetails().setAccountNo("309200004767");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(3000.00));
        file.getSettlementRecords().add(rec);
    
        r = new Retailer("Retailer 3");
        r.setId(3l);
        r.getBankDetails().setId(4l);
        r.getBankDetails().setBankName("Bank 3");
        r.getBankDetails().setAccountNo("0005");
        rec = new SettlementRecord();
        rec.setRetailer(r);
        rec.setLoanAmount(BigDecimal.valueOf(3000.00));
        file.getSettlementRecords().add(rec);
    
        return file;
    }
}