package javaGames.Halma.v0.controller;

import javaGames.Halma.v0.model.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;
import javaGames.Halma.v0.AI.*;

import java.util.ArrayList;
import javax.swing.*;

public class Core
{
	private AI PlayerIntelligence;
	private posData PedSel;
	private posData current;

	public Core()
	{
		Startup.dati.initPiece();
	}

	public void press(int _i,int _j)
	{
		posData temp = new posData(_i,_j);
		if(Startup.dati.stato==0 && Startup.dati.Player[Startup.dati.turno].isOfPlayer(temp)){
			PedSel=temp;
			current=PedSel;
			Startup.dati.stato=1;
		}else if(Startup.dati.stato==1 && ( (temp.x == PedSel.x && temp.y == PedSel.y) ||   (Startup.dati.scacchiera[_i][_j]==2) )){
			if(temp.x == PedSel.x && temp.y == PedSel.y){
				reset();
			}else{
				if(chkmossa( PedSel, temp)){
					Startup.dati.stato = setfase(PedSel,temp);
					Startup.dati.scacchiera[current.y][current.x]=2;
					Startup.dati.Player[Startup.dati.turno].deletePiece(current);
					current=temp;
					Startup.dati.scacchiera[current.y][current.x]=Startup.dati.turno;
					Startup.dati.Player[Startup.dati.turno].pedine.add(current);
				}
			}
		}else if(Startup.dati.stato==2 && Startup.dati.scacchiera[_i][_j]== 2){
			if(temp.x == PedSel.x && temp.y == PedSel.y ){
				reset();
			}
			else if(chkmossa( current, temp) && setfase(current,temp) == 2){
					Startup.dati.stato=setfase(current,temp);
					Startup.dati.scacchiera[current.y][current.x]=2;
					Startup.dati.Player[Startup.dati.turno].deletePiece(current);
					current=temp;
					Startup.dati.scacchiera[current.y][current.x] = Startup.dati.turno;
					Startup.dati.Player[Startup.dati.turno].pedine.add(current);
			}
		}
		if(chkEnd()){
			JOptionPane.showMessageDialog(null, "HA VINTO " + Startup.dati.Player[Startup.dati.turno].simbolo);
			Startup.finestra.dispose();
		}
		Startup.finestra.refresh ();
	}

	public posData getPedSel()
	{
		return current;
	}

	public byte setfase(posData PP, posData PA)
	{
		int DiffR=PP.y-PA.y;
		int DiffC=PP.x-PA.x;
		if(Math.abs(DiffR)>1 || Math.abs(DiffC)>1)
			return 2;
		else
			return  3;
	}

	public boolean chkmossa(posData PP, posData PA)
	{
		int DiffR=PA.y-PP.y;
		int DiffC=PA.x-PP.x;
		if(Math.abs(DiffR) > 2 || Math.abs(DiffC) > 2)
			return false;
		if(DiffR == 0 && Math.abs(DiffC) == 2){
			int c=PP.x;
			do{
				if(DiffC > 0)
					c++;
				else
					c--;
				if(Math.abs(c-PP.x) == 1 && Startup.dati.scacchiera[PP.y][c] == 2)
						return false;
				if (Math.abs(c-PP.x) == 2 && Startup.dati.scacchiera[PP.y][c] !=2 )
					return false;
			}while(Math.abs(c-PP.x) < 2);
		}else if(DiffC == 0 && Math.abs(DiffR) == 2){
			int r=PP.y;
			do{
				if(DiffR > 0)
					r++;
				else
					r--;
				if(Math.abs(r-PP.y) == 1 && Startup.dati.scacchiera[r][PP.x] == 2)
						return false;
				if (Math.abs(r-PP.y) == 2 && Startup.dati.scacchiera[r][PP.x] !=2 )
					return false;
			}while(Math.abs(r-PP.x) < 2);
		} else if(Math.abs(DiffC) == 2 && Math.abs(DiffR) == 2){
			int c = PP.x;
			int r = PP.y;
			do{
				if(DiffC > 0) c++;
				else c--;
				if(DiffR > 0)r++;
				else r--;
				if(Math.abs(r-PP.y) == 1 && Math.abs(c-PP.x) == 1  && Startup.dati.scacchiera[r][c] == 2)
					return false;
				if (Math.abs(r-PP.y) == 2  && Math.abs(c-PP.x) == 2 && Startup.dati.scacchiera[r][c] !=2 )
					return false;
			}while(Math.abs(r-PP.y) < 2);
		}
		if( (Math.abs(DiffR) == 1 &&  Math.abs(DiffC) == 2) || (Math.abs(DiffR) == 2 &&  Math.abs(DiffC) == 1))
			return false;
		else
			return true;
	}


	public void nextTurn()
	{
		PedSel=null;
		current=null;
		if(Startup.dati.turno==0)Startup.dati.turno=1;
		else Startup.dati.turno=0;
		Startup.dati.stato=0;
		Startup.finestra.refresh();
		if(Startup.dati.Player[Startup.dati.turno].isCpu == true){
			makeAI();
		}
	}

	public void makeAI()
	{
		PlayerIntelligence = new AI(Startup.dati.Player[Startup.dati.turno].pedine);
		PlayerIntelligence.start();
	}

	public void reset()
	{
		Startup.dati.scacchiera[current.y][current.x]=2;
		Startup.dati.Player[Startup.dati.turno].deletePiece(current);
		Startup.dati.scacchiera[PedSel.y][PedSel.x]=Startup.dati.turno;
		Startup.dati.Player[Startup.dati.turno].pedine.add(PedSel);
		PedSel=null;
		current=null;
		Startup.dati.stato=0;
		Startup.finestra.refresh();
	}

	public boolean chkEnd()
	{
		ArrayList<posData> t = Startup.dati.Player[Startup.dati.turno].pedine;
		for(int i = 0; i < t.size(); i++ ){
			if(Startup.dati.chkPos(t.get(i).y,t.get(i).x) != (Startup.dati.turno + 1) % 2){
				return false;
			}
		}
		return true;
	}
}
