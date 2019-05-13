public class FortuneCityEntry {
    String Account;
    String Category;
    long Amount;
    String Currency;
    String RecordedAt;
    String note;
    int AccountID;

    public void setAccount(String account) {
        Account = account;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setAmount(long amount) {
        Amount = amount;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public void setRecordedAt(String recordedAt) {
        RecordedAt = recordedAt;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public String getRecordedAt() {
        return RecordedAt;
    }

    public String getCategory() {
        return Category;
    }
}
