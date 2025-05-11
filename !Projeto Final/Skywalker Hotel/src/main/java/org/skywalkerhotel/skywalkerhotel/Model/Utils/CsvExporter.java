package org.skywalkerhotel.skywalkerhotel.Model.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void export(String filePath, List<String[]> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                writer.append(String.join(",", row)).append("\n");
            }
        }
    }
}
