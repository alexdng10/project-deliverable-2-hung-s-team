import java.util.ArrayList;
import java.util.List;

// Base User class
class User {
    protected String name;
    protected String email;
    protected String password;
    protected boolean isOwner;

    public User(String name, String email, String password, boolean isOwner) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOwner = isOwner;
    }

    public Reservation makeReservation(Table table) {
        if (!table.isReserved()) {
            Reservation reservation = new Reservation(this, table);
            table.reserve();
            return reservation;
        } else {
            System.out.println("Table is already reserved.");
            return null;
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (reservation != null) {
            reservation.getTable().cancelReservation();
            System.out.println("Reservation canceled.");
        }
    }
}
