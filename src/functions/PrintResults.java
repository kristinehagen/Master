package functions;

import classes.Input;
import enums.SolutionMethod;
import javafx.scene.control.Cell;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class PrintResults {

    public static void printSimulationResultsToExcelFile(double averageViolation, double averagePercentageVoilation, double sdViolations, double sdPercentageViolation,
                                                       double averageNumberOfTimesVehicleRouteGenerated, double avergageTimeToVehicleRouteGenerated,
                                                       double avergaeComputationalTimeXpress, double averageComputationalTimeXpressPlussInitialization, Input input) throws IOException {

        System.out.println("avergaeComputationalTimeXpress: " + avergaeComputationalTimeXpress);
        System.out.println("averageComputationalTimeXpressPlussInitialization" + averageComputationalTimeXpressPlussInitialization);


        //Read the spreadsheet that needs to be updated
        FileInputStream fileInputStream= new FileInputStream(new File("Results.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        int rowNumber = 0;
        boolean findEmptyRow = true;

        while (findEmptyRow) {
            if (worksheet.getRow(rowNumber) == null) {
                findEmptyRow = false;
            } else {
                rowNumber ++;
            }
        }


        //Print output to file
        Row rowOutput = worksheet.createRow(rowNumber);



        //Objective function
        rowOutput.createCell(4).setCellValue(input.getWeightViolation());
        rowOutput.createCell(5).setCellValue(input.getWeightDeviation());
        rowOutput.createCell(6).setCellValue(input.getWeightReward());
        rowOutput.createCell(7).setCellValue(input.getWeightDeviationReward());
        rowOutput.createCell(8).setCellValue(input.getWeightDrivingTimePenalty());

        //Loading interval
        rowOutput.createCell(9).setCellValue(input.getMinLoad());
        rowOutput.createCell(10).setCellValue(input.getMaxLoad());

        //Input
        rowOutput.createCell(11).setCellValue(input.getSolutionMethod().toString());
        if (!input.getSolutionMethod().equals(SolutionMethod.NO_VEHICLES)) {
            rowOutput.createCell(12).setCellValue(input.getReOptimizationMethod().toString());
            rowOutput.createCell(18).setCellValue(input.getNumberOfVehicles());
            rowOutput.createCell(13).setCellValue(input.getMaxVisit());
            rowOutput.createCell(26).setCellValue(averageNumberOfTimesVehicleRouteGenerated);
            rowOutput.createCell(27).setCellValue(avergageTimeToVehicleRouteGenerated);
        }
        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_1) || input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2) || input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_3)) {
            rowOutput.createCell(19).setCellValue(input.getNrStationBranching());
            rowOutput.createCell(22).setCellValue(input.getTresholdLengthRoute());
            //Criticality score
            rowOutput.createCell(0).setCellValue(input.getWeightTimeToViolation());
            rowOutput.createCell(1).setCellValue(input.getWeightViolationRate());
            rowOutput.createCell(2).setCellValue(input.getWeightDrivingTime());
            rowOutput.createCell(3).setCellValue(input.getWeightOptimalState());
        }
        rowOutput.createCell(14).setCellValue(input.getTimeHorizon());
        rowOutput.createCell(15).setCellValue(input.getSimulationStartTime()/60);
        rowOutput.createCell(16).setCellValue(input.getSimulationStopTime()/60);
        rowOutput.createCell(17).setCellValue(input.getTestInstance());
        if (input.getSolutionMethod().equals(SolutionMethod.NO_VEHICLES)) {
            rowOutput.createCell(18).setCellValue(0);
        }
        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2 )) {
            rowOutput.createCell(20).setCellValue(input.getLoadInterval());
        }
        rowOutput.createCell(21).setCellValue(input.getNumberOfRuns());

        rowOutput.createCell(23).setCellValue(avergaeComputationalTimeXpress);
        rowOutput.createCell(24).setCellValue(averageComputationalTimeXpressPlussInitialization);
        rowOutput.createCell(25).setCellValue(averagePercentageVoilation);

        fileInputStream.close();

        FileOutputStream fileOut = new FileOutputStream("Results.xlsx");
        workbook.write(fileOut);
        fileOut.close();


        System.out.println("Printet resultater");

    }







    public static void printOneRouteResultsToExcelFile(Input input, double objectiveValue, double computationalTimeXpress, double computationalTimeIncludingInitialization) throws IOException {

        System.out.println("Objective value: " + objectiveValue);
        System.out.println("Computational time: " + computationalTimeXpress);
        System.out.println("Computational time including initialization: " + computationalTimeIncludingInitialization);
        System.out.println();


        //Read the spreadsheet that needs to be updated
        FileInputStream fileInputStream= new FileInputStream(new File("Results.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet worksheet = workbook.getSheetAt(1);

        int rowNumber = 0;
        boolean findEmptyRow = true;

        while (findEmptyRow) {
            if (worksheet.getRow(rowNumber) == null) {
                findEmptyRow = false;
            } else {
                rowNumber ++;
            }
        }


        //Print output to file
        Row rowOutput = worksheet.createRow(rowNumber);

        //Criticality score
        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_1) || input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2) || input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_3)) {
            rowOutput.createCell(0).setCellValue(input.getWeightTimeToViolation());
            rowOutput.createCell(1).setCellValue(input.getWeightViolationRate());
            rowOutput.createCell(2).setCellValue(input.getWeightDrivingTime());
            rowOutput.createCell(3).setCellValue(input.getWeightOptimalState());
            rowOutput.createCell(19).setCellValue(input.getNrStationBranching());
            rowOutput.createCell(22).setCellValue(input.getTresholdLengthRoute());
        }

        //Objective function
        rowOutput.createCell(4).setCellValue(input.getWeightViolation());
        rowOutput.createCell(5).setCellValue(input.getWeightDeviation());
        rowOutput.createCell(6).setCellValue(input.getWeightReward());
        rowOutput.createCell(7).setCellValue(input.getWeightDeviationReward());
        rowOutput.createCell(8).setCellValue(input.getWeightDrivingTimePenalty());

        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2)) {
            //Loading interval
            rowOutput.createCell(9).setCellValue(input.getMinLoad());
            rowOutput.createCell(10).setCellValue(input.getMaxLoad());
        }


        //Input
        rowOutput.createCell(11).setCellValue(input.getSolutionMethod().toString());
        if (!input.getSolutionMethod().equals(SolutionMethod.NO_VEHICLES)){
            rowOutput.createCell(12).setCellValue(input.getReOptimizationMethod().toString());
            rowOutput.createCell(18).setCellValue(input.getNumberOfVehicles());
            rowOutput.createCell(23).setCellValue(computationalTimeXpress);
            rowOutput.createCell(24).setCellValue(computationalTimeIncludingInitialization);
            rowOutput.createCell(13).setCellValue(input.getMaxVisit());
        }

        rowOutput.createCell(14).setCellValue(input.getTimeHorizon());
        rowOutput.createCell(15).setCellValue(input.getSimulationStartTime()/60);
        rowOutput.createCell(17).setCellValue(input.getTestInstance());

        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2)) {
            rowOutput.createCell(20).setCellValue(input.getLoadInterval());
        }

        rowOutput.createCell(25).setCellValue(objectiveValue);

        fileInputStream.close();

        FileOutputStream fileOut = new FileOutputStream("Results.xlsx");
        workbook.write(fileOut);
        fileOut.close();

    }


}




















