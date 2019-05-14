import java.util.LinkedList;
import java.util.List;

public class SpendriaJson {
    int version;
    long timestamp;
    private List<SpendriaCurrency> currencies;
    private List<SpendriaCategory> categories;
    private List<SpendriaTag> tags;
    private List<SpendriaAccount> accounts;
    private List<SpendriaTransaction> transactions;

    public SpendriaJson() {
        version = 9;
        timestamp = 1557555414002L;
    }

    public void clearTempData() {
        tags = new LinkedList();
        accounts = new LinkedList<>();
        transactions = new LinkedList();
    }

    public void addCategory(SpendriaCategory category) {
        categories.add(category);
    }

    public void addAccount(SpendriaAccount account) {
        accounts.add(account);
    }

    public void addTransaction(SpendriaTransaction transaction) {
        transactions.add(transaction);
    }

    public List<SpendriaCategory> getCategories() {
        return categories;
    }

    public List<SpendriaAccount> getAccounts() {
        return accounts;
    }

    public List<SpendriaTransaction> getTransactions() {
        return transactions;
    }
}
