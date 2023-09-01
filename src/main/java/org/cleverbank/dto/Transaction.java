package org.cleverbank.dto;

import lombok.*;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private UUID uuid;
    private double amount;
    private int fromAccount;
    private int toAccount;
    private String bankFrom;
    private String bankTo;
    private String type;
    private Date datestamp;
    private Time timestamp;

    public Transaction(UUID uuid, int account_id, int counterparty_account_id, double amount, String type, Date datestamp, Time timestamp, String bankFrom)
    {
        this.uuid = uuid;
        this.fromAccount = account_id;
        this.toAccount = counterparty_account_id;
        this.amount = amount;
        this.type = type;
        this.datestamp = datestamp;
        this.timestamp = timestamp;
        this.bankFrom = bankFrom;
        this.bankTo = bankTo;
    }

    //FOR LIST OF TRANSACTIONS IN STATEMENT SERVICE
    public Transaction(java.sql.Date date, String string, double Double, int fromAccount, int toAccount) {
        this.datestamp = date;
        this.type = string;
        this.amount = Double;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;

    }

    @Override
    public String toString() {
        return "Transaction{" +
                "uuid=" + uuid +
                ", amount=" + amount +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", bankFrom='" + bankFrom + '\'' +
                ", bankTo='" + bankTo + '\'' +
                ", type='" + type + '\'' +
                ", datestamp=" + datestamp +
                ", timestamp=" + timestamp +
                '}';
    }
}
