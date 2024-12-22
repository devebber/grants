import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static ArrayList<Grant> parseCSV() throws IOException {
        var grants = new ArrayList<Grant>(); // Список для хранения объектов Grant
        var isFirstRow = true; // Флаг для пропуска заголовка
        List<String[]> rows = null; // Список для хранения строк CSV

        try {
            // Создаем объект CSVReader для чтения файла
            var reader = new CSVReader(new FileReader("Grants.csv"));
            rows = reader.readAll(); // Читаем все строки из файла
        } catch (CsvException e) {
            e.printStackTrace(); // Выводим стек ошибки, если возникла проблема с чтением
        }

        // Проходим по строкам CSV файла
        for (var row : rows) {
            if (isFirstRow) {
                isFirstRow = false; // Пропускаем первую строку с заголовками
                continue;
            }

            // Создаем объект Grant из строки данных
            String companyName = row[0].isEmpty() ? "Unknown Company" : row[0];
            String streetName = row[1].isEmpty() ? "Unknown Street" : row[1];
            double grantAmount = row[2].isEmpty() ? 0.0 : Double.parseDouble(row[2].replace("$", "").replace(",", ""));
            int year = row[3].isEmpty() ? 0 : Integer.parseInt(row[3]);
            String businessType = row[4].isEmpty() ? "Unknown Business Type" : row[4];
            int workplacesQuantity = row[5].isEmpty() ? 0 : Integer.parseInt(row[5]);

            grants.add(new Grant(companyName, streetName, grantAmount, year, businessType, workplacesQuantity));
        }

        return grants; // Возвращаем список объектов Grant
    }
}
