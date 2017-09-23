package javaGames.Wali.v1.AI;

import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.model.GameData.GameRound;
import javaGames.Wali.v1.model.GameData.GameState;
import javaGames.Wali.v1.*;
import java.math.*;
import javax.swing.*;
import java.util.*;

public class AI
	extends Thread
{
	private int ID;
	private PlayerData Me;
	private PlayerData You;
	private ArrayList<PieceData> lstMovablePieces = new ArrayList<PieceData>();
	byte[][] refM;

	public AI(int n)
	{
		super();
		ID = n;
		Me = Startup.dati.getPlayers()[Startup.dati.turno.ordinal()];
		You = Startup.dati.getPlayers()[(Startup.dati.turno.ordinal()+1)%2];
		refM = Startup.dati.getScacchiera().getMatrix();
	}

	public void run()
	{
		byte r=0;
		byte c=0;
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){}
		switch(Startup.dati.stato)
		{
			case Pos:
				//Controllo se l'avversario ha set di possibili tris.
				lstMovablePieces = getListOfMovablePieces(You);
				if	(
						(	lstMovablePieces != null && //Se la lista non è vuota E
							(getPieceCanDoTris(lstMovablePieces)) != null //Se in questa lista ci sono pedine che possono fare tris
						) && // E
						!(Startup.core.isContigui((byte)Me.nextCPUrow,(byte)Me.nextCPUcol)) // Se la posizione dove fare tris non è contigua alle mie pedine
					)
				{
					//Si: Blocco con mia pedina.
					r = (byte)Me.nextCPUrow;
					c = (byte)Me.nextCPUcol;
				}else{
					//No: Controllo se ho pedine già inserite.
					lstMovablePieces = getListOfMovablePieces(Me);
					PieceData p;
					if(lstMovablePieces != null && (p = getPieceNextTo(lstMovablePieces)) != null)
					{
						//Si: esiste una posizione contigua, mi posiziono.
						r = p.riga;
						c = p.colonna;
						//JOptionPane.showMessageDialog(null,"Ops: "+r+" "+c);
					}else{
						//No: Allora mi posiziono random.
						do{
							do{
								r = (byte)(Math.random()*refM.length);
								c = (byte)(Math.random()*refM[r].length);
							}while(refM[r][c] != 2);	//Controllo che la posizione scelta random sia vuota
						}while(Startup.core.isContigui(r,c));	//Controllo che la posizione scelta random non sia contigua
					}
				}
				Me.nextCPUrow = Me.nextCPUrow = -1;
			break;

			case Select:
				//Controllo se è possibile muovere almeno una pedina
				if(!Startup.core.noMove())
				{
					lstMovablePieces = getListOfMovablePieces(Me);
					//Tra quelle che posso muovere dare priorità a quelle che permettono il proprio tris
					PieceData p = getPieceCanDoTris(lstMovablePieces);
					if(p != null)
					{
						r = p.riga;
						c = p.colonna;
					}else{
						//Se non posso fare tris con una mossa allora controllo se esistono tris avversari che posso bloccare
						lstMovablePieces =  getPosCanDoTris(getListOfMovablePieces(You));
						boolean trovato = false;
						if(!lstMovablePieces.isEmpty())
						{
							//scandisco la lista delle posizioni da bloccare cercando una pedina contigua da posizionare
							int i = 0;
							while(i < lstMovablePieces.size() && !trovato)
							{
								//Cerco un pezzo che possa bloccare il potenziale tris avversario
								if((p = getPieceCanBlock(lstMovablePieces.get(i))) != null)
								{
									r = p.riga;
									c = p.colonna;
									Me.nextCPUrow = lstMovablePieces.get(i).riga;
									Me.nextCPUcol = lstMovablePieces.get(i).colonna;
									trovato = true;
								}
								i++;
							}
						}
						if(!trovato)
						{
							//Se non trovo nulla procedo con scomporre eventuali miei tris fatti.
							lstMovablePieces = getListOfMovablePieces(Me);
							//Seleziono la pedina che scompone il mio tris
							p = getPieceOfTris(lstMovablePieces);
							if(p == null)
							{
								//Se tale pedina non esiste seleziono una pedina random che non blocca un tris avversario
								lstMovablePieces = getListNoBlockTris(lstMovablePieces);
								if(!lstMovablePieces.isEmpty())
									p = lstMovablePieces.get((int)(Math.random()*lstMovablePieces.size()));
								else{
									// altrimenti procedo random
									p = getListOfMovablePieces(Me).get((int)Math.random()*lstMovablePieces.size());
								}
							}
							r = p.riga;
							c = p.colonna;
						}
					}
				}else{
					//Se non posso muovere nulla ne scelgo una a caso così passo il turno.
					PieceData p = Me.getPieceByIndex((int)(Math.random()*Me.getCurrentPieces()));
					r = p.riga;
					c = p.colonna;
				}
			break;

			case Move:
				if(Me.nextCPUrow != -1 && Me.nextCPUcol != -1)
				{
					r = (byte)Me.nextCPUrow;
					c = (byte)Me.nextCPUcol;
					Me.nextCPUrow = Me.nextCPUcol = -1;
				}else{
					int pos = -1;
					c = Startup.dati.getScacchiera().getColSel();
					r = Startup.dati.getScacchiera().getRigSel();
					do{
						int o = (int)(Math.random()*4);
						switch(o){
							case 0:
								if((r+1) < refM.length && refM[r+1][c] == 2)
								{
									pos = 0;
									r++;
								}
							break;

							case 1:
								if((c+1) < refM[0].length && refM[r][c+1] == 2)
								{
									pos = 0;
									c++;
								}
							break;

							case 2:
								if((r-1) >= 0 && refM[r-1][c] == 2)
								{
									pos = 0;
									r--;
								}
							break;

							case 3:
								if((c-1) >= 0 && refM[r][c-1] == 2)
								{
									pos = 0;
									c--;
								}
							break;
						}
					}while(pos == -1);
				}
			break;

			case DelPed:
				//Dare priorità alle pedine che possono permettere il tris all'avversario
				lstMovablePieces = getListOfMovablePieces(You);
				PieceData p;
				if(lstMovablePieces != null && (p = getPieceCanDoTris(lstMovablePieces)) != null )
				{
					r = p.riga;
					c = p.colonna;
					Me.nextCPUrow = Me.nextCPUcol = -1;
				}else{
					do{
						r = (byte)(Math.random()*refM.length);
						c = (byte)(Math.random()*refM[r].length);
					}while(refM[r][c] != ((Startup.dati.turno.ordinal()+1)%2) );
				}
			break;
		}
		int i= Startup.core.press(r,c);
		//JOptionPane.showMessageDialog(null,"errore:"+i+", riga:"+r+", colonna:"+c+" valore matrice:"+refM[r][c]);
	}

	public ArrayList<PieceData> getListNoBlockTris(ArrayList<PieceData> lstPieces)
	{
		ArrayList<PieceData> temp = new ArrayList<PieceData>();
		for(int i=0; i < lstPieces.size(); i++)
		{
			if(!isPieceBlocksTris(lstPieces.get(i)))
				temp.add(lstPieces.get(i));
		}
		return temp;
	}

	public boolean isPieceBlocksTris(PieceData p)
	{
		int r = p.riga;
		int c = p.colonna;

		//Controllo a nord
		if( (r-1) >= 0 && refM[r-1][c] == You.getID())
		{
			if( (r-2) >= 0 && refM[r-2][c] == You.getID())
			{
				if( (r+1) < refM.length && refM[r+1][c] == You.getID())
					return true;
				if( (c+1) < refM[0].length && refM[r][c+1] == You.getID())
					return true;
				if( (c-1) >= 0 && refM[r][c-1] == You.getID())
					return true;
			}
		}
		//Controllo ad est
		if( (c+1) < refM[0].length && refM[r][c+1] == You.getID())
		{
			if( (c+2) < refM[0].length && refM[r][c+2] == You.getID())
			{
				if( (r+1) < refM.length && refM[r+1][c] == You.getID())
					return true;
				if( (r-1) >= 0 && refM[r-1][c] == You.getID())
					return true;
				if( (c-1) >= 0 && refM[r][c-1] == You.getID())
					return true;
			}
		}

		//Controllo a sud
		if( (r+1) < refM.length && refM[r+1][c] == You.getID())
		{
			if((r+2) < refM.length && refM[r+2][c] == You.getID())
			{
				if( (r-1) >= 0 && refM[r-1][c] == You.getID())
					return true;
				if( (c+1) < refM[0].length && refM[r][c+1] == You.getID())
					return true;
				if( (c-1) >= 0 && refM[r][c-1] == You.getID())
					return true;
			}
		}

		//Controllo a ovest
		if( (c-1) >= 0 && refM[r][c-1] == You.getID())
		{
			if( (c-2) >=0 && refM[r][c-2] == You.getID())
			{
				if( (r+1) < refM.length && refM[r+1][c] == You.getID())
					return true;
				if( (c+1) < refM[0].length && refM[r][c+1] == You.getID())
					return true;
				if( (r-1) >= 0 && refM[r-1][c] == You.getID())
					return true;
			}

		}
		return false;
		/*if( r+2 < refM.length && refM[r+1][c] == refM[r+2][c] && refM[r+1][c] == You.getID())
			if(
			return false;
		if( r-2 >= 0 && refM[r-1][c] == refM[r-2][c] && refM[r-1][c] == You.getID())
			return false;
		if( c+2 < refM[0].length && refM[r][c+1] == refM[r][c+2] && refM[r][c+1] == You.getID())
			return false;
		if( c-2 >=0 0 && refM[r][c-1] == refM[r][c-2] && refM[r][c-1] == You.getID())
			return false;
		if( r+1 < refM.length &&
		return true;
		*/

	}

	public PieceData getPieceOfTris(ArrayList<PieceData> lstPieces)
	{
		for(int i=0; i < lstPieces.size(); i++)
			if(Startup.core.isContigui(lstPieces.get(i).riga, lstPieces.get(i).colonna))
				return lstPieces.get(i);
		return null;
	}

	public PieceData getPieceCanBlock(PieceData t)
	{
		//Ho la posizione vuota dove il mio avversario può fare tris, cerco una mia pedina nelle vicinanze per poter bloccare
		//la mossa avversaria.
		//NORD
		byte rig = (byte)(t.riga-1);
		byte col = t.colonna;
		if(rig >=0 && refM[rig][col] == Me.getID())
		{
			//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
			return new PieceData(rig,col);
		}
		//SUD
		rig = (byte)(t.riga+1);
		col = t.colonna;
		if(rig < refM.length && refM[rig][col] == Me.getID())
		{
			//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
			return new PieceData(rig,col);
		}
		//EST
		rig = t.riga;
		col = (byte)(t.colonna+1);
		if(col < refM[0].length && refM[rig][col] == Me.getID())
		{
			//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
			return new PieceData(rig,col);
		}
		//OVEST
		rig = t.riga;
		col = (byte)(t.colonna-1);
		if(col >=0 && refM[rig][col] == Me.getID())
		{
			//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
			return new PieceData(rig,col);
		}
		return null;
	}

	public ArrayList<PieceData> getListOfMovablePieces(PlayerData pl)
	{
		ArrayList<PieceData> lstTemp = new ArrayList<PieceData>();
		for(int i=0; i < pl.getCurrentPieces(); i++)
		{
			if(!Startup.core.isBlocked(pl.getPieceByIndex(i).riga, pl.getPieceByIndex(i).colonna))
			{
				lstTemp.add(new PieceData(pl.getPieceByIndex(i).riga, pl.getPieceByIndex(i).colonna));
			}
		}
		return lstTemp;
	}

	public ArrayList<PieceData> getPosCanDoTris(ArrayList<PieceData> lstPieces)
	{
		ArrayList<PieceData> lstTem = new ArrayList<PieceData>();
		PieceData movForTris;
		for(int i=0; i < lstPieces.size(); i++)
		{
			movForTris = getPieceCanTris(lstPieces.get(i));
			if(movForTris != null)
			{
				boolean trovato = false;
				int j = 0;
				while( j < lstTem.size() && !trovato)
				{
					//JOptionPane.showMessageDialog(null,"righe: "+lstTem.get(j).riga+" tris:"+movForTris.riga);
					//JOptionPane.showMessageDialog(null,"colon: "+lstTem.get(j).colonna+" tris:"+movForTris.colonna);
					if(lstTem.get(j).riga == movForTris.riga && lstTem.get(j).colonna == movForTris.colonna)
					{
						//JOptionPane.showMessageDialog(null,"sono");
						trovato = true;
					}
					j++;
				}
				if(!trovato)
				{
					//JOptionPane.showMessageDialog(null,"qui");
					lstTem.add(movForTris);
				}
			}
		}
		return lstTem;
	}

	//è un metodo che cerca tra i possibili pezzi della lista dei pezzi che posso muovere qualcuno che permette, con uno spostamento, il tris.
	//Appena ne trova uno esce restituendolo, altrimenti continua fino alla fine della lista restituendo null
	public PieceData getPieceCanDoTris(ArrayList<PieceData> lstPieces)
	{
		for(int i=0; i < lstPieces.size(); i++)
		{
			PieceData movForTris = getPieceCanTris(lstPieces.get(i));
			if( movForTris != null)
			{
				//da qualche parte devo salvare movForTris.riga,movForTris.colonna
				//if(Me.getID() == Startup.dati.turno.ordinal())
				//{
					Me.nextCPUrow = movForTris.riga;
					Me.nextCPUcol = movForTris.colonna;
				//}
				return lstPieces.get(i);
			}
		}
		return null;
	}

	//Dato un pezzo restituisce lo spostamento se quel pezzo può far tris, altrimenti restituisce null.
	public PieceData getPieceCanTris(PieceData p)
	{
		int r = p.riga;
		int c = p.colonna;
		PieceData temp;
		if((r+1) < refM.length && refM[r+1][c] == 2)
		{
			if(doTris(p, r+1, c)) return temp = new PieceData((byte)(r+1),(byte)c);
		}
		if((c+1) < refM[0].length && refM[r][c+1] == 2)
		{
			if(doTris(p, r, c+1)) return temp = new PieceData((byte)r,(byte)(c+1));
		}
		if((r-1) >= 0 && refM[r-1][c] == 2)
		{
			if(doTris(p, r-1, c)) return temp = new PieceData((byte)(r-1),(byte)c);
		}
		if((c-1) >= 0 && refM[r][c-1] == 2)
		{
			if(doTris(p, r, c-1)) return temp = new PieceData((byte)r,(byte)(c-1));
		}
		return null;
	}

	public PieceData getPieceNextTo(ArrayList<PieceData> lstPieces)
	{
		for(int i=0; i < lstPieces.size(); i++)
		{
			PieceData p = getRandomPosNextTo(lstPieces.get(i));
			if(p != null)
			{
				if(!Startup.core.isContigui(p.riga,p.colonna))
				{
					return p;
				}
			}
		}
		return null;
	}

	public PieceData getRandomPosNextTo(PieceData t)
	{
		byte rig;
		byte col;
		for(int i=0; i<4; i++){
			switch((int)(Math.random()*4)){
				case 0:
					//NORD
					rig = (byte)(t.riga-1);
					col = t.colonna;
					if(rig >=0 && refM[rig][col] == 2)
					{
						//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
						return new PieceData(rig,col);
					}
				break;

				case 1:
					//SUD
					rig = (byte)(t.riga+1);
					col = (byte)(t.colonna+1);
					if(rig < refM.length && col < refM[0].length && refM[rig][col] == 2)
					{
						//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
						return new PieceData(rig,col);
					}
				break;

				case 2:
					//EST
					rig = t.riga;
					col = (byte)(t.colonna+1);
					if(col < refM[0].length && refM[rig][col] == 2)
					{
						//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
						return new PieceData(rig,col);
					}
				break;

				case 3:
					//OVEST
					rig = (byte)(t.riga-1);
					col = (byte)(t.colonna-1);
					if(col >=0 && rig >=0 && refM[rig][col] == 2)
					{
						//JOptionPane.showMessageDialog(null,"riga: "+rig+" collona: "+col);
						return new PieceData(rig,col);
					}
				break;
			}
		}
		return null;
	}

	//metodo che restituisce true se quel pezzo può far tris a quelle determinate coordinate
	public boolean doTris(PieceData p, int r, int c)
	{
		byte prevR = p.riga;
		byte prevC = p.colonna;
		//Controllo a nord
		if( (r-1) >= 0 && refM[r-1][c] == refM[prevR][prevC] && ((r-1) != prevR || c != prevC))
		{
				if( (r-2) >= 0 && refM[r-2][c] == refM[prevR][prevC])
					return true;
				if( (r+1) < refM.length && refM[r+1][c] == refM[prevR][prevC] && ((r+1) != prevR || c != prevC))
					return true;
		}

		//Controllo ad est
		if( (c+1) < refM[0].length && refM[r][c+1] == refM[prevR][prevC] && (r != prevR || (c+1) != prevC))
		{
			if( (c+2) < refM[0].length && refM[r][c+2] == refM[prevR][prevC])
				return true;
			if( c-1 >= 0 && refM[r][c-1] == refM[prevR][prevC] && (r != prevR || (c-1) != prevC))
				return true;
		}

		//Controllo a sud
		if( (r+1) < refM.length && refM[r+1][c] == refM[prevR][prevC] && ((r+1) != prevR || c != prevC))
		{
			if((r+2) < refM.length && refM[r+2][c] == refM[prevR][prevC])
				return true;
			if((r-1) >= 0 && refM[r-1][c] == refM[prevR][prevC] && ((r-1) != prevR || c != prevC))
				return true;
		}

		//Controllo a ovest
		if( (c-1) >= 0 && refM[r][c-1] == refM[prevR][prevC] && (r != prevR || (c-1) != prevC))
		{
			if( (c-2) >=0 && refM[r][c-2] == refM[prevR][prevC])
				return true;
			if((c+1) < refM[0].length && refM[r][c+1] == refM[prevR][prevC] && (r != prevR || (c+1) != prevC))
				return true;
		}
		return false;
	}
}
