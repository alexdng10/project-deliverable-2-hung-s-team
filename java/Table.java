// Table class representing individual tables
class Table {
    private int tableId;
    private int seats;
    private boolean isReserved;

    public Table(int tableId, int seats) {
        this.tableId = tableId;
        this.seats = seats;
        this.isReserved = false;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void reserve() {
        this.isReserved = true;
    }

    public void cancelReservation() {
        this.isReserved = false;
    }

    public int getTableId() {
        return tableId;
    }

    public int getSeats() {
        return seats;
    }
}
