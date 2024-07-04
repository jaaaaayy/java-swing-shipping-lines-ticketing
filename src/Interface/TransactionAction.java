package Interface;

import Models.Transaction;
import java.util.List;

public interface TransactionAction {
    public boolean createTransaction(Transaction t);
    public List<Transaction> viewTransaction(int selected);
    public boolean updateTransaction(Transaction t);
    public boolean deleteTransaction(int id);
}
