package functions;

import classes.Station;
import main.DrivingTimeMatrix;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ReadDistanceMatrix {

    public static void lookUpDrivingTimes(HashMap<Integer, Station> stations, ArrayList<Integer> stationIDlist) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File("DrivingTimeMatrix.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Row headerRow = datatypeSheet.getRow(0);

        for (Integer origin: stationIDlist){
            for (Row row: datatypeSheet) {
                Double value = row.getCell(0).getNumericCellValue();
                int firstCell =  Integer.valueOf(value.intValue());
                if (firstCell == origin){
                    for (Integer destination: stationIDlist){
                        for (Cell cell: row) {
                        if (headerRow.getCell(cell.getColumnIndex()).getNumericCellValue() == destination) {
                            stations.get(origin).addDistanceToStationHashmap(destination, cell.getNumericCellValue());
                            break;
                        }
                        }
                    }
                    break;
                }
            }
        }
        }
    }

