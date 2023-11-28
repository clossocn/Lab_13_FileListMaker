package com.example.lab_13_filelistmaker;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Lab_13_FileListMaker {
        private static ArrayList<String> list = new ArrayList<>();
        private static boolean needsToBeSaved = false;
        private static String currentFileName = "";
        private static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) {
            boolean quit = false;

            while (!quit) {
                displayMenu();
                String choice = scanner.nextLine().trim().toUpperCase();

                switch (choice) {
                    case "A":
                        addItem();
                        break;
                    case "D":
                        deleteItem();
                        break;
                    case "V":
                        viewList();
                        break;
                    case "O":
                        loadListFromFile();
                        break;
                    case "S":
                        saveListToFile();
                        break;
                    case "C":
                        clearList();
                        break;
                    case "Q":
                        quit = confirmQuit();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }

        private static void displayMenu() {
            System.out.println("List Manager Menu:");
            System.out.println("A - Add an item to the list");
            System.out.println("D - Delete an item from the list");
            System.out.println("V - View the list");
            System.out.println("O - Open a list file from disk");
            System.out.println("S - Save the current list file to disk");
            System.out.println("C - Clear the current list");
            System.out.println("Q - Quit");
        }

        private static void addItem() {
            System.out.print("Enter an item to add: ");
            String item = scanner.nextLine().trim();
            if (!item.isEmpty()) {
                list.add(item);
                needsToBeSaved = true;
                System.out.println("Item added to the list.");
            } else {
                System.out.println("Empty input. Item not added.");
            }
        }

        private static void deleteItem() {
            if (list.isEmpty()) {
                System.out.println("The list is empty.");
                return;
            }

            displayList();

            int itemNumber = getValidIndex();
            if (itemNumber != -1) {
                String deletedItem = list.remove(itemNumber);
                System.out.println("Item '" + deletedItem + "' removed from the list.");
                needsToBeSaved = true;
            } else {
                System.out.println("Invalid index. No item deleted.");
            }
        }

        private static void viewList() {
            displayList();
        }

        private static void clearList() {
            list.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }

        private static void loadListFromFile() {
            if (needsToBeSaved) {
                System.out.println("You have unsaved changes. Do you want to save before loading a new list?");
                boolean saveBeforeLoad = SafeInput.getYNConfirm("Save before loading? (y/n): ");
                if (saveBeforeLoad) {
                    saveListToFile();
                }
            }

            String fileName = scanner.nextLine().trim();
            currentFileName = fileName + ".txt";
            list.clear();

            try (BufferedReader reader = new BufferedReader(new FileReader(currentFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                System.out.println("List loaded from " + currentFileName);
                needsToBeSaved = false; // Loaded list is not dirty
            } catch (IOException e) {
                System.out.println("Error loading the file: " + e.getMessage());
            }
        }

        private static void saveListToFile() {
            if (currentFileName.isEmpty()) {
                currentFileName = scanner.nextLine().trim() + ".txt";
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFileName))) {
                for (String item : list) {
                    writer.write(item);
                    writer.newLine();
                }
                System.out.println("List saved to " + currentFileName);
                needsToBeSaved = false; // Saved list is not dirty
            } catch (IOException e) {
                System.out.println("Error saving the file: " + e.getMessage());
            }
        }

        private static boolean confirmQuit() {
            if (needsToBeSaved) {
                System.out.println("You have unsaved changes. Do you want to save before quitting?");
                boolean saveBeforeQuit = SafeInput.getYNConfirm("Save before quitting? (y/n): ");
                if (saveBeforeQuit) {
                    saveListToFile();
                }
            }
            return true;
        }

        private static void displayList() {
            if (list.isEmpty()) {
                System.out.println("The list is empty.");
                return;
            }

            System.out.println("Current List:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }

        private static int getValidIndex() {
            int maxIndex = list.size();
            System.out.print("Enter the item number to delete (1-" + maxIndex + "): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (choice < 0 || choice >= maxIndex) {
                    return -1;
                }
            } catch (NumberFormatException e) {
                return -1;
            }
            return choice;
        }
    }
