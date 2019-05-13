public class SpendriaAccount {
    String id;
    int model_state;
    int sync_state;
    String currency_code;
    String title;
    String note;

    public String getTitle() {
        return title;
    }

    long balance;
    boolean include_in_totals;

    public SpendriaAccount(){
        id = null;
        model_state = 1;
        sync_state = 1;
        currency_code = "";
        title = "";
        note = "";
        balance = 0;
        include_in_totals = true;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModel_state(int model_state) {
        this.model_state = model_state;
    }

    public void setSync_state(int sync_state) {
        this.sync_state = sync_state;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setBalance(long Balance) {
        this.balance = Balance;
    }

    public void addBalance(long addedBalance) {
        this.balance += addedBalance;
    }

    public void setInclude_in_totals(boolean include_in_totals) {
        this.include_in_totals = include_in_totals;
    }
}
