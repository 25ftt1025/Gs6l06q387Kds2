import java.util.Scanner;
import java.util.ArrayList;

public class LibraryManagementSystem {

    // Book class to store book details
    static class Book {
        String bookId;
        String title;
        String author;
        String category;
        int quantity;
        String isbn;

        Book(String bookId, String title, String author, String category, int quantity, String isbn) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.category = category;
            this.quantity = quantity;
            this.isbn = isbn;
        }
    }

    // List to hold all books (in‑memory storage)
    static ArrayList<Book> books = new ArrayList<>();

    // Scanner for user input
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre‑add a sample book for demonstration
        books.add(new Book("B001", "Java Programming", "John Doe", "Education", 5, "978-1234567890"));

        // Login loop
        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.print("Enter email: ");
            String email = input.nextLine();
            System.out.print("Enter password: ");
            String password = input.nextLine();

            if (login(email, password)) {
                System.out.println("\nLogin successful! Welcome, Librarian.");
                mainMenu();  // After successful login, show main menu
            } else {
                System.out.println("\nInvalid email or password. Please try again.");
            }
        }
    }

    // ---------- FEATURE 1: LOGIN & LOGOUT ----------
    static boolean login(String email, String password) {
        // Email validation: must contain '@' and '.' after '@'
        boolean validEmail = email.contains("@") && email.indexOf('.') > email.indexOf('@') + 1;
        // Password validation: length >=8, at least 1 digit, 1 uppercase, 1 lowercase
        boolean validPassword = password.length() >= 8 &&
                                password.matches(".*\\d.*") &&
                                password.matches(".*[A-Z].*") &&
                                password.matches(".*[a-z].*");

        // For this demo, we accept any email/password that meets the format rules.
        // In a real system you would check against a stored admin account.
        // Here we also accept a fixed admin account for testing.
        boolean isAdmin = email.equalsIgnoreCase("admin@library.com") && password.equals("Admin123");

        return (validEmail && validPassword) || isAdmin;
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book");
            System.out.println("3. Delete Book");
            System.out.println("4. List All Books");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    editBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    listBooks();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;  // return to login loop
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        }
    }

    // ---------- FEATURE 2: ADD, EDIT, DELETE BOOKS ----------
    static void addBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        String bookId;
        // Validate unique Book ID
        while (true) {
            System.out.print("Book ID: ");
            bookId = input.nextLine();
            if (bookId.trim().isEmpty()) {
                System.out.println("Error: Book ID cannot be empty.");
                continue;
            }
            boolean idExists = false;
            for (Book b : books) {
                if (b.bookId.equalsIgnoreCase(bookId)) {
                    idExists = true;
                    break;
                }
            }
            if (idExists) {
                System.out.println("Error: Book ID already exists. Please use a unique ID.");
            } else {
                break;
            }
        }

        // Title: at least 3 characters, not empty
        String title;
        while (true) {
            System.out.print("Title: ");
            title = input.nextLine();
            if (title.trim().length() >= 3) {
                break;
            } else {
                System.out.println("Error: Title must be at least 3 characters and not empty.");
            }
        }

        // Author: at least 3 characters, not empty
        String author;
        while (true) {
            System.out.print("Author: ");
            author = input.nextLine();
            if (author.trim().length() >= 3) {
                break;
            } else {
                System.out.println("Error: Author must be at least 3 characters and not empty.");
            }
        }

        // Category: not empty
        String category;
        while (true) {
            System.out.print("Category: ");
            category = input.nextLine();
            if (!category.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Error: Category cannot be empty.");
            }
        }

        // Quantity: whole number > 0
        int quantity;
        while (true) {
            System.out.print("Quantity: ");
            if (input.hasNextInt()) {
                quantity = input.nextInt();
                input.nextLine(); // consume newline
                if (quantity > 0) {
                    break;
                } else {
                    System.out.println("Error: Quantity must be greater than 0.");
                }
            } else {
                System.out.println("Error: Please enter a valid whole number.");
                input.nextLine(); // clear invalid input
            }
        }

        // ISBN: if provided, must be valid (simple check: not empty and length 10 or 13)
        String isbn;
        while (true) {
            System.out.print("ISBN (press Enter to skip): ");
            isbn = input.nextLine();
            if (isbn.trim().isEmpty()) {
                break; // ISBN is optional
            }
            // Simple ISBN validation: length 10 or 13, only digits and hyphens allowed
            String isbnClean = isbn.replace("-", "");
            if (isbnClean.matches("\\d+") && (isbnClean.length() == 10 || isbnClean.length() == 13)) {
                break;
            } else {
                System.out.println("Error: ISBN must be 10 or 13 digits (hyphens allowed).");
            }
        }

        books.add(new Book(bookId, title, author, category, quantity, isbn));
        System.out.println("Book added successfully!");
    }

    static void editBook() {
        System.out.println("\n--- EDIT BOOK ---");
        if (books.isEmpty()) {
            System.out.println("No books in the system yet.");
            return;
        }

        System.out.print("Enter Book ID to edit: ");
        String bookId = input.nextLine();
        Book bookToEdit = null;
        for (Book b : books) {
            if (b.bookId.equalsIgnoreCase(bookId)) {
                bookToEdit = b;
                break;
            }
        }
        if (bookToEdit == null) {
            System.out.println("Error: Book ID not found.");
            return;
        }

        System.out.println("Leave field empty to keep current value.");
        System.out.println("Current Title: " + bookToEdit.title);
        System.out.print("New Title: ");
        String newTitle = input.nextLine();
        if (!newTitle.trim().isEmpty()) {
            while (newTitle.trim().length() < 3) {
                System.out.println("Error: Title must be at least 3 characters.");
                System.out.print("New Title: ");
                newTitle = input.nextLine();
            }
            bookToEdit.title = newTitle;
        }

        System.out.println("Current Author: " + bookToEdit.author);
        System.out.print("New Author: ");
        String newAuthor = input.nextLine();
        if (!newAuthor.trim().isEmpty()) {
            while (newAuthor.trim().length() < 3) {
                System.out.println("Error: Author must be at least 3 characters.");
                System.out.print("New Author: ");
                newAuthor = input.nextLine();
            }
            bookToEdit.author = newAuthor;
        }

        System.out.println("Current Category: " + bookToEdit.category);
        System.out.print("New Category: ");
        String newCategory = input.nextLine();
        if (!newCategory.trim().isEmpty()) {
            bookToEdit.category = newCategory;
        }

        System.out.println("Current Quantity: " + bookToEdit.quantity);
        System.out.print("New Quantity: ");
        String newQtyStr = input.nextLine();
        if (!newQtyStr.trim().isEmpty()) {
            try {
                int newQty = Integer.parseInt(newQtyStr);
                if (newQty > 0) {
                    bookToEdit.quantity = newQty;
                } else {
                    System.out.println("Error: Quantity must be > 0. Keeping old value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number. Keeping old quantity.");
            }
        }

        System.out.println("Current ISBN: " + bookToEdit.isbn);
        System.out.print("New ISBN (press Enter to keep current): ");
        String newIsbn = input.nextLine();
        if (!newIsbn.trim().isEmpty()) {
            String isbnClean = newIsbn.replace("-", "");
            if (isbnClean.matches("\\d+") && (isbnClean.length() == 10 || isbnClean.length() == 13)) {
                bookToEdit.isbn = newIsbn;
            } else {
                System.out.println("Error: Invalid ISBN format. Keeping old ISBN.");
            }
        }

        System.out.println("Book updated successfully!");
    }

    static void deleteBook() {
        System.out.println("\n--- DELETE BOOK ---");
        if (books.isEmpty()) {
            System.out.println("No books in the system.");
            return;
        }

        System.out.print("Enter Book ID to delete: ");
        String bookId = input.nextLine();
        boolean found = false;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).bookId.equalsIgnoreCase(bookId)) {
                books.remove(i);
                found = true;
                System.out.println("Book deleted successfully!");
                break;
            }
        }
        if (!found) {
            System.out.println("Error: Book ID not found.");
        }
    }

    static void listBooks() {
        System.out.println("\n--- ALL BOOKS ---");
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.printf("%-10s %-30s %-20s %-15s %-8s %-15s%n", "ID", "Title", "Author", "Category", "Qty", "ISBN");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Book b : books) {
            System.out.printf("%-10s %-30s %-20s %-15s %-8d %-15s%n",
                    b.bookId, b.title, b.author, b.category, b.quantity, b.isbn);
        }
    }
}
