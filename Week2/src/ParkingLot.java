import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    String status;

    ParkingSpot() {
        status = "EMPTY";
    }
}

public class ParkingLot {

    ParkingSpot[] table;
    int capacity;
    int occupied = 0;
    int totalProbes = 0;
    int operations = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public String parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;
        operations++;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String plate) {

        int index = hash(plate);

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                    table[index].licensePlate.equals(plate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                table[index].status = "DELETED";
                occupied--;

                double hours = duration / 3600000.0;
                double fee = hours * 5;

                return "Spot #" + index + " freed, Fee: $" + fee;
            }

            index = (index + 1) % capacity;
        }

        return "Vehicle not found";
    }

    public void getStatistics() {

        double occupancy = (occupied * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : (double) totalProbes / operations;

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Avg Probes: " + avgProbes);
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(500);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        System.out.println(lot.exitVehicle("ABC-1234"));

        lot.getStatistics();
    }
}