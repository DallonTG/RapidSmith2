/*
 * Copyright (c) 2016 Brigham Young University
 *
 * This file is part of the BYU RapidSmith Tools.
 *
 * BYU RapidSmith Tools is free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * BYU RapidSmith Tools is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License is included with the BYU
 * RapidSmith Tools. It can be found at doc/LICENSE.GPL3.TXT. You may
 * also get a copy of the license at <http://www.gnu.org/licenses/>.
 */
package edu.byu.ece.rapidSmith.device;

import java.io.Serializable;

/**
 * A Wire is describes simply as an integer representing the wire and
 * a row/column tile offset from the source wire. It makes little sense
 * by itself and is only understood in the context of the HashMap found in
 * Tile.HashMap&lt;Integer,Wire[]&gt; which provides all of the wire connections
 * from a wire existing to an array of Wire objects which describe wires
 * that maybe in the same tile or other tiles nearby depending on the
 * row and column offset.  These connections also may be PIP connections
 * if the boolean isPIP is true.
 * @author Chris Lavin
 *
 */
public class WireConnection implements Serializable, Comparable<WireConnection>{
	private static final long serialVersionUID = 8614891405695500370L;
	/** The wire enumeration value of the wire to be connected to */
	private int wire;
	/** The tile row offset from the source wire's tile */
	private int rowOffset;
	/** The tile column offset from the source wire's tile */
	private int columnOffset;
	/** Does the source wire connected to this wire make a PIP? */
	private boolean isPIP;

	public WireConnection(){
		this.wire = -1;
		this.rowOffset = 0;
		this.columnOffset = 0;
		this.setPIP(false);
	}

	public WireConnection(int wire, int rowOffset, int columnOffset, boolean pip){
		this.wire = wire;
		this.rowOffset = rowOffset;
		this.columnOffset = columnOffset;
		this.setPIP(pip);
	}

	/**
	 * @param wire the destination wire to set
	 */
	public void setWire(int wire) {
		this.wire = wire;
	}
	/**
	 * @return the destination wire
	 */
	public int getWire() {
		return wire;
	}

	/**
	 * Returns the sink tile of this wire connection relative to the specified
	 * source tile.
	 * @param currTile the source tile of this wire connection
	 * @return the sink tile of this wire connection
	 */
	public Tile getTile(Tile currTile) {
		return currTile.getDevice().getTile(currTile.getRow()-this.rowOffset, currTile.getColumn()-this.columnOffset);
	}


	public Tile getWireCacheTile(Device dev, Tile currTile){
		String name = currTile.getName().substring(0, currTile.getName().lastIndexOf("_")+1) +
				"X" + (currTile.getTileXCoordinate()+this.columnOffset) +
				"Y" + (currTile.getTileYCoordinate()+this.rowOffset);
		return dev.getTile(name);
	}

	public int getRowOffset() {
		return rowOffset;
	}

	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	public int getColumnOffset() {
		return columnOffset;
	}

	public void setColumnOffset(int columnOffset) {
		this.columnOffset = columnOffset;
	}

	/**
	 * @param isPIP the isPIP to set
	 */
	public void setPIP(boolean isPIP) {
		this.isPIP = isPIP;
	}

	/**
	 * Does the source wire connected to this wire make a PIP?
	 * @return true if this wire connection is a PIP
	 */
	public boolean isPIP() {
		return isPIP;
	}

	@Override
	public int hashCode(){
		return  ((this.rowOffset << 24) & 0xFF000000) | ((this.columnOffset << 16) & 0x00FF0000) |(this.wire);
	}

	public int compareTo(WireConnection w){
		return (this.columnOffset + this.rowOffset + this.wire) - (w.columnOffset + w.rowOffset + w.wire);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WireConnection other = (WireConnection) obj;
		return columnOffset == other.columnOffset &&
				isPIP == other.isPIP &&
				rowOffset == other.rowOffset &&
				wire == other.wire;
	}

	@Override
	public String toString(){
		return this.wire +" "+ this.rowOffset +" "+ this.columnOffset + " ";
	}

	public String toString(WireEnumerator we){
		return we.getWireName(this.wire) +"("+ this.rowOffset +","+ this.columnOffset +","+ this.isPIP + ")";
	}
}
