package com.company.file_management;

import com.company.AccountAdmin;
import com.company.AccountCustomer;
import com.company.BedPrice;
import com.company.StandardPrice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadData {
    //Deserialize data: Load

    public ArrayList<Object> loadRoom(int roomNumber) { //Correlates with methods loadAllRooms, updateAllRooms and updateRoom in class HotelLogistics
        File file = new File("src/com/company/file_management/files/room" + roomNumber + ".ser");
        ArrayList<Object> data = new ArrayList<>();
        if (file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                data = (ArrayList<Object>) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return data;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public ArrayList<AccountCustomer> loadCustomers() {
        File file = new File("src/com/company/file_management/files/customers.ser");
        ArrayList<AccountCustomer> customerList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                customerList = (ArrayList<AccountCustomer>) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return customerList;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public ArrayList<AccountAdmin> loadAdmins() {
        File file = new File("src/com/company/file_management/files/admins.ser");
        ArrayList<AccountAdmin> adminList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                adminList = (ArrayList<AccountAdmin>) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return adminList;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public ArrayList<BedPrice> loadBedPrices() {
        File file = new File("src/com/company/file_management/files/beds.ser");
        ArrayList<BedPrice> bedsList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                bedsList = (ArrayList<BedPrice>) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return bedsList;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public ArrayList<StandardPrice> loadStandards() {
        File file = new File("src/com/company/file_management/files/standards.ser");
        ArrayList<StandardPrice> standardList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                standardList = (ArrayList<StandardPrice>) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return standardList;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public int[] loadEntityCounts() { //For loading count values for ID's: Rooms(0), Customers(1), Admins(2) Bookings(3)
        File file = new File("src/com/company/file_management/files/entityCounts.dat");
        int[] entityValues = new int[5];
        if(file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);

                entityValues = (int[]) objectInput.readObject();

                fileInput.close();
                objectInput.close();
                return entityValues;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
}
