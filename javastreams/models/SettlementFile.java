package javastreams.models;

import java.util.ArrayList;
import java.util.List;

public class SettlementFile {
    private List<SettlementRecord> settlementRecords = new ArrayList<>();

    public List<SettlementRecord> getSettlementRecords() {
        return settlementRecords;
    }

    public void setSettlementRecords(List<SettlementRecord> settlementRecords) {
        this.settlementRecords = settlementRecords;
    }
}
