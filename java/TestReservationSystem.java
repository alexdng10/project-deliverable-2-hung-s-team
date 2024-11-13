public class TestReservationSystem {
    public static void main(String[] args) {
        // Create an owner and a franchise
        Owner owner = new Owner("Alice", "alice@example.com", "password123", null);
        Franchise franchise = new Franchise("Main Street", owner);

        // Link franchise to the owner
        owner.setFranchise(franchise);

        // Add tables to the franchise
        Table table1 = new Table(4);
        Table table2 = new Table(2);
        franchise.addTable(table1);
        franchise.addTable(table2);

        // Display available tables initially
        System.out.println("Initial table availability:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table ID: " + table.getTableId() + ", Seats: " + table.getSeats());
        }

        // Create a user
        User user1 = new User("Bob", "bob@example.com", "bobpassword", false);

        // User makes a reservation for table1
        Reservation reservation1 = user1.makeReservation(table1, "12:00 PM", "1:00 PM");
        if (reservation1 != null) {
            System.out.println("Reservation made for User: " + user1.name + ", Table ID: " + table1.getTableId());
            reservation1.confirmReservation();
        }

        // Display table availability after reservation
        System.out.println("\nTable availability after reservation:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table ID: " + table.getTableId() + ", Seats: " + table.getSeats());
        }

        // Attempt to make another reservation on the same table (should fail)
        Reservation reservation2 = user1.makeReservation(table1, "1:00 PM", "2:00 PM");
        if (reservation2 == null) {
            System.out.println("\nFailed to make reservation: Table " + table1.getTableId() + " is already reserved.");
        }

        // Cancel the reservation and check availability
        System.out.println("\nCancelling reservation...");
        user1.cancelReservation(reservation1);

        System.out.println("\nTable availability after cancellation:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table ID: " + table.getTableId() + ", Seats: " + table.getSeats());
        }

        // Owner views all reservations (should be empty after cancellation)
        System.out.println("\nOwner's view of all reservations:");
        for (Reservation res : owner.viewReservations()) {
            System.out.println("Reservation ID: " + res.getId() + ", Table ID: " + res.getTable().getTableId());
        }
    }
}
