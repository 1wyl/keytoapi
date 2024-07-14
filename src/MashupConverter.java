import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MashupConverter {
    public static void main(String[] args) {
        String mashupFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\mashup.csv";
        String filteredApiTablePath = "C:\\JavaProjects\\KeysToAPIs\\src\\filtered_apitable2.csv";
        String singleKeyToAPIsPath = "C:\\JavaProjects\\KeysToAPIs\\src\\singleKeyToAPIs.csv";
        String mashup2FilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\mashup2.csv";

        try {
            // 读取 filtered_apitable2.csv 构建 api 名字和编号的映射
            Map<String, String> apiNameToNumberMap = readMapping(filteredApiTablePath);

            // 读取 singleKeytoAPIs.csv 构建 key 名字和编号的映射
            Map<String, String> keyNameToNumberMap = readMapping(singleKeyToAPIsPath);

            // 处理 mashup.csv 文件，将 api 名字和 key 名字转换为编号
            processMashupFile(mashupFilePath, apiNameToNumberMap, keyNameToNumberMap, mashup2FilePath);

            System.out.println("转换成功：" + mashup2FilePath);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> readMapping(String filePath) throws IOException {
        Map<String, String> mapping = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            for (String[] row : rows.subList(1, rows.size())) {
                mapping.put(row[1], row[0]);
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return mapping;
    }

    private static void processMashupFile(String inputFilePath, Map<String, String> apiMapping,
                                          Map<String, String> keyMapping, String outputFilePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(inputFilePath));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {

            List<String[]> rows = reader.readAll();

            List<String[]> newRows = new LinkedList<>(rows.subList(0, 1));
            for (String[] row : new ArrayList<>(rows.subList(1, rows.size()))) {
                String[] newRow = new String[row.length];
                newRow[0] = row[0]; // 保持第一列不变

// 转换第二列的 api 名字为编号
                String[] apiNames = row[1].split(",");
                StringBuilder apiNumbers = new StringBuilder();
                for (String apiName : apiNames) {
                    if (apiMapping.containsKey(apiName)) {
                        apiNumbers.append(apiMapping.get(apiName)).append(",");
                    }
                }
                newRow[1] = apiNumbers.toString();

// 转换第三列的 key 名字为编号
                String[] keyNames = row[2].split(",");
                StringBuilder keyNumbers = new StringBuilder();
                for (String keyName : keyNames) {
                    if (keyMapping.containsKey(keyName)) {
                        keyNumbers.append(keyMapping.get(keyName)).append(",");
                    }
                }
                newRow[2] = keyNumbers.toString();


                newRows.add(newRow);
            }

            // 写入新的 CSV 文件
            writer.writeAll(newRows);
        }
    }
}
