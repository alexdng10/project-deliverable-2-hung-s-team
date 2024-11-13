import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Base User class
class User {
    protected String name;
    protected String email;
    protected String password;
    protected boolean isOwner;
    protected boolean reservedTable = false;

    public User(String name, String email, String password, boolean isOwner) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOwner = isOwner;

        // save user to csv file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true))) {
            writer.write(name + "," + email + "," + password + "," + isOwner);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reservation makeReservation(Table table, String startTime, String endTime) {
        if (reservedTable) System.out.println("You have already reserved a table");

        if (!table.isReserved()) {
            Reservation reservation = new Reservation(this, table, startTime, endTime);
            table.reserve();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.csv", true))) {
                writer.write(reservation.getId() + "," + table.getTableId() + "," + startTime + "," + endTime);
                writer.newLine();
                reservedTable = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return reservation;
        } else {
            System.out.println("Table is already reserved.");
            return null;
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (reservation != null && deleteReservation(reservation.getId())) {
            reservation.cancelReservation();
            System.out.println("Reservation canceled.");
        }
    }

    private boolean deleteReservation(String reservationId) {
        File inputFile = new File("reservations.csv");
        File tempFile = new File("temp_reservations.csv");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (!data[0].equals(reservationId)) {
                    writer.write(currentLine);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace old file with new file
        if (found && inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            tempFile.delete();
        }

        return found;
    }
    }

