// Franchise class representing a restaurant location
class Franchise {
    private String location;
    private List<Table> tables;
    private List<Reservation> reservations;
    private Owner owner;

    public Franchise(String location, Owner owner) {
        this.location = location;
        this.owner = owner;
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public List<Table> getTableAvailability() {
        List<Table> availableTables = new ArrayList<>();
        for (Table table : tables) {
            if (!table.isReserved()) {
                availableTables.add(table);
            }
        }
        return availableTables;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
