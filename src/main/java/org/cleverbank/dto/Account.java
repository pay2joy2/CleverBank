package org.cleverbank.dto;

import lombok.*;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
//@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private int id;
    private double balance = 0;
    private int bank_id;
    private int user_id;

   ReentrantLock lock = new ReentrantLock();

    public Account(int id, double amount, int user_id, int bank_id)
    {
        this.id = id;
        this.balance = amount;
        this.user_id = user_id;
        this.bank_id = bank_id;
    }

    public Account(int id, int user_id, int bank_id)
    {
        this.id = id;
        this.balance = 0;
        this.user_id = user_id;
        this.bank_id = bank_id;
    }

    public void lock()
    {
        lock.lock();
    }

    public void unlock()
    {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", bank_id=" + bank_id +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Double.compare(balance, account.balance) == 0 && bank_id == account.bank_id && user_id == account.user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, bank_id, user_id);
    }
}