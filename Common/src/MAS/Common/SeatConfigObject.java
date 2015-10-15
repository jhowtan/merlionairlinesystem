package MAS.Common;


import MAS.Exception.NotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<Integer> takenSeats;
    private static final String[] ALPHABETS = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R"};//No "I"
    private List<Integer> templateSeats;


    public SeatConfigObject() {
        cabins = new ArrayList<>();
        addCabin();
        selectCabin(0);
        takenSeats = new ArrayList<>();
        templateSeats = new ArrayList<>();
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

    public void addTakenSeats(List<Integer> seats) {
        for (int i = 0; i < seats.size(); i++) {
            takenSeats.add(seats.get(i));
        }
    }

    public List<Integer> getAvailableSeats() {
        if (templateSeats == null || templateSeats.size() != getTotalSeats()) {
            templateSeats = new ArrayList<>();
            int l = getTotalSeats();
            for (int i = 0; i < l; i++) {
                templateSeats.add(i);
            }
        }
        List<Integer> result = new ArrayList<>(templateSeats);
        result.removeAll(takenSeats);

        return result;
    }

    public String convertIntToString(int seatNumber) throws NotFoundException {
        if (seatNumber > getTotalSeats())
            throw new NotFoundException();
        int inCabin = findInCabin(seatNumber);
        int row = 0;
        String column = "";
        for (int i = 0; i < inCabin; i++) {
            seatNumber -= cabins.get(i).seatCount();
            row += cabins.get(i).getNumRows();
        }
        row += (seatNumber / cabins.get(inCabin).getRowLength()) + 1;
        column = ALPHABETS[seatNumber % cabins.get(inCabin).getRowLength()];
        return Integer.toString(row).concat(column);
    }

    public int convertStringToInt(String seatNumber) throws NotFoundException {
        int result = 0;
        if (seatNumber.length() > 4) {
            throw new NotFoundException();
        }
        else if (!seatNumber.substring(seatNumber.length() - 1 ,seatNumber.length()).matches("[a-zA-Z]+"))
        {
            throw new NotFoundException();
        }
        int column = Arrays.asList(ALPHABETS).indexOf(seatNumber.substring(seatNumber.length() - 1 ,seatNumber.length()));
        int row = 0;
        if (column == -1)
            throw new NotFoundException();
        try {
            row = Integer.parseInt(seatNumber.substring(0, seatNumber.length() - 1));
        } catch (Exception e) {
            throw new NotFoundException();
        }
        int inCabin = findRowInCabin(row);
        for (int i = 0; i < inCabin; i++) {
            result += cabins.get(i).seatCount();
            row -= cabins.get(i).getNumRows();
        }
        result += (row - 1) * cabins.get(inCabin).getRowLength();
        result += column;

        return result;
    }

    private int findInCabin(int seatNumber) throws NotFoundException {
        for (int i = 0; i < cabins.size(); i++) {
            Cabin currCabin = cabins.get(i);
            if (seatNumber < currCabin.seatCount())
                return i;
            else
                seatNumber -= currCabin.seatCount();
        }
        throw new NotFoundException();
    }

    private int findRowInCabin(int rowNumber) throws NotFoundException {
        for (int i = 0; i < cabins.size(); i++) {
            Cabin currCabin = cabins.get(i);
            if (rowNumber < currCabin.getNumRows())
                return i;
            else
                rowNumber -= currCabin.getNumRows();
        }
        throw new NotFoundException();
    }
}