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

        Travel Classes:
        0 : First
        1 : Business
        2 : Premium Economy
        3 : Economy
     */
    private ArrayList<Cabin> cabins;
    private int selection;

    public SeatConfigObject() {
        cabins = new ArrayList<>();
        addCabin();
        selectCabin(0);
    }

    public void parse(String seatConfigString) {
        if (seatConfigString.length() == 0)
            return;
        cabins = new ArrayList<>();
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
                else if (c == '_' && seatConfigString.charAt(i+2) != 'e') {
                    cabins.get(selection).setTravelClass(Character.getNumericValue(seatConfigString.charAt(i+1)));
                    addCabin();
                    selection++;
                    numRow = 0;
                    i++;
                }
                else if (c == '_') {
                    cabins.get(selection).setTravelClass(Character.getNumericValue(seatConfigString.charAt(i+1)));
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
            result = result.concat(Integer.toString(cabins.get(i).getTravelClass()));
        }
        return  result.concat("e");
    }

    public int getSeatsInClass(int travelClass) {
        int result = 0;
        for (int i = 0; i < cabins.size(); i++) {
            if (cabins.get(i).getTravelClass() == travelClass) {
                result += cabins.get(i).seatCount();
            }
        }
        return result;
    }

    public int getTotalSeats() {
        return getSeatsInClass(0) + getSeatsInClass(1) + getSeatsInClass(2) + getSeatsInClass(3);
    }
}