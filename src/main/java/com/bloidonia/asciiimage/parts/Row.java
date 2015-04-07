package com.bloidonia.asciiimage.parts;

public class Row {
    private final int row;
    private final String rowString;

    public Row(int row, String rowString) {
        this.row = row;
        this.rowString = rowString;
    }

    public int getRow() {
        return row;
    }

    public String getRowString() {
        return rowString;
    }
}
