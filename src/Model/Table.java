package Model;

public class Table {
    private String tableName;
    private String tableStatus;
    private String tableCook;
    private String tableServe;
    private String tablePay;
    private String orderId;
    private String tableSeat;

    // Constructor đầy đủ
    public Table(String tableName, String tableStatus, String tableCook,
                    String tableServe, String tablePay, String orderId, String tableSeat) {
        this.tableName = tableName;
        this.tableStatus = tableStatus;
        this.tableCook = tableCook;
        this.tableServe = tableServe;
        this.tablePay = tablePay;
        this.orderId = orderId;
        this.tableSeat = tableSeat;
    }

    // Constructor rỗng
    public Table() {}

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public String getTableCook() {
        return tableCook;
    }

    public void setTableCook(String tableCook) {
        this.tableCook = tableCook;
    }

    public String getTableServe() {
        return tableServe;
    }

    public void setTableServe(String tableServe) {
        this.tableServe = tableServe;
    }

    public String getTablePay() {
        return tablePay;
    }

    public void setTablePay(String tablePay) {
        this.tablePay = tablePay;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableSeat() {
        return tableSeat;
    }

    public void setTableSeat(String tableSeat) {
        this.tableSeat = tableSeat;
    }
}