package com.company.file_management;

import com.company.BookingConfirm;
import com.company.Room;

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
            if(!file.exists()) {
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
}
