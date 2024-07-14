import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class filterapi {
    public static void main(String[] args) {
        String mashupFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\mashup.csv";
        String apiTableFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\apitable2.csv";
        String filteredApiTableFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\filtered_apitable2.csv";

        try {
            // 读取 mashup.csv 文件，提取第二列的所有不同的 API 名字
            Set<String> apiNamesInMashup = extractApiNames(mashupFilePath);

            // 读取 apitable2.csv 文件，过滤符合条件的行写入 filtered_apitable3.csv 文件
            filterApiTable(apiTableFilePath, filteredApiTableFilePath, apiNamesInMashup);

            System.out.println("filtered_apitable2.csv 文件已生成！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> extractApiNames(String mashupFilePath) throws IOException, CsvException {
        Set<String> apiNames = new HashSet<>();
        try (CSVReader reader = new CSVReader(new FileReader(mashupFilePath))) {
            List<String[]> rows = reader.readAll();
            // 从第二行开始迭代，提取第二列的 API 名字
            for (String[] row : rows.subList(1, rows.size())) {
                String[] apiNamesArray = row[1].split(",");
                for (String apiName : apiNamesArray) {
                    apiNames.add(apiName.trim());
                }
            }
        }
        return apiNames;
    }

    private static void filterApiTable(String apiTableFilePath, String filteredApiTableFilePath, Set<String> apiNamesInMashup) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(apiTableFilePath));
             CSVWriter writer = new CSVWriter(new FileWriter(filteredApiTableFilePath))) {

            List<String[]> rows = reader.readAll();
            List<String[]> newRows = rows.subList(0, 1);

            // 临时列表，用于存储需要添加到 newRows 中的行
            List<String[]> tempRows = new ArrayList<>();

            // 从第二行开始迭代，检查 API 名字是否在 mashup.csv 中出现过
            for (String[] row : rows.subList(1, rows.size())) {
                if (apiNamesInMashup.contains(row[1].trim())) {
                    tempRows.add(row);
                }
            }

            // 将临时列表中的行添加到 newRows 中
            newRows.addAll(tempRows);

            // 写入新的 CSV 文件
            writer.writeAll(newRows);
        }
    }
}
