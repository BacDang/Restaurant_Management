/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class Customer {

    private String tenKhach;
    private String soDienThoai;
    private int tongChi;
    private int soLuongDon;

    public Customer(String tenKhach, String soDienThoai, int tongChi, int soLuongDon) {
        this.tenKhach = tenKhach;
        this.soDienThoai = soDienThoai;
        this.tongChi = tongChi;
        this.soLuongDon = soLuongDon;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getTongChi() {
        return tongChi;
    }

    public void setTongChi(int tongChi) {
        this.tongChi = tongChi;
    }

    public int getSoLuongDon() {
        return soLuongDon;
    }

    public void setSoLuongDon(int soLuongDon) {
        this.soLuongDon = soLuongDon;
    }

}
