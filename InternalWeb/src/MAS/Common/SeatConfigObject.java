package MAS.Common;


import java.util.ArrayList;

public class SeatConfigObject {
    /*
    Reference for characters in seatConfig:
        s : seat
        | : corridor/aisle
        / : new row
        _ : new cabin
        e : end
     */

    private ArrayList<Cabin> cabins;
    private int selection;

    public SeatConfigObject() {
        cabins = new ArrayList<Cabin>();
        addCabin();
        selectCabin(0);
    }

    public void parse(String seatConfigString) {
        if (seatConfigString.length() == 0)
            return;
        cabins = new ArrayList<Cabin>();
        addCabin();
        selectCabin(0);
        int numRow = 0;
        for (int i = 0; i < seatConfigString.length(); i++) {
            char c = seatConfigString.charAt(i);
            if (c == 'e')
                break;
            if (numRow == 0) {
                if (c == 's')
                    addSeat();
                else if (c == '|')
                    addCorridor();
                else if (c == '/')
                    numRow = 1;
            }
            else {
                if (c == '/') {
                    numRow++;
                    setNumRows(numRow);
                }
                else if (c == '_' && seatConfigString.charAt(i+1) != 'e') {
                    addCabin();
                    selection++;
                    numRow = 0;
                }
            }
        }
        selectCabin(0);
    }

    public void addCabin() {
        cabins.add(new Cabin());
    }

    public boolean selectCabin(int val) {
        if (val < cabins.size()) {
            selection = val;
            return true;
        }
        return false;
    }

    public void addSeat() {
        cabins.get(selection).addSeat();
    }
    public void addCorridor() {
        cabins.get(selection).addCorridor();
    }
    public void removeLast() {
        cabins.get(selection).removeLast();
    }
    public void setNumRows(int val) {
        cabins.get(selection).setNumRows(val);
    }
    public String rowString() {
        return cabins.get(selection).toString();
    }

    public ArrayList<Cabin> getCabins() {
        return cabins;
    }

    public String toString() {
        String result = "";
        if (cabins.size() == 0)
            return  result;
        for (int i = 0; i < cabins.size(); i++) {
            result = result.concat(cabins.get(i).toString());
            result = result.concat("_");
        }
        return  result.concat("e");
    }
}