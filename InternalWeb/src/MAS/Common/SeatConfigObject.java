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

    public void selectCabin(int val) {
        if (val < cabins.size())
            selection = val;
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

class Cabin {

    private ArrayList<Character> row;
    private int numRows;

    public Cabin() {
        row = new ArrayList<Character>();
    }

    public void addSeat() {
        row.add('s');
    }

    public void addCorridor() {
        row.add('|');
    }

    public void setNumRows(int val) {
        numRows = val;
    }

    public void addRow() {
        numRows++;
    }

    public void removeLast() {
        row.remove(row.size() - 1);
    }

    public void removeRow() {
        if (numRows > 0)
            numRows--;
    }

    public String toString() {
        String result = "";
        String oneRow;

        if (row.size() == 0)
            return result;

        StringBuilder builder = new StringBuilder(row.size());
        for(Character ch: row)
        {
            builder.append(ch);
        }
        oneRow = builder.toString();
        for (int i = 0; i < numRows; i ++) {
            result = result.concat(oneRow);
            result = result.concat("/");
        }
        return  result;
    }
}