public class SpendriaCategory {
    String id;
    int model_state;
    int sync_state;
    String title;
    int color;
    int transaction_type;
    int sort_order;

    public SpendriaCategory(String id, int model_state, int sync_state, String title, int color, int transaction_type, int sort_order) {
        this.id = id;
        this.model_state = model_state;
        this.sync_state = sync_state;
        this.title = title;
        this.color = color;
        this.transaction_type = transaction_type;
        this.sort_order = sort_order;
    }
}
