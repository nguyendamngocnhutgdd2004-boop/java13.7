import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookManager manager = new BookManager();
        int choice;

        do {
            System.out.println("\n********************* HỆ THỐNG QUẢN LÝ THƯ VIỆN ********************");
            System.out.println("1. Thêm sách mới");
            System.out.println("2. Cập nhật thông tin sách theo ID");
            System.out.println("3. Xóa sách theo ID");
            System.out.println("4. Tìm kiếm sách theo tác giả");
            System.out.println("5. Hiển thị danh sách tất cả sách");
            System.out.println("6. Thoát");
            System.out.print("Lựa chọn của bạn (1-6): ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> {
                        System.out.println("\n--- THÊM SÁCH MỚI ---");
                        String title = inputNonEmpty(scanner, "Nhập tên sách: ");
                        String author = inputNonEmpty(scanner, "Nhập tác giả: ");
                        int year = inputYear(scanner);
                        double price = inputDouble(scanner, "Nhập giá bán sách: ");
                        manager.addBook(new Book(title, author, year, price));
                    }
                    case 2 -> {
                        System.out.println("\n--- CẬP NHẬT THÔNG TIN SÁCH ---");
                        int id = inputInt(scanner, "Nhập mã ID sách cần sửa: ");
                        String title = inputNonEmpty(scanner, "Nhập tên sách mới: ");
                        String author = inputNonEmpty(scanner, "Nhập tác giả mới: ");
                        int year = inputYear(scanner);
                        double price = inputDouble(scanner, "Nhập giá bán mới: ");
                        manager.updateBook(id, new Book(title, author, year, price));
                    }
                    case 3 -> {
                        System.out.println("\n--- XÓA SÁCH KHỎI THƯ VIỆN ---");
                        int id = inputInt(scanner, "Nhập mã ID sách cần xóa: ");
                        manager.deleteBook(id);
                    }
                    case 4 -> {
                        System.out.println("\n--- TÌM KIẾM SÁCH THEO TÁC GIẢ ---");
                        String author = inputNonEmpty(scanner, "Nhập tên tác giả cần tìm kiếm: ");
                        manager.findBooksByAuthor(author);
                    }
                    case 5 -> manager.listAllBooks();
                    case 6 -> System.out.println("Chương trình kết thúc. Tạm biệt!");
                    default -> System.err.println("Lỗi: Hãy chọn số hợp lệ từ 1 đến 6!");
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Lựa chọn menu phải là một số nguyên!");
                choice = -1;
            }
        } while (choice != 6);

        scanner.close();
    }

    private static String inputNonEmpty(Scanner scanner, String prompt) {
        while (true) {
            System.err.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.err.println("Lỗi: Trường thông tin này không được phép để trống!");
                continue;
            }
            return input;
        }
    }

    private static int inputInt(Scanner scanner, String prompt) {
        while (true) {
            System.err.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Dữ liệu nhập vào phải là một số nguyên!");
            }
        }
    }

    private static double inputDouble(Scanner scanner, String prompt) {
        while (true) {
            System.err.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value <= 0) {
                    System.err.println("Lỗi: Giá bán phải lớn hơn 0!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Dữ liệu nhập vào phải là một số thực!");
            }
        }
    }

    private static int inputYear(Scanner scanner) {
        while (true) {
            int year = inputInt(scanner, "Nhập năm xuất bản: ");
            if (year < 1000 || year > 2026) {
                System.err.println("Lỗi: Năm xuất bản không hợp lệ (Phải từ năm 1000 đến năm 2026)!");
                continue;
            }
            return year;
        }
    }
}
