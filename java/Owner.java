import java.util.ArrayList;
import java.util.List;
// Owner class extending User
class Owner extends User {
    private Franchise franchise;

    public Owner(String name, String email, String password, Franchise franchise) {
        super(name, email, password, true);
        this.franchise = franchise;
    }

    public List<Reservation> viewReservations() {
        return franchise.getReservations();
    }
    public void setFranchise(Franchise franchise) {
        this.franchise = franchise;
    }
}