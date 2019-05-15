import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class readFiles {

    public static SpendriaJson readSpendriaJson(File inputFile) {
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

    public static Map<String, String> readTranslationFile(File inputFile) {
        Map<String, String> translationEntries = new HashMap();
        Reader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8);
            br = new BufferedReader(reader);
            CSVReader csvReader = new CSVReaderBuilder(br).withSkipLines(0).build();

            List<String[]> records = csvReader.readAll();
            Iterator<String[]> iterator = records.iterator();

            while (iterator.hasNext()) {
                String[] record = iterator.next();
                if (record.length != 2)
                    continue;
                translationEntries.put(record[0], record[1]);
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
        return translationEntries;
    }
}
