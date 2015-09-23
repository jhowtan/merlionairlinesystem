package MAS.Common;

public class Cabin {

    private String row;
    private int numRows;

    public Cabin() {
        row = "";
        numRows = 1;
    }

    public void addSeat() {
        row = row.concat("s");
    }

    public void addCorridor() {
        row = row.concat("|");
    }

    public void setNumRows(int val) {
        numRows = val;
    }

    public void addRow() {
        numRows++;
    }

    public void removeLast() {
        if (row.length() > 0)
            row = row.substring(0, row.length() - 1);
    }

    public void removeRow() {
        if (numRows > 0)
            numRows--;
    }

    public String toString() {
        String result = "";
        if (row.length() == 0)
            return result;

        for (int i = 0; i < numRows; i ++) {
            result = result.concat(row);
            result = result.concat("/");
        }
        return  result;
    }
}