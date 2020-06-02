package pl.plauszta;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Main {

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final String TOTAL_SUM_PATTERN = "Total sum: $%.2f%n";
    private static double balance = 0.00;
    private static final List<Purchase> PURCHASES = new ArrayList<>();

    public static void main(String[] args) {
        int option;
        do {
            printMenu();
            option = Integer.parseInt(SCANNER.nextLine());

            switch (option) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    showList();
                    break;
                case 4:
                    showBalance();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    analyze();
                    break;
                case 0:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        } while (option != 0);

        System.out.println("\nBye!");
        SCANNER.close();
    }


    private static void printMenu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
    }

    private static void addIncome() {
        System.out.println("\nEnter income:");
        balance += Double.parseDouble(SCANNER.nextLine());
        System.out.println("Income was added!\n");
    }

    private static void addPurchase() {
        while (true) {
            printCategories();
            System.out.println("5) Back");
            int option = Integer.parseInt(SCANNER.nextLine());
            if (option == 5) {
                System.out.println();
                return;
            }
            Category category = getCategory(option);
            System.out.println("\nEnter purchase name:");
            String name = SCANNER.nextLine();
            System.out.println("Enter its price:");
            double price = Double.parseDouble(SCANNER.nextLine());
            PURCHASES.add(new Purchase(name, price, category));
            balance -= price;
            System.out.println("Purchase was added!\n");
        }
    }

    private static void printCategories() {
        System.out.println("\nChoose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
    }

    private static void showList() {
        if (PURCHASES.isEmpty()) {
            System.out.println("\nPurchase list is empty");
        } else {
            while (true) {
                double sum = 0.00;
                printCategories();
                System.out.println("5) All");
                System.out.println("6) Back");
                int option = Integer.parseInt(SCANNER.nextLine());

                if (option == 6) {
                    System.out.println();
                    return;
                }
                System.out.println();
                if (option == 5) {
                    System.out.println("All:");
                    for (Purchase purchase : PURCHASES) {
                        sum += purchase.getPrice();
                        System.out.println(purchase);
                    }
                    System.out.println(String.format("Total: $%.2f", sum));
                } else {
                    printCategoryList(option);
                }
            }
        }
    }

    private static void printCategoryList(int option) {
        double sum = 0.00;
        Category category;
        if (option == 1) {
            category = Category.FOOD;
            System.out.println("Food:");
        } else if (option == 2) {
            category = Category.CLOTHES;
            System.out.println("Clothe:");
        } else if (option == 3) {
            category = Category.ENTERTAINMENT;
            System.out.println("Entertainment:");
        } else {
            category = Category.OTHER;
            System.out.println("Other:");
        }
        boolean isEmpty = true;

        for (Purchase purchase : PURCHASES) {
            if (purchase.getCategory() == category) {
                isEmpty = false;
                sum += purchase.getPrice();
                System.out.println(purchase);
            }
        }
        System.out.println(isEmpty ? "Purchase list is empty!" : String.format("Total: $%.2f", sum));
        System.out.println();
    }

    private static Category getCategory(int option) {
        if (option == 1) {
            return Category.FOOD;
        } else if (option == 2) {
            return Category.CLOTHES;
        } else if (option == 3) {
            return Category.ENTERTAINMENT;
        } else {
            return Category.OTHER;
        }
    }

    private static void showBalance() {
        System.out.println();
        System.out.println(String.format("Balance: $%.2f", balance));
        System.out.println();
    }

    private static void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("purchases.txt"))) {
            bufferedWriter.write(balance + "\n");
            for (Purchase purchase : PURCHASES) {
                bufferedWriter.write(purchase.getName() + "\t"
                        + purchase.getPrice() + "\t"
                        + purchase.getCategory().toString() + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("\nPurchases were saved!\n");
    }

    private static void load() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line = bufferedReader.readLine();
            balance = Double.parseDouble(line);
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split("\t");
                PURCHASES.add(new Purchase(str[0], Double.parseDouble(str[1]), Category.valueOf(str[2])));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("\nPurchases were loaded\n");
    }

    private static void analyze() {
        System.out.println();
        while (true) {
            printSortMenu();
            int option = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (option) {
                case 1:
                    sortAll();
                    break;
                case 2:
                    sortByType();
                    break;
                case 3:
                    sortType();
                    break;
                case 4:
                    return;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private static void printSortMenu() {
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
    }

    private static void sortAll() {
        if (PURCHASES.isEmpty()) {
            System.out.println("\nPurchase list is empty!\n");
            return;
        }
        System.out.println();
        double sum = 0;
        Collections.sort(PURCHASES);
        System.out.println("All:");
        for (Purchase purchase : PURCHASES) {
            System.out.println(purchase);
            sum += purchase.getPrice();
        }
        System.out.println(String.format(TOTAL_SUM_PATTERN, sum));
    }

    private static void sortByType() {
        Map<Category, Double> purchMap = new LinkedHashMap<>();
        double sum = 0;
        purchMap.put(Category.FOOD, 0.0);
        purchMap.put(Category.CLOTHES, 0.0);
        purchMap.put(Category.ENTERTAINMENT, 0.0);
        purchMap.put(Category.OTHER, 0.0);

        for (Purchase purchase : PURCHASES) {
            purchMap.put(purchase.getCategory(), purchMap.get(purchase.getCategory()) + purchase.getPrice());
            sum += purchase.getPrice();
        }

        Map<Category, Double> result = sortMap(purchMap);
        System.out.println();
        printTypesAndPrices(sum, result);
    }

    private static Map<Category, Double> sortMap(Map<Category, Double> purchMap) {
        return purchMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    private static void printTypesAndPrices(double sum, Map<Category, Double> result) {
        result.entrySet()
                .stream()
                .map(s -> String.format("%s - $%.2f", (s.getKey().toString().toUpperCase().charAt(0)
                                + s.getKey().toString().toLowerCase().substring(1)),
                        s.getValue()))
                .forEach(System.out::println);

        System.out.println(String.format(TOTAL_SUM_PATTERN, sum));
    }

    private static void sortType() {
        printCategories();
        int option = Integer.parseInt(SCANNER.nextLine());
        System.out.println();
        Category category = printAndGetCategory(option);

        double sum = 0;
        Collections.sort(PURCHASES);
        for (Purchase purchase : PURCHASES) {
            if (category == purchase.getCategory()) {
                System.out.println(String.format("%s $%.2f", purchase.getName(), purchase.getPrice()));
                sum += purchase.getPrice();
            }
        }
        if (sum == 0.0) {
            System.out.println("Purchase list is empty!\n");
        } else {
            System.out.println(String.format(TOTAL_SUM_PATTERN, sum));
        }
    }

    private static Category printAndGetCategory(int option) {
        Category category;
        if (option == 1) {
            category = Category.FOOD;
            System.out.println("Food:");
        } else if (option == 2) {
            category = Category.CLOTHES;
            System.out.println("Clothes:");
        } else if (option == 3) {
            category = Category.ENTERTAINMENT;
            System.out.println("Entertainment:");
        } else {
            category = Category.OTHER;
            System.out.println("Other:");
        }
        return category;
    }
}