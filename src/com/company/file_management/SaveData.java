package com.company.file_management;

import com.company.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveData {
    //Serialize data: Save

    public void saveRoom(Room room) {
        ArrayList<Object> data = new ArrayList<>();
        data.add(room.getRoomNumber());
        data.add(room.getStandard());
        data.add(room.getBeds());
        data.add(room.getRoomBookingList());

        try {
            File file = new File("src/com/company/file_management/files/room" + room.getRoomNumber() + ".ser");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(data);
            objectOutput.close();
            fileOutput.close();
            //System.out.println("Room " + room.getRoomNumber() + " saved.");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveCustomers(ArrayList<AccountCustomer> customerList) {
        try {
            File file = new File("src/com/company/file_management/files/customers.ser");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(customerList);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveAdmins(ArrayList<AccountAdmin> adminArrayList) {
        try {
            File file = new File("src/com/company/file_management/files/admins.ser");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(adminArrayList);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveBedPrices(ArrayList<BedPrice> bedsList) {
        try {
            File file = new File("src/com/company/file_management/files/beds.ser");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(bedsList);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveStandards(ArrayList<StandardPrice> standardList) {
        try {
            File file = new File("src/com/company/file_management/files/standards.ser");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(standardList);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveEntityCounts(int[] entityValues) { //For saving count values for ID's: Rooms(0), Accounts(1), Bookings(2)
        try {
            File file = new File("src/com/company/file_management/files/entityCounts.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(entityValues);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
