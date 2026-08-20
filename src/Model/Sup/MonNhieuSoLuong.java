/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Sup;

/**
 *
 * @author Admin
 */
public class MonNhieuSoLuong {
    private String tenMon;
    private int soLuong;

    public MonNhieuSoLuong(String tenMon, int soLuong) {
        this.tenMon = tenMon;
        this.soLuong = soLuong;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
}
