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

    public void clearTempData() {
        tags = new LinkedList();
        accounts = new LinkedList<>();
        transactions = new LinkedList();
    }

    public void addCurrency(SpendriaCurrency currency) {
        currencies.add(currency);
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

    public boolean containsAccount(String inputAccount) {
        for (SpendriaAccount account : accounts)
            if (account.title.contentEquals(inputAccount)) {
                return true;
            }
        return false;
    }

    public boolean addAccountBalance(String inputAccount, long addedBalance) {
        for (SpendriaAccount account : accounts)
            if (account.title.contentEquals(inputAccount)) {
                account.addBalance(addedBalance);
                return true;
            }
        return false;
    }

    public String getAccountID(String inputAccount) {
        for (SpendriaAccount account : accounts)
            if (account.title.contentEquals(inputAccount)) {
                return account.id;
            }
        return null;
    }

    public boolean containsCurrencyCode(String inputCode) {
        for (SpendriaCurrency currency : currencies)
            if (currency.code.contentEquals(inputCode)) {
                return true;
            }
        return false;
    }

    public int getCurrencyDecimalCount(String inputCode){
        for (SpendriaCurrency currency : currencies)
            if (currency.code.contentEquals(inputCode)) {
                return currency.decimal_count;
            }
        return 0;
    }
}
