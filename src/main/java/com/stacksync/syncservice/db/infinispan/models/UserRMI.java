package com.stacksync.syncservice.db.infinispan.models;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRMI implements Serializable{

    public UUID id;
    private String name;
    private String swiftUser;
    private String swiftAccount;
    private String email;
    private Integer quotaLimit;
    private Integer quotaUsed;
    private List<DeviceRMI> devices;
    private List<UUID> workspaces;

    @Deprecated
    public UserRMI() {}

    public UserRMI(UUID id) {
        this(id, "", "", "", "", 0, 0);
    }

    public UserRMI(UUID id, String name, String swiftUser, String swiftAccount, String email, Integer quotaLimit, Integer quotaUsed) {
        this.id = id;
        this.name = name;
        this.swiftUser = swiftUser;
        this.swiftAccount = swiftAccount;
        this.email = email;
        this.quotaLimit = quotaLimit;
        this.quotaUsed = quotaUsed;
        this.devices = new ArrayList<>();
        this.workspaces = new ArrayList<>();
    }

    public void setUser(UserRMI usr) {
        this.id = usr.getId();
        this.name = usr.getName();
        this.swiftUser = usr.getSwiftUser();
        this.swiftAccount = usr.getSwiftAccount();
        this.email = usr.getEmail();
        this.quotaLimit = usr.getQuotaLimit();
        this.quotaUsed = usr.getQuotaUsed();
        this.devices = usr.getDevices();
        this.workspaces = usr.getWorkspaces();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwiftUser() {
        return swiftUser;
    }

    public void setSwiftUser(String swiftUser) {
        this.swiftUser = swiftUser;
    }

    public String getSwiftAccount() {
        return swiftAccount;
    }

    public void setSwiftAccount(String swiftAccount) {
        this.swiftAccount = swiftAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getQuotaLimit() {
        return quotaLimit;
    }

    public void setQuotaLimit(Integer quotaLimit) {
        this.quotaLimit = quotaLimit;
    }

    public Integer getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(Integer quotaUsed) {
        this.quotaUsed = quotaUsed;
    }

    public List<DeviceRMI> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceRMI> devices) {
        this.devices = devices;
    }

    public void addDevice(DeviceRMI device) {
        this.devices.add(device);
        }

    public void removeDevice(DeviceRMI device) {
        this.devices.remove(device);
    }

    public List<UUID> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<UUID> workspaces) {
        this.workspaces = workspaces;
    }

    public void addWorkspace(UUID workspace) {
        this.workspaces.add(workspace);
    }

    public void removeWorkspace(UUID workspace) {
        int index = 0;
        for (UUID ID : workspaces) {
            if (ID.equals(workspace)) {
                workspaces.remove(index);
                break;
            }
            index++;
        }
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, name=%s, swiftUser=%s, swiftAccount=%s, email=%s, quotaLimit=%s, quotaUsed=%s]", id, name,
                swiftUser, swiftAccount, email, quotaLimit, quotaUsed);
    }

    /**
     * Checks whether the user contains all required attributes (ID is not
     * required since it is assigned automatically when a user is inserted to
     * the database)
     *
     * @return Boolean True if the user is valid. False otherwise.
     */
    public boolean isValid() {
        return !(this.swiftUser == null || this.email == null || this.name == null || this.quotaLimit == null
                || this.quotaUsed == null);
    }

    //************************************
    //************************************
    //************** DEVICE **************
    //************************************
    //************************************
    public DeviceRMI getDevice(UUID id) throws RemoteException {

        for (DeviceRMI device : devices) {
            if (device.getId().equals(id)) {
                return device;
            }
        }
        return null;
    }

    public void updateDevice(DeviceRMI device) throws RemoteException {

        for (DeviceRMI currentDevice : devices) {
            if (currentDevice.getId().equals(device.getId())) {
                devices.remove(currentDevice);
                devices.add(device);
                break;
            }
        }
    }

    public void deleteDevice(UUID id) throws RemoteException {

        for (DeviceRMI currentDevice : devices) {
            if (currentDevice.getId().equals(id)) {
                devices.remove(currentDevice);
                break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserRMI userRMI = (UserRMI) o;

        return id.equals(userRMI.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}