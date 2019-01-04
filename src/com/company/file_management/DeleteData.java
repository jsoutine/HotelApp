package com.company.file_management;

import java.io.File;

public class DeleteData {

    public void deleteRoom (int roomNumber) {
        File file = new File("src/com/company/file_management/files/room" + roomNumber + ".ser");
        if(file.exists()) {
            file.delete();
        }
        else{
            System.out.println("Room not found in file system.");
        }
    }

}
