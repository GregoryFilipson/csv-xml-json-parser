package csvJsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            while (reader.readNext() != null) {
                ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
                strategy.setType(Employee.class);
                strategy.setColumnMapping(columnMapping);
                CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                        .withMappingStrategy(strategy)
                        .build();
                return csv.parse();
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json) {
        try (FileOutputStream fos = new FileOutputStream("data.json", true)) {
            byte[] bytes = json.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
