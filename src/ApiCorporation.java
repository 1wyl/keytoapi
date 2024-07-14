import java.io.*;
import java.util.*;

public class ApiCorporation {
    public static void main(String[] args) {
        String mashupFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\mashup.csv";
        String filteredApiTablePath = "C:\\JavaProjects\\KeysToAPIs\\src\\filtered_apitable2.csv";
        String corporationFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\corporation.csv";

        // 读取mashup表和filtered_apitable2表
        List<String[]> mashupData = readCSV(mashupFilePath);
        List<String[]> apiTableData = readCSV(filteredApiTablePath);

        // 创建合作次数的映射表
        Map<String, Integer> corporationMap = createCorporationMap(mashupData);

        // 创建新的corporation表
        List<String[]> corporationTable = createCorporationTable(apiTableData, corporationMap);

        // 将corporation表写入文件
        writeCSV(corporationFilePath, corporationTable);
    }

    // 读取CSV文件
    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // 创建合作次数的映射表
    private static Map<String, Integer> createCorporationMap(List<String[]> mashupData) {
        Map<String, Integer> corporationMap = new HashMap<>();

        for (String[] row : mashupData) {
            Set<String> apiSet = new HashSet<>();

            // 将mashup表中的api组合添加到集合中
            for (int i = 1; i < row.length; i++) {
                apiSet.add(row[i]);
            }

            // 计算并更新合作次数
            for (String api1 : apiSet) {
                for (String api2 : apiSet) {
                    if (!api1.equals(api2)) {
                        String key = api1 + "," + api2;
                        int count = corporationMap.getOrDefault(key, 0);
                        corporationMap.put(key, count + 1);
                    }
                }
            }
        }

        return corporationMap;
    }

    // 创建新的corporation表
    private static List<String[]> createCorporationTable(List<String[]> apiTableData, Map<String, Integer> corporationMap) {
        List<String[]> corporationTable = new ArrayList<>();

        // 添加表头
        corporationTable.add(new String[]{"apinum1", "apinum2", "coNum"});

        for (int i = 0; i < apiTableData.size(); i++) {
            String[] row1 = apiTableData.get(i);

            for (int j = i + 1; j < apiTableData.size(); j++) {
                String[] row2 = apiTableData.get(j);

                String api1 = row1[1];
                String api2 = row2[1];
                String key = api1 + "," + api2;

                if (corporationMap.containsKey(key)) {
                    int coNum = corporationMap.get(key);
                    String[] newRow = {row1[0], row2[0], String.valueOf(coNum)};
                    corporationTable.add(newRow);
                }
            }
        }

        return corporationTable;
    }

    // 写入CSV文件
    private static void writeCSV(String filePath, List<String[]> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
