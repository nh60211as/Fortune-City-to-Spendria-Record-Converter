public class SpendriaAccount {
    String id;
    int model_state;
    int sync_state;
    String currency_code;
    String title;
    String note;
    long balance;
    boolean include_in_totals;

    public SpendriaAccount(String id, int model_state, int sync_state, String currency_code, String title, String note, long balance, boolean include_in_totals) {
        this.id = id;
        this.model_state = model_state;
        this.sync_state = sync_state;
        this.currency_code = currency_code;
        this.title = title;
        this.note = note;
        this.balance = balance;
        this.include_in_totals = include_in_totals;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public void addBalance(long addedBalance) {
        this.balance += addedBalance;
    }
}
