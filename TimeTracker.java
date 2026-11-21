

public class TimeTracker {
    private static String currentTask = null;
    private static Instant startTime = null;
    private static final Map<String, Duration> taskLogs = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Java Time Tracker ===");
        System.out.println("Commands: start <task>, stop, report, exit, chal nahi to bahut marunga");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                stopCurrentTask();
                printReport();
                System.out.println("Goodbye!");
                break;
            } else if (input.startsWith("start ")) {
                String taskName = input.substring(6).trim();
                if (taskName.isEmpty()) {
                    System.out.println("Please specify a task name pop.");
                } else {
                    startTask(taskName);
                }
            } else if (input.equalsIgnoreCase("stop")) {
                stopCurrentTask();
            } else if (input.equalsIgnoreCase("report")) {
                printReport();
            } else {
                System.out.println("Unknown command. Try: start <task>, stop, report, exit");
            }
        }
        scanner.close();
    }

    private static void startTask(String taskName) {
        // Automatically stop any active task before starting a new one
        if (currentTask != null) {
            stopCurrentTask();
        }
        currentTask = taskName;
        startTime = Instant.now();
        System.out.println("Started tracking: " + currentTask);
    }

    private static void stopCurrentTask() {
        if (currentTask == null) {
            System.out.println("No task is currently running.");
            return;
        }
        Duration elapsed = Duration.between(startTime, Instant.now());
        taskLogs.put(currentTask, taskLogs.getOrDefault(currentTask, Duration.ZERO).plus(elapsed));
        
        System.out.println("Stopped '" + currentTask + "' (Session: " + formatDuration(elapsed) + ")");
        currentTask = null;
        startTime = null;
    }

    private static void printReport() {
        System.out.println("\n--- Time Summary ---");
        if (taskLogs.isEmpty() && currentTask == null) {
            System.out.println("No time logged yet.");
            return;
        }
        for (Map.Entry<String, Duration> entry : taskLogs.entrySet()) {
            System.out.printf("%-20s : %s%n", entry.getKey(), formatDuration(entry.getValue()));
        }
        if (currentTask != null) {
            Duration activeElapsed = Duration.between(startTime, Instant.now());
            System.out.printf("%-20s : %s (running)%n", currentTask, formatDuration(activeElapsed));
        }
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }
}
