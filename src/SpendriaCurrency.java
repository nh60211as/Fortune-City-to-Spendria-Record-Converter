public class SpendriaCurrency {
    String id;
    int model_state;
    int sync_state;
    String code;
    String symbol;
    int symbol_position;
    String decimal_separator;
    String group_separator;
    int decimal_count;

    public SpendriaCurrency(String id, int model_state, int sync_state, String code, String symbol, int symbol_position, String decimal_separator, String group_separator, int decimal_count) {
        this.id = id;
        this.model_state = model_state;
        this.sync_state = sync_state;
        this.code = code;
        this.symbol = symbol;
        this.symbol_position = symbol_position;
        this.decimal_separator = decimal_separator;
        this.group_separator = group_separator;
        this.decimal_count = decimal_count;
    }
}
