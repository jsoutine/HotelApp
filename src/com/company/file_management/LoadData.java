package com.company.file_management;

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
}
