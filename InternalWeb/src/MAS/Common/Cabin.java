package MAS.Common;

import java.util.Arrays;

public class Cabin {

    public static String[] TRAVEL_CLASSES = {
            "First",
            "Business",
            "Premium Economy",
            "Economy"
    };
    private String row;
    private int numRows;
    private int travelClass;

    public Cabin() {
        travelClass = 3;
        row = "";
        numRows = 1;
    }

    public void addSeat() {
        row = row.concat("s");
    }

    public void addCorridor() {
        row = row.concat("|");
    }

    public int getNumRows() {
        return numRows;
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

    public int seatCount() {
        int result = 0;
        for (int i = 0; i < row.length(); i++){
            char c = row.charAt(i);
            if (c == 's')
                result++;
        }
        return result;
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

    public String getRepresentation() {
        return row;
    }

    //TODO: Check whether removing this function breaks createSeatConfig page
    public void setRepresentation(String val) { }

    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }
    public int getClassIndex(String travelClass) {
        return Arrays.asList(Cabin.TRAVEL_CLASSES).indexOf(travelClass);
    }

    public String[] getTravelClasses() {
        return TRAVEL_CLASSES;
    }
}