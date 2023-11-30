import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class DataRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String NAMES_FILE_PATH = "DeviceNames.ser";
    private static final String DETAILS_FILE_PATH = "DeviceDetails.ser";
    private static final String USERS_FILE_PATH = "UserNames.ser";
    private static final String USER_ASSIGNMENTS_PATH = "UserAssignment.ser";
    private static final String[] DEVICE_TYPES = {"Desktop", "Laptop", "Tablet", "Monitor", "Input device"};
    private static DataRepository instance;
    private ArrayList<String> deviceNames = new ArrayList<>();
    private ArrayList<String> deviceDetails = new ArrayList<>();
    private ArrayList<UserInfo> users = new ArrayList<>();
    private ArrayList<String> userAssignments = new ArrayList<>();
    private Map<String, ArrayList<String>> deviceTypeMap = new HashMap<>();
    
    

    private DataRepository() {
        loadNamesFromFile();
        loadDetailsFromFile();
        loadUsersFromFile();
        loadUserAssignmentsFromFile();
        

        for (String deviceType : DEVICE_TYPES) {
            deviceTypeMap.put(deviceType, new ArrayList<>());
            loadDeviceTypeFromFile(deviceType);
        }
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public ArrayList<String> getDeviceDetails() {
        return deviceDetails;
    }

    public ArrayList<String> getDeviceNames() {
        return deviceNames;
    }

    public ArrayList<UserInfo> getUserName() {
        return users;
    }

    public ArrayList<UserInfo> getUsers() {
        return users;
    }

    public void addDevice(String deviceType, String deviceName, String deviceDetail) {
        String deviceInfo = "Device type: " + deviceType + "\nDevice name: " + deviceName + "\n" + deviceDetail;
        deviceNames.add(deviceName);
        deviceDetails.add(deviceInfo);
        saveNamesToFile();
        saveDetailsToFile();

        deviceTypeMap.get(deviceType).add(deviceName);
        saveDeviceTypeToFile(deviceType);
    }

    public ArrayList<String> getDeviceType(String deviceType) {
        return deviceTypeMap.getOrDefault(deviceType, new ArrayList<>());
    }

    // Save and load devices by type
    private void saveDeviceTypeToFile(String deviceType) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(deviceType + ".ser"))) {
            oos.writeObject(deviceTypeMap.get(deviceType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    private void loadDeviceTypeFromFile(String deviceType) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(deviceType + ".ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                    deviceTypeMap.put(deviceType, (ArrayList<String>) obj);
            } else {
                deviceTypeMap.put(deviceType, new ArrayList<>());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            deviceTypeMap.put(deviceType, new ArrayList<>());
        }
    }

    private void saveNamesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NAMES_FILE_PATH))) {
            oos.writeObject(deviceNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDetailsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DETAILS_FILE_PATH))) {
            oos.writeObject(deviceDetails);
            loadDetailsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadNamesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NAMES_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                deviceNames = (ArrayList<String>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadDetailsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DETAILS_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                deviceDetails = (ArrayList<String>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> getDeviceByType(String deviceType) {
        ArrayList<String> filteredDevices = new ArrayList<>();
        
        for (int i = 0; i < deviceDetails.size(); i++) {
            if (deviceDetails.get(i).startsWith("Device type: " + deviceType)) {
                filteredDevices.add(deviceNames.get(i));
            }
        }
        return filteredDevices;
        
    }

    public boolean isValidIndex(int index) {
        return index >=0 && index < deviceNames.size() && index < deviceDetails.size();
    }

    public void addUser(String userName) {
        if (!userExists(userName)) {
            UserInfo userInfo = new UserInfo(userName);
            users.add(userInfo);
            saveUsersToFile();
        }
    }

    public void updateDeviceDetails(int index, String updatedDetails) {
        if (isValidIndex(index)) {
            String currentDetails = deviceDetails.get(index);
            String[] currentParts = currentDetails.split("\n");
    
            if (currentParts.length == 6) {
                String deviceType = currentParts[0].substring("Device type: ".length());
                String deviceName = currentParts[1].substring("Device name: ".length());
                String updatedDeviceDetails = currentParts[2] + "\n" + currentParts[3] + "\n" + currentParts[4] + "\n" + currentParts[5];
    
                String[] updatedParts = updatedDetails.split("\n");
    
                if (updatedParts.length == 6) {
                    updatedDeviceDetails = updatedParts[2] + "\n" + updatedParts[3] + "\n" + updatedParts[4] + "\n" + updatedParts[5];
                    // Preserve the existing device type and name
                    deviceType = updatedParts[0].substring("Device type: ".length());
                    deviceName = updatedParts[1].substring("Device name: ".length());
                }
    
                // Combine device type, name, and updated details
                String combinedDetails = "Device type: " + deviceType + "\nDevice name: " + deviceName + "\n" + updatedDeviceDetails;
                deviceDetails.set(index, combinedDetails);
    
                saveDetailsToFile();
                saveNamesToFile();
            } else {
                System.out.println("Invalid format for updated details.");
            }
        } else {
            System.out.println("Invalid index: " + index);
        }
    }
    
    private boolean userExists(String userName) {
        for (UserInfo user : users) {
            if (user.getUserName().equals(userName)) {
                return true; // user already exists
            }
        }
        return false;
    }

    public void assignDeviceToUser(String userName, String deviceName, String InventoryItem) {
        String assignment = "User: " + userName + "\nDevice: " + deviceName +"\n" + deviceDetails;
        userAssignments.add(assignment);
        saveUserAssignmentsToFile();
    }

    public ArrayList<String> getAssignedDevices(String userName) {
        ArrayList<String> assignedDevices = new ArrayList<>();
        for (String assignment : userAssignments) {
            if (assignment.startsWith("User: " + userName)) {
                String[] parts = assignment.split("\n");
                if (parts.length > 1) {
                    String deviceName = parts[1].substring("Device: ".length());
                    assignedDevices.add(deviceName);
                }               
            }
        }
        return assignedDevices;
    }

    public void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUserAssignmentsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_ASSIGNMENTS_PATH))) {
            oos.writeObject(userAssignments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                users = (ArrayList<UserInfo>) obj;
            }
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void loadUserAssignmentsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_ASSIGNMENTS_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                userAssignments = (ArrayList<String>) obj;
            } else {
                userAssignments = new ArrayList<>();
            }
        } catch (FileNotFoundException e ) {
            userAssignments = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            userAssignments = new ArrayList<>();
        }
    }

    public void removeDevice(int index) {
        if (isValidIndex(index)) {
            deviceNames.remove(index);
            deviceDetails.remove(index);
            saveNamesToFile();
            saveDetailsToFile();
        }
    }

    public void removeUser(int index) {
        if (isValidIndex(index)) {
            UserInfo removedUser = users.remove(index);
            userAssignments.removeIf(assignment -> assignment.startsWith(removedUser.getUserName()));
            saveUsersToFile();
            saveUserAssignmentsToFile();
        }
    }
    
    
}