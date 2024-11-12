// Reservation class
class Reservation {
    private static int idCounter = 1;
    private int reservationId;
    private User user;
    private Table table;

    public Reservation(User user, Table table) {
        this.reservationId = idCounter++;
        this.user = user;
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void confirmReservation() {
        System.out.println("Reservation confirmed for table " + table.getTableId());
    }

    public void cancelReservation() {
        table.cancelReservation();
        System.out.println("Reservation canceled.");
    }
}