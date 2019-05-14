import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws java.text.ParseException{
        File inputSpendriaJson = new File("Spendria.json");
        SpendriaJson spendriaJson = parseSpendriaJson(inputSpendriaJson);

        File inputFortuneCityCSV = new File(args[0]);
        List<FortuneCityEntry> fortuneCityEntries = parseFortuneCityCSV(inputFortuneCityCSV);

        // clear out data from the imported Spendria JSON file
        spendriaJson.clearTempData();

        List<String> accountRecord = new LinkedList();
        // KEY is fortuneCityEntry.Category. VALUE is the type of transaction.
        Map<String, Integer> categoryAndTypeRelation = new HashMap();
        // KEY is fortuneCityEntry.Category. VALUE is the ID of category.
        Map<String, String> categoryAndIDRelation = new HashMap();
        final int INCOME = 2;
        final int EXPENSE = 1;
        for (SpendriaCategory category : spendriaJson.getCategories()) {
            categoryAndTypeRelation.put(category.title, category.transaction_type);
            categoryAndIDRelation.put(category.title,category.id);
        }

        int sort_order = 7;
        for (FortuneCityEntry fortuneCityEntry : fortuneCityEntries) {
            // if the added account is never seen before
            // add a new account
            if (!accountRecord.contains(fortuneCityEntry.Account)) {
                accountRecord.add(fortuneCityEntry.Account);

                SpendriaAccount account = new SpendriaAccount();
                account.setId(generateRandomHexString());
                account.setModel_state(1);
                account.setSync_state(1);
                account.setCurrency_code("TWD");
                account.setTitle(fortuneCityEntry.Account);
                account.setNote("");
                account.setBalance(0);
                account.setInclude_in_totals(true);
                spendriaJson.addAccount(account);
            }

            // if the category is never seen before
            // add note and its category to noteAndCategoryRelation
            if (!categoryAndTypeRelation.containsKey(fortuneCityEntry.Category)) {
                System.out.println("Category \"" + fortuneCityEntry.Category + "\" is never seen before");
                System.out.println("Press 1 to set this category to EXPENSE");
                System.out.println("Press 2 to set this category to INCOME");
                Scanner scanner = new Scanner(System.in);
                int transactionType;
                do {
                    transactionType = scanner.nextInt();
                } while (!(transactionType == INCOME || transactionType == EXPENSE));

                SpendriaCategory category = new SpendriaCategory();
                category.id = generateRandomHexString();
                category.model_state = 1;
                category.sync_state = 1;
                category.title = fortuneCityEntry.Category;
                category.color = -12627531;
                category.transaction_type = transactionType;
                category.sort_order = sort_order;
                sort_order++;
                spendriaJson.addCategory(category);
                categoryAndTypeRelation.put(category.title, category.transaction_type);
                categoryAndIDRelation.put(category.title, category.id);
            }

            // start putting transaction data in
            SpendriaTransaction transaction = new SpendriaTransaction();
            transaction.id = generateRandomHexString();
            transaction.model_state = 1;
            transaction.sync_state = 1;
            //System.out.println(transaction.id);

            transaction.amount = fortuneCityEntry.Amount * 100;
            switch (categoryAndTypeRelation.get(fortuneCityEntry.Category)) {
                case INCOME:
                    spendriaJson.getAccounts().get(0).addBalance(transaction.amount);
                    transaction.account_from_id = spendriaJson.getAccounts().get(0).id;
                    transaction.account_from_id = null;
                    transaction.transaction_type = INCOME;
                case EXPENSE:
                    spendriaJson.getAccounts().get(0).addBalance(-transaction.amount);
                    transaction.account_from_id = null;
                    transaction.account_from_id = spendriaJson.getAccounts().get(0).id;
                    transaction.transaction_type = EXPENSE;
            }

            transaction.category_id = categoryAndIDRelation.get(fortuneCityEntry.Category);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = sdf.parse(fortuneCityEntry.getRecordedAt());
            transaction.date = date.getTime();
            transaction.date_modified = date.getTime();
            transaction.exchange_rate = 1.0;
            transaction.note = fortuneCityEntry.note;
            transaction.transaction_state = 1;
            transaction.include_in_reports = true;

            spendriaJson.addTransaction(transaction);
        }

        //System.out.println(spendriaJson.getTransactions().size());
        File outputSpendriaJson = new File(args[1]);
        writeSpendriaJson(spendriaJson, outputSpendriaJson);
    }

    private static SpendriaJson parseSpendriaJson(File inputFile) {
        SpendriaJson spendriaJson = new SpendriaJson();
        Gson gson = new Gson();
        Reader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8);
            br = new BufferedReader(reader);
            spendriaJson = gson.fromJson(br, SpendriaJson.class);
            //System.out.println(gson.toJson(spendriaObject));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return spendriaJson;
    }

    private static List<FortuneCityEntry> parseFortuneCityCSV(File inputFile) {
        List<FortuneCityEntry> fortuneCityEntries = new LinkedList<>();
        Reader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8);
            br = new BufferedReader(reader);
            CSVReader csvReader = new CSVReaderBuilder(br).withSkipLines(1).build();

            List<String[]> records = csvReader.readAll();
            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                if (record.length < 7)
                    continue;
                FortuneCityEntry fortuneCityEntry = new FortuneCityEntry();
                fortuneCityEntry.setAccount(record[0]);
                fortuneCityEntry.setCategory(record[1]);
                fortuneCityEntry.setAmount(Long.parseLong(record[2]));
                fortuneCityEntry.setCurrency(record[3]);
                fortuneCityEntry.setRecordedAt(record[4]);
                fortuneCityEntry.setNote(record[5]);
                fortuneCityEntry.setAccountID(Integer.parseInt(record[6]));
                fortuneCityEntries.add(fortuneCityEntry);
            }
            //System.out.println(fortuneCityEntries.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return fortuneCityEntries;
    }

    private static void writeSpendriaJson(SpendriaJson spendriaJson, File outputFile){
        Writer writer = null;
        BufferedWriter bw = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
            bw = new BufferedWriter(writer);

            Gson gson = new GsonBuilder().serializeNulls().create();
            String output = gson.toJson(spendriaJson);
            bw.write(output);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String generateRandomHexString(){
        int length = 32;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length()<length){
            sb.append(Integer.toHexString(random.nextInt()));
        }
        // "00000000-0000-0000-0000-000000000000"
        // 8-4-4-4-12
        sb.insert(20,'-');
        sb.insert(16,'-');
        sb.insert(12,'-');
        sb.insert(8,'-');
        return sb.toString().substring(0,length+4);
    }
}
