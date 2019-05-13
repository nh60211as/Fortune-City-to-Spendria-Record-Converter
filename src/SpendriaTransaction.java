import java.util.LinkedList;
import java.util.List;

public class SpendriaTransaction {
    String id;
    int model_state;
    int sync_state;
    String account_from_id;
    String account_to_id;
    String category_id;
    List<String> tag_ids;
    long date;
    long date_modified;
    long amount;
    double exchange_rate;
    String note;
    int transaction_state;
    int transaction_type;
    boolean include_in_reports;

    public SpendriaTransaction(){
        id = null;
        model_state = 1;
        sync_state = 1;
        account_from_id = null;
        account_to_id = null;
        category_id = null;
        tag_ids = new LinkedList();
        date = 0;
        date_modified = 0;
        amount = 0;
        exchange_rate = 1.0;
        note = "";
        transaction_state = 1;
        transaction_type = 1;
        include_in_reports = true;
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

    public void setAccount_from_id(String account_from_id) {
        this.account_from_id = account_from_id;
    }

    public void setAccount_to_id(String account_to_id) {
        this.account_to_id = account_to_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void addTag_ids(String addedTag_ids) {
        tag_ids.add(addedTag_ids);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDate_modified(long date_modified) {
        this.date_modified = date_modified;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTransaction_state(int transaction_state) {
        this.transaction_state = transaction_state;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setInclude_in_reports(boolean include_in_reports) {
        this.include_in_reports = include_in_reports;
    }
}
