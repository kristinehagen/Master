package functions;

import classes.Input;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ReadClusterList {

    public static void readClusterListExcel(Input input, String filename) throws IOException {


        FileInputStream excelFile = new FileInputStream(new File(filename));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(input.getNumberOfVehicles()-1);

        for (Row row : datatypeSheet) {
            int stationId = (int) row.getCell(0).getNumericCellValue();
            for (Cell cell : row) {
                if (cell.getColumnIndex() != 0) {
                    int vehicleBelonging = (int) cell.getNumericCellValue();
                    input.getVehicles().get(vehicleBelonging).addStationToClusterList(input.getStations().get(stationId));
                }

            }
        }

    }


    public static void readClusterListTextFile(Input input, String filename) throws IOException {

        File inputFile = new File(filename);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int vehicleId = element.nextInt();
                int stationId = element.nextInt();
                input.getVehicles().get(vehicleId).addStationToClusterList(input.getStations().get(stationId));
            }
        }
        in.close();

    }

}
