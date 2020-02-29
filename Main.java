package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class HolidaySystem {
    public static final int FIRST_NAME_WIDTH = 15;
    public static final int LAST_NAME_WIDTH = 20;
    public static final int EMAIL_WIDTH = 35;
    public static final int ID_WIDTH = 20;
    public static final int HOLIDAY_PERIOD_WIDTH = 35;
    public static final int HOLIDAY_TYPE_WIDTH = 130;

    public static final String HEADER_FIRST_NAME = "Име";
    public static final String HEADER_LAST_NAME = "Фамилия";
    public static final String HEADER_EMAIL = "Е-мейл";
    public static final String HEADER_ID ="ЕГН";
    public static final String HEADER_HOLIDAY_PERIOD = "Начална дата - крайна дата";
    public static final String HEADER_HOLIDAY_TYPE = "Тип отпуска";


    public static void main(String[] args) {
        printUserOptions();
    }
    public static void printUserOptions(){
        Scanner sc = new Scanner(System.in);
        printApplicationOptions();
        System.out.println("Въведи избор: ");
        String userChoice = sc.nextLine();
        while (!userChoice.matches("[1-5]")){
            System.out.println("Моля, въведете число между 1 и 5!");
            userChoice = sc.nextLine();
        }
        switch (Integer.parseInt(userChoice)) {
            case 1:
                writeHolidayToFile();
                break;
            case 2:
                showAllHoliday();
                break;
            case 3:
                showUserHolidaies();
                break;
            case 4:
                changeHolidayStatus();
                break;
            case 5:
                exitOfProgram();
                break;
            default:
                System.out.println("Моля, въведи избор между 1 и 5!");
                printUserOptions();
        }
    }

    public static void printApplicationOptions(){
        drawBorder();
        System.out.println("   1. Заяви отпуска. ");
        System.out.println("   2. Виж всички отпуски.");
        System.out.println("   3. Виж отпуска на служител.");
        System.out.println("   4. Промени статус на отпуска.");
        System.out.println("   5. Изход");
        drawBorder();
    }

    private static void drawBorder(){
        System.out.println("--------------------------------------------");
    }

    public static String[]  returnUserDetails(){
        String[] holidayDetails = new String[6];
        holidayDetails[0] = returnFirstName();
        holidayDetails[1] = returnLastName();
        holidayDetails[2] = returnEmail();
        holidayDetails[3] = returnUserId();
        holidayDetails[4] = returnHolidayPeriod();
        holidayDetails[5] = returnHolidayType();
        return holidayDetails;
    }

    public static void writeHolidayToFile(){
        String[] holiday = returnUserDetails();
        try (
                PrintWriter writer = new PrintWriter(
                        new BufferedWriter(
                                new FileWriter("holiday.txt", true)))) {
            for (int i = 0; i < holiday.length - 1; i++) {
                writer.print(holiday[i] + "\t");
            }
            writer.println(holiday[5]);
            System.out.println("Заявката ви беше успешно записана.");
            writer.close();
            printUserOptions();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void showAllHoliday(){
        String fetchedHoliday = fetchAllHolidayFromFile();
        formatHolidayAndDisplay(fetchedHoliday);
        printUserOptions();
    }

    public static String fetchAllHolidayFromFile() {
        Path patToFile = Paths.get("holiday.txt");
        if (Files.notExists(patToFile) || patToFile.toFile().length() == 0) {
            System.out.println("Все още няма заявени отпуски.");
            printUserOptions();
            return "";
        } else {
            File holiday = new File("holiday.txt");
            StringBuilder holidaySB = new StringBuilder();
            Scanner scanner = null;
            try {
                scanner = new Scanner(holiday);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                holidaySB.append("\n");
            }
            String allHoliday = holidaySB.toString();
            return allHoliday;
        }
    }

    public static void formatHolidayAndDisplay(String holidayString) {
        String[] holidayArray;
        holidayArray = holidayString.split("\n");
        printHolidayTableHeader();
        for (int i = 0; i < holidayArray.length; i++) {
            String[] row = holidayArray[i].split("\t");
            printHolidaies(row);

        }
    }

    public static void printHolidayTableHeader() {
        System.out.print(HEADER_FIRST_NAME);
        for (int j = 0; j < FIRST_NAME_WIDTH - HEADER_FIRST_NAME.length(); j++) {
            System.out.print(" ");
        }
        System.out.print(HEADER_LAST_NAME);
        for (int j = 0; j < LAST_NAME_WIDTH - HEADER_LAST_NAME.length(); j++) {
            System.out.print(" ");
        }
        System.out.print(HEADER_EMAIL);
        for (int j = 0; j < EMAIL_WIDTH - HEADER_EMAIL.length(); j++) {
            System.out.print(" ");
        }
        System.out.print(HEADER_ID);
        for (int j = 0; j < ID_WIDTH - HEADER_ID.length(); j++) {
            System.out.print(" ");
        }
        System.out.print(HEADER_HOLIDAY_PERIOD);
        for (int j = 0; j < HOLIDAY_PERIOD_WIDTH - HEADER_HOLIDAY_PERIOD.length(); j++) {
            System.out.print(" ");
        }
        System.out.println(HEADER_HOLIDAY_TYPE);
        for (int j = 0; j < HOLIDAY_TYPE_WIDTH; j++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void showUserHolidaies() {
        String allHolidaies = fetchAllHolidayFromFile();
        String[] userFirstAndLastNames = new String[2];
        userFirstAndLastNames[0] = returnFirstName();
        userFirstAndLastNames[1] = returnLastName();
        String[] holidaiesArray = allHolidaies.split("\n");
        int indexFirstName;
        int indexLastName;
        int occurrenceCounter = 0;
        boolean isTableHeaderPrinted = false;
        for (int i = 0; i < holidaiesArray.length; i++) {
            indexFirstName = holidaiesArray[i].toLowerCase().indexOf(userFirstAndLastNames[0].toLowerCase());
            indexLastName = holidaiesArray[i].toLowerCase().indexOf(userFirstAndLastNames[1].toLowerCase());
            if ((indexFirstName == -1) || (indexLastName == -1)) {
                continue;
            }
            {
                if (!isTableHeaderPrinted) {
                    printHolidayTableHeader();
                }
                String[] row = holidaiesArray[i].split("\t");
                printHolidaies(row);
                occurrenceCounter++;
            }
            isTableHeaderPrinted = true;
        }
        if (occurrenceCounter != 0) {
            printUserOptions();
        } else {
            System.out.println();
            System.out.println("Този служител все още няма заявени отпуски.");
            printUserOptions();
        }
    }

    public static void printHolidaies(String[] holidaies){
        String[] row = holidaies;
        System.out.print(row[0]);
        for (int j = 0; j < FIRST_NAME_WIDTH - row[0].length(); j++) {
            System.out.print(" ");
        }
        System.out.print(row[1]);
        for (int j = 0; j < LAST_NAME_WIDTH - row[1].length(); j++) {
            System.out.print(" ");
        }
        System.out.print(row[2]);
        for (int j = 0; j < EMAIL_WIDTH - row[2].length(); j++) {
            System.out.print(" ");
        }
        System.out.print(row[3]);
        for (int j = 0; j < ID_WIDTH - row[3].length(); j++) {
            System.out.print(" ");
        }
        System.out.print(row[4]);
        for (int j = 0; j < HOLIDAY_PERIOD_WIDTH - row[4].length(); j++) {
            System.out.print(" ");
        }
        System.out.println(row[5]);
    }

    public static void changeHolidayStatus(){
        System.out.println("Тази опция все още не е реализирна. Моля, изберете друга!");
        printUserOptions();
    }

    public static String returnFirstName(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете собствено име: ");
        String firstName = sc.nextLine();
        if(firstName == null){
            System.out.println("Моля, въведете име!");
            firstName = sc.nextLine();
        }
        else{
            firstName = firstName.trim();
            while (firstName.isEmpty() || !firstName.matches("^[А-ЩЮ-Я][а-я]{0,10}[а-щю-я][A-Z][a-z]$"));
            {
                System.out.println("Моля, въведете коректно вашето име!");
                firstName = sc.nextLine();
            }
        }
        return firstName;

    }

    public static String returnLastName(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете фамилно име: ");
        String lastName = sc.nextLine();
        if(lastName == null){
            System.out.println("Моля, въведете име!");
            lastName = sc.nextLine();
        }
        else{
            lastName = lastName.trim();
            while (lastName.isEmpty() || !lastName.matches("^[А-ЩЮ-Я][а-я]{0,10}[а-щю-я][A-Z][a-z]$"));
            {
                System.out.println("Моля, въведете коректно вашето фамилно име!");
                lastName = sc.nextLine();
            }
        }
        return lastName;

    }

    public static String returnEmail(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете имейл: ");
        String email = sc.nextLine();
        if (email == null){
            System.out.println("Моля, въведете коректно вашият имейл!");
            email = sc.nextLine();
        }
        else{
            email = email.trim();
            while(!email.matches("^[A-Za-z0-9_\\-.]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-.]{2,}$")){
                System.out.println("Моля, въведете валиден имейл!");
                email = sc.nextLine();
            }
        }
        return email;
    }

    public static String returnUserId(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете ЕГН: ");
        String id = sc.nextLine();
        if (id == null || id.isEmpty()){
            System.out.println("Моля, въведете валидно ЕГН!");
            id = sc.nextLine();
        }
        else{
            id = id.trim();
            while (!id.matches("[0-9]{10}")){
                System.out.println("Моля, въведете валидно ЕГН!");
                id = sc.nextLine();
            }
        }
        return id;
    }

    public static String returnHolidayPeriod(){
        String startDate = returnStartDate();
        String endDate = returnEndDate();
        String holidayPeriod = startDate.concat(" // ").concat(endDate);
        return holidayPeriod;
    }

    public static String returnStartDate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете начална дата в формат DD/Month/Year");
        String startDate = sc.nextLine();
        if (startDate == null || startDate.isEmpty()){
            System.out.println("Моля, въведете валидна начална дата в указания формат!");
            startDate = sc.nextLine();
        }
        else{
            String dateRegex = "[0-9]{2}/[0-9]{2}[A-Za-z]/[0-9]{4}";
            while (!startDate.matches(dateRegex) || !validateDate(startDate)){
                System.out.println("Моля, въведете ваидна начална дата в указания формат!");
                startDate = sc.nextLine();
            }
        }
        return startDate;
    }

    public static String returnEndDate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете начална дата в формат DD/Month/Year");
        String endDate = sc.nextLine();
        if (endDate == null || endDate.isEmpty()){
            System.out.println("Моля, въведете валидна начална дата в указания формат!");
            endDate = sc.nextLine();
        }
        else{
            String dateRegex = "[0-9]{2}/[0-9]{2}[A-Za-z]/[0-9]{4}";
            while (!endDate.matches(dateRegex) || !validateDate(endDate)){
                System.out.println("Моля, въведете ваидна начална дата в указания формат!");
                endDate = sc.nextLine();
            }
        }
        return endDate;
    }

    public static String returnHolidayType(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Моля, въведете тип отпуск: платен / неплатен");
        String payed = "платена";
        String notPayed = "неплатена";
        String holidayType = sc.nextLine();
        if (holidayType == null || holidayType.isEmpty()){
            holidayType = holidayType.trim();
            while (!holidayType.equals(payed) && !holidayType.equals(notPayed)) {
                System.out.println("Моля, въведете думата \"платена\"" +
                        " или думата \"неплатена\"!");
                holidayType = sc.nextLine();
            }
        }
        return holidayType;
    }

    public static boolean validateDate(String date){
        String stringDate = date;
        String[] dateArray = stringDate.split("/");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        boolean isDateValid;
        if (day == 0 || month > 12){
            isDateValid = false;
        }
        else if (month == 0) {
            isDateValid = false;
        }else if (month == 1 && day > 31) {
            isDateValid = false;
        } else if (month == 2 && day > 28) {
            isDateValid = false;
        } else if (month == 3 && day > 31) {
            isDateValid = false;
        } else if (month == 4 && day > 30) {
            isDateValid = false;
        } else if (month == 5 && day > 31) {
            isDateValid = false;
        } else if (month == 6 && day > 30) {
            isDateValid = false;
        } else if (month == 7 && day > 31) {
            isDateValid = false;
        } else if (month == 8 && day > 31) {
            isDateValid = false;
        } else if (month == 9 && day > 30) {
            isDateValid = false;
        } else if (month == 10 && day > 31) {
            isDateValid = false;
        } else if (month == 11 && day > 30) {
            isDateValid = false;
        } else if (month == 12 && day > 31) {
            isDateValid = false;
        } else {
            isDateValid = true;
        }
        return isDateValid;
    }

    public static void exitOfProgram(){
        System.out.println("Излязохте от програмата.");
        System.exit(0);
    }
}
