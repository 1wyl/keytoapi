//import com.opencsv.CSVReader;
//import com.opencsv.CSVWriter;
//import com.opencsv.exceptions.CsvException;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class KeyToAPIConverter {
//    public static void main(String[] args) {
//        // 文件路径
//        String inputFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\filtered_apitable2.csv";
//        String outputFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\singleKeytoAPIs.csv";
//
//        try {
//            // 读取原始CSV文件
//            CSVReader reader = new CSVReader(new FileReader(inputFilePath));
//            List<String[]> originalRows = reader.readAll();
//
//            // 处理原始数据，构建关键词到API的映射
//            Map<String, List<String>> keyToAPIsMap = new HashMap<>();
//            for (String[] row : originalRows.subList(1, originalRows.size())) {
//                String[] keys = row[2].split(",");
//                for (String key : keys) {
//                    keyToAPIsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(row[0]);
//                }
//            }
//
//            // 创建新的CSV文件并写入数据
//            CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
//            List<String[]> newRows = new ArrayList<>();
//
//            // 添加标题行
//            newRows.add(new String[]{"keyNum", "singleKey", "apis"});
//
//            // 遍历关键词到API的映射，生成新的行
//            int keyNum = 1;
//            for (Map.Entry<String, List<String>> entry : keyToAPIsMap.entrySet()) {
//                String singleKey = entry.getKey();
//                String apis = String.join(",", entry.getValue());
//
//                newRows.add(new String[]{String.valueOf(keyNum), singleKey, apis});
//                keyNum++;
//            }
//
//            // 写入数据到新的CSV文件
//            writer.writeAll(newRows);
//            writer.close();
//
//            System.out.println("生成成功：" + outputFilePath);
//        } catch (IOException | CsvException e) {
//            e.printStackTrace();
//        }
//    }
//}
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyToAPIConverter {
    public static void main(String[] args) {
        // 文件路径
        String inputFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\filtered_apitable2.csv";
        String outputFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\singleKeytoAPIs.csv";

        try {
            // 读取原始CSV文件
            CSVReader reader = new CSVReader(new FileReader(inputFilePath));
            List<String[]> originalRows = reader.readAll();

            // 处理原始数据，构建关键词到API的映射
            Map<String, List<String>> keyToAPIsMap = new HashMap<>();
            for (String[] row : originalRows.subList(1, originalRows.size())) {
                String[] keys = row[2].split(",");
                for (String key : keys) {
                    keyToAPIsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(row[0]);
                }
            }

            // 创建新的CSV文件并写入数据
            CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
            List<String[]> newRows = new ArrayList<>();

            // 添加标题行
            newRows.add(new String[]{"keyNum", "singleKey", "apis"});

            // 遍历关键词到API的映射，生成新的行
            int keyNum = 1;
            for (Map.Entry<String, List<String>> entry : keyToAPIsMap.entrySet()) {
                String singleKey = entry.getKey();
                List<String> apisList = entry.getValue();

                // 将每个API写入同一行的不同单元格
                String[] newRow = new String[apisList.size() + 2];  // 2 表示 keyNum 和 singleKey 的单元格
                newRow[0] = String.valueOf(keyNum);
                newRow[1] = singleKey;

                for (int i = 0; i < apisList.size(); i++) {
                    newRow[i + 2] = apisList.get(i);
                }

                newRows.add(newRow);
                keyNum++;
            }

            // 写入数据到新的CSV文件
            writer.writeAll(newRows);
            writer.close();

            System.out.println("生成成功：" + outputFilePath);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
