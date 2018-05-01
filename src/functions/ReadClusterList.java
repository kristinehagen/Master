package functions;

import classes.Input;
import classes.Station;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReadClusterList {

    public static ArrayList<Station> readClusterList(Input input, String filename, int vehicleId) throws IOException {

        ArrayList<Station> cluster = new ArrayList<>();

        FileInputStream excelFile = new FileInputStream(new File(filename));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(input.getNumberOfVehicles()-1);

        for (Row row : datatypeSheet) {
            int stationId = (int) row.getCell(0).getNumericCellValue();

            for (Cell cell : row) {
                if (cell.getColumnIndex() != 0) {
                    int vehicleBelonging = (int) cell.getNumericCellValue();
                    if (vehicleBelonging == vehicleId) {
                        cluster.add(input.getStations().get(stationId));
                    }
                }

            }
        }

        return cluster;

    }
}
