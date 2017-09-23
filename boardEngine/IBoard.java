package javaGames.boardEngine;

public interface IBoard
{
	// Reset della scacchiera
	public void reset();

	// Ritorna TRUE se la casella è vuota
	public boolean isEmpty(final IPosition pos);

	// Ritorna il pezzo contenuto in pos
	public IPiece getPiece(final IPosition pos);

	// Rimuove un pezzo
	public void removePiece(final IPosition pos);

	// Imposta un pezzo
	public void setPiece(final IPosition pos, final IPiece IPiece);

	// Ritorna una posizione date le coordinate
	public IPosition getPosition(final int x, final int y) throws InvalidPositionException;

	// Ritorna una posizione data la stringa
	public IPosition getPosition(final String str) throws InvalidPositionException;

	// Get methods
	public int getRows();
	public int getCols();
}