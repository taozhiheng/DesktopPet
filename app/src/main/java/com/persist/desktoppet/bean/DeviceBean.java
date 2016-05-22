package com.persist.desktoppet.bean;

/**
 * Created by taozhiheng on 16-5-4.
 *
 * bluetooth device info:
 * name:device name
 * address device mac address
 */
public class DeviceBean {
    public String name;
    public String address;

    public DeviceBean(String name, String address)
    {
        this.name = name;
        this.address = address;
    }
}
