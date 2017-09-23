package javaGames.Halma.v0.AI;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.model.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;

import java.util.*;

public class AI extends Thread
{
	private ArrayList<posData> myPed;
	int ID;

	public AI(ArrayList<posData> p)
	{
		ID  = (int) Startup.dati.turno;
		myPed = p;
	}

	//Metodo che trova la mossa da effettuare
	public ArrayList<posData> makeStep()
	{
		ArrayList<ArrayList<TreeNodo>> mosse;
		ArrayList<posData> best = null;
		ArrayList<posData> temp = new ArrayList<posData>();
		//Mi faccio restituire la lista delle pedine che posso muovere
		ArrayList<posData> lstMovablePed = getListOfMovablePed(myPed);
		//Controllo la presenza di pedine
		if(!lstMovablePed.isEmpty()) {
			/*best = new ArrayList<posData>();
			best = findBestStep(lstMovablePed);*/
			for(int i = 0; i < lstMovablePed.size(); i++) {
				mosse = findTree(lstMovablePed.get(i));
				if(!mosse.isEmpty()) {
					//Per ogni albero cerco il percorso migliore per la pedina
					temp = choosePath(mosse, lstMovablePed.get(i));
				}
				// Vedo se muovere questa pedina è meglio di muovere quella
				if((best==null || best.isEmpty()) || (temp != null && chkPath(best,temp)) ) {
					best = temp;
				}
			}
		}else{
			//Se non ce ne sono, spostiamo quelle già infilate
			lstMovablePed = getListOfInPed(myPed);
			best = new ArrayList<posData>();
			best.add(lstMovablePed.get((int)(Math.random()*lstMovablePed.size())));
			lstMovablePed = findMove(best.get(0));
			best.add(lstMovablePed.get((int)(Math.random()*lstMovablePed.size())));
		}
		return best; //Ritorno l'array di posizioni con la mossa migliore
	}

	public ArrayList<posData> findBestStep(ArrayList<posData> lstP)
	{
		posData posInit = lstP.get(0);
		posData posFin = null;
		posData posTemp;
		ArrayList<posData> best = new ArrayList<posData>();
		for(int i=0; i < lstP.size(); i++) {
			posTemp = getBestMove(lstP.get(i)) ;
			if(posTemp!=null) {
				if(ID == 1) {
					if(posFin==null || posFin.priority() < posTemp.priority()) {
						posInit = lstP.get(i);
						posFin = posTemp;
					}
				} else {
					if(posFin==null || posFin.priority() > posTemp.priority()) {
						posInit = lstP.get(i);
						posFin = posTemp;
					}
				}
			}
		}
		best.add(posInit);
		best.add(posFin);
		return best;
	}

	public posData getBestMove(posData p)
	{
		posData temp = null;
		posData best = null;
		for(int i= (p.y == 0)? 0 : p.y-1; i < p.y+2 && i < Startup.dati.scacchiera.length; i++) {
			for(int j= (p.x == 0) ? 0 : p.x-1; j <= p.x+2 && j < Startup.dati.scacchiera[i].length; j++) {
				temp = new posData(i,j);
				if(!p.equals(temp) && Startup.dati.scacchiera[i][j]==2 && Startup.core.chkmossa(p,new posData(i,j))) {
					if(ID == 1) {
						if(best==null || best.priority() < temp.priority()) {
							best = temp;
						}
					} else {
						if(best==null || best.priority() > temp.priority()) {
							best = temp;
						}
					}
				}
			}
		}
		return best;
	}

	public ArrayList<posData> getListOfMovablePed(ArrayList<posData> p)
	{
		ArrayList<posData> temp = new ArrayList<posData>();
		for(int i=0; i < p.size(); i++) {
			if(!isBlocked(p.get(i)) && !isIn(p.get(i))) {
				temp.add(p.get(i));
			}
		}
		return temp;
	}

	public ArrayList<posData> getListOfInPed(ArrayList<posData> p)
	{
		ArrayList<posData> temp = new ArrayList<posData>();
		for(int i=0; i < p.size(); i++) {
			if(!isBlocked(p.get(i)) && isIn(p.get(i))) {
				temp.add(p.get(i));
			}
		}
		return temp;
	}

	public boolean isBlocked(posData p)
	{
		//Creo una lista dei possibili spostamenti della pedina
		ArrayList<posData> temp = findMove(p);
		if(temp.isEmpty()) {
			//Se tale lista è vuota, non può fare spostamenti
			return true;
		}
		return false;
	}

	//cerco tutti i possibili spostamenti della singola pedina e li inserisco dentro una lista
	public ArrayList<posData> findMove(posData p)
	{
		ArrayList<posData> temp = new ArrayList<posData>();
		int iLimit = p.y - 2;
		int jLimit = p.x - 2;
		if (iLimit < 0) iLimit = 0;
		if (jLimit < 0) jLimit = 0;
		for(int i = iLimit; i <= p.y + 2 &&  i < Startup.dati.scacchiera.length; i++)
			for(int j = jLimit; j <= p.x + 2 && j < Startup.dati.scacchiera[i].length; j++){
				posData t = new posData(i,j);
				if((Startup.dati.scacchiera[t.y][t.x]== 2 && Startup.core.chkmossa(p,t))) {
					temp.add(t);
				}
			}
		return temp;
	}

	public boolean isIn(posData p)
	{
		if (ID == Startup.dati.chkPos(p)) return true;
		return false;
	}

	public void run(){
		ArrayList<posData> bestStep = null;
		do {
			bestStep = makeStep();
		} while(bestStep==null);
		//Passa le varie posizioni al core
		for(int i = 0; i < bestStep.size(); i++){
			try {
				Thread.sleep(30);
			} catch(InterruptedException e){}
			//JOptionPane.showMessageDialog(null, best.get(i));
			Startup.core.press(bestStep.get(i).y, bestStep.get(i).x);
		}
		//Passa il turno
		Startup.core.nextTurn();
	}

	private boolean chkPath(ArrayList<posData> prec, ArrayList<posData> succ) {
		if((diffPriorita(succ) < diffPriorita(prec) && (Startup.dati.turno==1)) || (diffPriorita(succ) > diffPriorita(prec) && Startup.dati.turno==0))
			return true;
		return false;
	}

	public int diffPriorita(ArrayList<posData> p)
	{
		return p.get(0).priority() - p.get(p.size()-1).priority();
	}


	//Genera le prime mosse che la singola pedina può fare
	private ArrayList<ArrayList<TreeNodo>> findTree(posData root)
	{
		//Creo l'albero
		ArrayList<ArrayList<TreeNodo>> m = new ArrayList<ArrayList<TreeNodo>>();
		//Creo la lista delle possibili mosse della pedina root dell'albero
		ArrayList<TreeNodo> temp = new ArrayList<TreeNodo>();
		//Creo la lista delle posizioni dove posso spostare la root
		ArrayList<posData> posizioni = findMove(new TreeNodo(root,null),m);
		for(int i = 0; i < posizioni.size(); i++)
		{
			temp.add(new TreeNodo(
				posizioni.get(i), //Passo la posizione della pedina
				null, //La prima volta il padre è null
				Startup.core.setfase(root, posizioni.get(i)) //Controllo tipo di spostamento
			));
		}
		if(temp.isEmpty())
			return m;
		m.add(temp);
		findTree(0, m);
		return m;
	}

	private void findTree(int livPrec, ArrayList<ArrayList<TreeNodo>> m)
	{
		ArrayList temp = new ArrayList<TreeNodo>();
		ArrayList<posData> posizioni ;
		switch(livPrec) {
			case 0:
				 //Controllo tutte le pedine del livello precedente controllando se può fare salti
				for(int i=0; i < m.get(0).size(); i++) {
					if(m.get(0).get(i).fase != 3) {
						//Trovo tutte le posizioni che posso fare partendo dalla nuova posizione
						posizioni = findMove(m.get(livPrec).get(i),m);
						//Scandisco tali mosse evitando che aggiunga spostamenti dopo salti
						for(int j = 0; j < posizioni.size(); j++)
							if(!isContent(posizioni.get(j), m) && Startup.core.setfase(posizioni.get(j), m.get(livPrec).get(i).p)!=3)
								temp.add(new TreeNodo(posizioni.get(j),m.get(livPrec).get(i)));
					}
				}
				break;

			default:
				  //Controllo tutte le pedine del livello precedente controllando se può fare salti
				for(int i=0; i < m.get(livPrec).size(); i++) {
					//Trovo tutte le posizioni che posso fare partendo dalla nuova posizione
					posizioni = findMove(m.get(livPrec).get(i),m);
					//Scandisco tali mosse evitando che aggiunga spostamenti dopo salti
					for(int j = 0; j < posizioni.size(); j++)
						if(!isContent(posizioni.get(j), m) && Startup.core.setfase(posizioni.get(j), m.get(livPrec).get(i).p)!=3)
							temp.add(new TreeNodo(posizioni.get(j),m.get(livPrec).get(i)));
				}
				break;
		}
		if(temp.isEmpty())
			return;
		m.add(temp);
		findTree(livPrec+1, m);
	}

	private ArrayList<posData> findMove(TreeNodo n, ArrayList<ArrayList<TreeNodo>> m) {
		ArrayList<posData> temp = new ArrayList<posData>();
		int iLimit = n.p.y - 2;
		int jLimit = n.p.x - 2;
		if(iLimit < 0)//Verifico da che casella posso partire a controllare
			iLimit=0;
		if(jLimit < 0)//Verifico da che casella posso partire a controllare
			jLimit=0;
		//Controllo gli spostamenti possibili nel raggio della pedina
		for(int i = iLimit; i <= n.p.y + 2 &&  i < Startup.dati.scacchiera.length; i++)
			for(int j = jLimit; j <= n.p.x + 2 && j < Startup.dati.scacchiera[i].length; j++){
				posData t= new posData(i,j);
				if((Startup.dati.scacchiera[t.y][t.x]== 2 && Startup.core.chkmossa(n.p,t)) && !isContent(t,m) && n.fase != 3){//Verifico se è uno spostamento valido
					temp.add(t);
				}
			}
		return temp;//Ritorna tutte le possibili nuove posizioni
	}


	//Metodo per controllare se la posizione è già contenuta nell'albero
	private boolean isContent(posData pos, ArrayList<ArrayList<TreeNodo>> m) {
		for(int i=0; i < m.size(); i++)
			for(int j = 0; j < m.get(i).size(); j++)
				if(m.get(i).get(j).p.equals(pos))
					return true;
		return false;
	}

	//Metodo per scegliere percorso migliore nell'albero
	private ArrayList<posData> choosePath(ArrayList<ArrayList<TreeNodo>> mosse, posData root) {
		ArrayList<posData> mossa = new ArrayList<posData>();
		TreeNodo best = null;
		posData temp;
		for(int i = 0; i < mosse.size(); i++)
			for(int j=0; j < mosse.get(i).size(); j++){
				temp = mosse.get(i).get(j).p;//Scandisco tutte le posizioni
				if(best == null || choosePos(mosse.get(i).get(j).p,best.p,root))//Vedo se questa posizione è la migliore su cui arrivare
					best = mosse.get(i).get(j);
			}
		mossa.add(root);
		aggiungi(mossa,best,mosse); // Aggiunge al percorso della mossa le varie posizioni su cui spostarsi
		return mossa;
	}

	//Sceglie quale posizione è + coveniente
	private boolean choosePos(posData p1, posData p2, posData root){
		ArrayList<posData> m1 = new ArrayList<posData>();
		m1.add(root);
		m1.add(p1);
		ArrayList<posData> m2 = new ArrayList<posData>();
		m2.add(root);
		m2.add(p2);
		if(	  (diffPriorita(m1) < diffPriorita(m2) && Startup.dati.turno == 1) ||  //Controllo se la nuova posizione di arrivo
				((diffPriorita(m1) > diffPriorita(m2) && Startup.dati.turno == 0))){ //è meglio di quella vecchia
			return true; //E' migliore
		}
		else{
			return false; // E' peggiore
		}
	}

	//Aggiunge le posizioni nella lista di mosse
	private void aggiungi(ArrayList<posData> mossa, TreeNodo p, ArrayList<ArrayList<TreeNodo>> m) {
		if(p.posRoot != null) aggiungi(mossa,p.posRoot,m);
		mossa.add(p.p);
	}

}
