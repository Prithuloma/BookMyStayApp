public class TrainConsistApp {

    // Custom Exception
    static class EmptyTrainException extends Exception {
        public EmptyTrainException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" UC20 - Exception Handling During Search ");
        System.out.println("======================================\n");

        // Try with empty array first
        String[] bogieIds = {};   // empty train ❌

        String searchId = "BG101";

        try {
            // FAIL-FAST CHECK
            if (bogieIds.length == 0) {
                throw new EmptyTrainException("Train has no bogies. Cannot perform search.");
            }

            // -------- LINEAR SEARCH --------
            boolean found = false;

            for (int i = 0; i < bogieIds.length; i++) {
                if (bogieIds[i].equals(searchId)) {
                    found = true;
                    System.out.println("Bogie ID " + searchId + " found at position: " + i);
                    break;
                }
            }

            if (!found) {
                System.out.println("Bogie ID " + searchId + " not found");
            }

        } catch (EmptyTrainException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println("\nUC20 execution completed...");
    }
}