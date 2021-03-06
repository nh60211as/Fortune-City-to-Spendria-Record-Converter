import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static List<String> usedIDString = new LinkedList();

    public static void main(String[] args) throws java.text.ParseException {
        final String SpendriaTemplateFileName = "resource/SpendriaTemplate.json";
        final String FortuneCityInputFileName = args[0];
        final String languageCode = args[1];
        final String SpendriaOutputFileName = args[2];

        // read Spendria template
        SpendriaJson spendriaJson = readFiles.readSpendriaJson(new File(SpendriaTemplateFileName));
        // clear out data from the imported Spendria JSON file
        // clear out tags, accounts, and transactions record
        spendriaJson.clearTempData();
        usedIDString.addAll(spendriaJson.getAllUsedIDString());

        // Apply translation
        String translationFolder = "resource/translation/%s.csv";
        //String[] supportedLanguages = {"chT", "eng", "chS", "jpn", "kor"};
        Map<String, String> translationMap = readFiles.readTranslationFile(new File(String.format(translationFolder, languageCode)));
        System.out.printf("The selected language is %s.\n",translationMap.get("$LANGUAGE"));
        spendriaJson.replaceCategoriesTitle(translationMap);

        // read all the Entries from Fortune City
        List<FortuneCityEntry> fortuneCityEntries = parseFortuneCityCSV(new File(FortuneCityInputFileName));

        // KEY is fortuneCityEntry.Category. VALUE is the type of transaction.
        Map<String, Integer> categoryAndTypeRelation = new HashMap();
        // KEY is fortuneCityEntry.Category. VALUE is the ID of category.
        Map<String, String> categoryAndIDRelation = new HashMap();
        for (SpendriaCategory category : spendriaJson.getCategories()) {
            categoryAndTypeRelation.put(category.title, category.transaction_type);
            categoryAndIDRelation.put(category.title, category.id);
        }

        final int INCOME = 2;
        final int EXPENSE = 1;
        int sort_order = spendriaJson.getCategories().size();
        for (FortuneCityEntry fortuneCityEntry : fortuneCityEntries) {
            // if the added account is never seen before
            // add a new account
            if (!spendriaJson.containsAccount(fortuneCityEntry.Account)) {
                spendriaJson.addAccount(new SpendriaAccount(generateRandomHexString(), 1, 1, fortuneCityEntry.Currency, fortuneCityEntry.Account, "", 0, true));

                if (!spendriaJson.containsCurrencyCode(fortuneCityEntry.Currency)) {
                    spendriaJson.addCurrency(new SpendriaCurrency(generateRandomHexString(), 1, 1, fortuneCityEntry.Currency, "$", 2, ".", ",", 2));
                    System.out.printf("Currency code %s not found. Added it to json file\n", fortuneCityEntry.Currency);
                    System.out.println("Make sure to edit the currency detail in Spendria.");
                }
            }

            // if the category is never seen before
            // add category and its type to categoryAndTypeRelation
            // add category and its id   to categoryAndIDRelation
            if (!categoryAndTypeRelation.containsKey(fortuneCityEntry.Category)) {
                System.out.printf("Category \"%s\" is never seen before\n", fortuneCityEntry.Category);
                System.out.println("Press 1 to set this category to EXPENSE");
                System.out.println("Press 2 to set this category to INCOME");

                Scanner scanner = new Scanner(System.in);
                int transactionType;
                do {
                    transactionType = scanner.nextInt();
                } while (!(transactionType == INCOME || transactionType == EXPENSE));

                SpendriaCategory category = new SpendriaCategory(generateRandomHexString(), 1, 1, fortuneCityEntry.Category, -12627531, transactionType, sort_order);
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

            transaction.amount = (long) (fortuneCityEntry.Amount * Math.pow(10, spendriaJson.getCurrencyDecimalCount(fortuneCityEntry.Currency)));
            switch (categoryAndTypeRelation.get(fortuneCityEntry.Category)) {
                case INCOME:
                    spendriaJson.addAccountBalance(fortuneCityEntry.Account, transaction.amount);
                    transaction.account_to_id = spendriaJson.getAccountID(fortuneCityEntry.Account);
                    transaction.account_from_id = null;
                    transaction.transaction_type = INCOME;
                    break;
                case EXPENSE:
                    spendriaJson.addAccountBalance(fortuneCityEntry.Account, -transaction.amount);
                    transaction.account_to_id = null;
                    transaction.account_from_id = spendriaJson.getAccountID(fortuneCityEntry.Account);
                    transaction.transaction_type = EXPENSE;
                    break;
                default:
                    System.out.println("Error occurred when determining transaction type.");
                    System.out.println(categoryAndTypeRelation.get(fortuneCityEntry.Category));
                    System.out.println(spendriaJson.toString());
                    return;
                //break;
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
        File outputSpendriaJson = new File(SpendriaOutputFileName);
        writeSpendriaJson(spendriaJson, outputSpendriaJson);
        System.out.printf("JSON file generated to %s\n",outputSpendriaJson.getAbsolutePath());
        System.out.println("Import it into Spendria to transfer the record.");
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

    private static void writeSpendriaJson(SpendriaJson spendriaJson, File outputFile) {
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

    private static String generateRandomHexString() {
        String output;
        do {
            int length = 32;
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            while (sb.length() < length) {
                sb.append(Integer.toHexString(random.nextInt()));
            }
            // "00000000-0000-0000-0000-000000000000"
            // 8-4-4-4-12
            sb.insert(20, '-');
            sb.insert(16, '-');
            sb.insert(12, '-');
            sb.insert(8, '-');
            output = sb.toString().substring(0, length + 4);
        } while (usedIDString.contains(output));
        usedIDString.add(output);
        return output;
    }
}
