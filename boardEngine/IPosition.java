package javaGames.boardEngine;

// Rappresenta una posizione nella scacchiera (oggetto immutabile)
public interface IPosition
{
	public String toString();
	public int getRow();
	public int getCol();
}