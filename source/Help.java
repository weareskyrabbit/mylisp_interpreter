public class Help {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Version: 0.0");
            System.out.println("Usage:");
            System.out.println("  \u001b[00;36mlisp [file]*\u001b[00m");
            System.out.println("Usage: interactive mode");
            System.out.println("  \u001b[00;36mlisp\u001b[00m");
        } else  {
            switch (args[0]) {
                case "-d":
                case "--detail":
                    System.out.println("Detail:");
                    break;
                case "-e":
                case "--error":
                    System.out.println("Error:");
                    break;
                case "-w":
                case "--warning":
                    System.out.println("Warning:");
                    break;
            }
        }
    }
}
