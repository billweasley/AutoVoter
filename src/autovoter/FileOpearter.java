
package autovoter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileOpearter {

    /* public static boolean writeAccount(String account, String password) {
        String record = account + "@" + password;
        try {
            FileOutputStream out = new FileOutputStream("accounts.txt");
            PrintStream p = new PrintStream(out);
            p.println(record);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("记录写入失败。");
            return false;
        }
    }*/
    public boolean  writeRecord(String account, String uuid, String HDCN) {
        String record = account + "@" + uuid + "@" + HDCN;
        try {
            FileOutputStream out = new FileOutputStream("test.txt",true);
            PrintStream p = new PrintStream(out);
            p.append(record+"\r\n");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("记录写入失败。");
            return false;
        }
    }

    public  List<Map<String, String>> readAccount() throws FileNotFoundException, IOException {
        File file = new File("accounts.txt");
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        String[] tempArray = null;
        List<Map<String, String>> resList = new ArrayList<>();
        Map<String, String> record;

        while ((temp = br.readLine()) != null) {
            record = new HashMap<>();
            tempArray = temp.split("@");
            record.put("account", tempArray[0]);
            record.put("password", tempArray[1]);
            resList.add(record);
        }
        return resList;
    }

    public  List<Map<String, String>> readAccount(List<String> toRemList) throws FileNotFoundException, IOException {
        File file = new File("accounts.txt");
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        String[] tempArray = null;
        List<Map<String, String>> resList = new ArrayList<>();
        Map<String, String> record;

        while ((temp = br.readLine()) != null) {
            tempArray = temp.split("@");
       
                if (!toRemList.contains(tempArray[0])) {
                    record = new HashMap<>();
                    record.put("account", tempArray[0]);
                    record.put("password", tempArray[1]);
                    resList.add(record);
                }

        }
        return resList;
    }

    public  List<Map<String, String>> readRecord() throws FileNotFoundException, IOException {
        File file = new File("test.txt");
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        String[] tempArray = null;
        List<Map<String, String>> resList = new ArrayList<>();
        Map<String, String> record;
        while ((temp = br.readLine()) != null) {
            record = new HashMap<>();
            tempArray = temp.split("@");
            record.put("account", tempArray[0]);
            record.put("uuid", tempArray[1]);
            record.put("HDCN", tempArray[2]);
            resList.add(record);
        }
        return resList;
    }
}
