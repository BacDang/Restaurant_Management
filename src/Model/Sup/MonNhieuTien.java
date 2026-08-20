/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Sup;

/**
 *
 * @author Admin
 */
public class MonNhieuTien {
    private String tenMon;
    private int tongTien;

    public MonNhieuTien(String tenMon, int tongTien) {
        this.tenMon = tenMon;
        this.tongTien = tongTien;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }
    
}
