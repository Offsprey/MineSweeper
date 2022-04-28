/*
Shane Neal : SpreSoft
 */


import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Spre
 */
public class MineSweeper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //playGame(10,10,20);
        int wins = 0;
        int losses = 0;
        int gwins = 0;
        int glosses = 0;
        int ex = 0;
        int gex = 0;
        int error = 0;
        int notFinished = 0;
        for(int i = 0; i < 1000; i++)
        {
            int result = solveGame(16,16,40,5,5);
            
            switch(result)
            {
                case 0:
                    System.out.print("Not Finished");
                    notFinished++;
                    break;
                case 1:
                    System.out.print("Won without guessing");
                    wins++;
                    break;
                case 2:
                    System.out.print("Lost without guessing");
                    losses++;
                    break;
                case 3:
                    System.out.print("Exception");
                    ex++;
                    break;
                case 11:
                    System.out.print("Won with guessing");
                    gwins++;
                    break;
                case 12:
                    System.out.print("Lost with guessing");
                    glosses++;
                    break;
                case 13:
                    System.out.print("Exception in guessing");
                    gex++;
                    break;
                case 14:
                    System.out.print("Exception in isolated cell");
                    gex++;
                    break;
                default :
                    System.out.print("Error");
                    error++;
                    break;
            }
            System.out.print("\n");    
        }
        System.out.print("------------------------------------ \n");
        System.out.print("Wins without guessing : " + wins + "\n");
        System.out.print("Losses without guessing : " + losses + "\n");
        System.out.print("Wins with guessing : " + gwins + "\n");
        System.out.print("Losses with guessing : " + glosses + "\n");
        System.out.print("Exceptions without guessing : " + ex + "\n");
        System.out.print("Exceptions with guessing : " + gex + "\n");
        System.out.print("Errors : " + error + "\n");
        
    }
        
    public static void playGame(int width, int height, int mines)
    {
        Board nBoard = new Board(width, height, mines);        
        
        String cmd = "";
        Scanner in = new Scanner(System.in);
        while (!cmd.equals("exit"))
        {
            System.out.print(nBoard.displayBoard());
            
            System.out.print(" >");
            cmd = in.nextLine();
            char c = cmd.charAt(0);
            if (c == 'n')
                nBoard = new Board(width, height, mines);
            if (c == 'e' || c== 'm' || c== 's')
            {
                String[] vals = cmd.split(" ");
                int x = Integer.parseInt(vals[1]);
                int y = Integer.parseInt(vals[2]);
                
                if (vals[0].equals("e"))
                    nBoard.exposeCell(x, y);
                if (vals[0].equals("m"))
                    nBoard.markMine(x, y);
                if (vals[0].equals("s"))
                {
                    nBoard = new Board(width, height, mines, x, y);
                    nBoard.exposeCell(x, y);
                }
            }                
        }
        
        System.out.print(nBoard.displayBoard());
    }
    
    public static int solveGame(int width, int height, int mines, int seedX, int seedY)
    {
        Board nBoard = new Board(width, height, mines, seedX, seedY);
        nBoard.exposeCell(seedX, seedY);
        System.out.print(nBoard.displayBoard());
        
        boolean guessed = false;
        int progress = 1;
        int round = 0;
        while (progress != 0)
        {
            System.out.println("\nRound : " + String.valueOf(round));
            System.out.print(nBoard.displayBoard());
            progress = flagMines(width, height, nBoard);
            
            progress += checkNeighbors(width, height, nBoard);
            round++;
            
            if ( nBoard.getFlaggedCells() > nBoard.getMines() || round > 100)
            {
            	int test = 0;
            }
            // 
//            if (nBoard.getFlaggedCells() == 40)
//            {
//                int test = 0;
//                nBoard.gameOver();
//                test = 1;
//            }
            // 
            if (progress == 0 && nBoard.gameOver() == false)
            {
                guessed = true;
                Cell gCell = nBoard.getBestGuess();
                if (gCell != null)
                {
	                System.out.print("Guessing : ");
	                //java.lang.NullPointerException
	                //best guess does not find option
	                //usually in corners / edges
	                //also caused by cells surrounded by mines
	                try{
	                System.out.print(gCell.getCellId()[0]);
	                System.out.print(",");
	                System.out.print(gCell.getCellId()[1]);
	                System.out.print("\n");
	                nBoard.exposeCell(gCell.getCellId()[0], gCell.getCellId()[1]);
	                }
	                catch(Exception ex)
	                {
	                    System.out.print("Exception\n");
	                    return 13;
	                }
	                
	                progress = 1;
                }
                else
                {                	
                	//no valid guess, check for isolated cells
                	try
                	{
    	                Cell iCell = nBoard.getIsolatedCell();
    	                int minesLeft = nBoard.getMines() - nBoard.getFlaggedCells();
    	                //if all mines flagged, isolated cells must be empty
    	                //==??
    	                
    	                int hiddenCells = nBoard.getHiddenCells().size();
    	                if (minesLeft == hiddenCells)
    	                {
    	                	iCell.setFlagged(true);
                            System.out.println("Flagging Isolated Cell: " + 
                                    String.valueOf(iCell.getCellId()[0]) + "," +
                                    String.valueOf(iCell.getCellId()[1])
                            );
    	                }
    	                //otherwise guess isolated cell
    	                else
    	                {
    	                	System.out.print("Guessing Isolated Cell: ");
    	                	System.out.print(iCell.getCellId()[0]);
    		                System.out.print(",");
    		                System.out.print(iCell.getCellId()[1]);
    		                System.out.print("\n");
    	                	nBoard.exposeCell(iCell.getCellId()[0], iCell.getCellId()[1]);
    	                }
    	                
    	                progress = 1;
	                }
	                catch(Exception ex)
	                {
	                    System.out.print("Exception\n");
	                    return 14;
	                }
                	
                }
            }
                
        }
        

        if (guessed)
            return nBoard.getGameStatus() + 10;
        else
            return nBoard.getGameStatus();
    }

    //flag mines
    public static int flagMines(int width, int height, Board nBoard)
    {
        int count = 0;
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
            	
                Cell cCell = nBoard.getCells()[x][y];
                
                
                //doesnt catch cases where 2 mines in 2 
                //== nBoard.getHiddenNeighbors(x,y).size
                //if (cCell.isExposed() && (cCell.getProx() - markedCells) == 1)
                if (cCell.isExposed() && (cCell.getProx() == nBoard.getHiddenNeighbors(x, y, true).size()))
                {
                    
                    //build neighbors
                    int[] n1 = new int[]{x-1,y-1};
                    int[] n2 = new int[]{x,y-1};
                    int[] n3 = new int[]{x+1,y-1};
                    int[] n4 = new int[]{x+1,y};
                    int[] n5 = new int[]{x+1,y+1};
                    int[] n6 = new int[]{x,y+1};
                    int[] n7 = new int[]{x-1,y+1};
                    int[] n8 = new int[]{x-1,y};

                    ArrayList<int[]> nList = new ArrayList<>();
                    ArrayList<Cell> hiddenCellList = new ArrayList<>();

                    nList.add(n1);
                    nList.add(n2);
                    nList.add(n3);
                    nList.add(n4);
                    nList.add(n5);
                    nList.add(n6);
                    nList.add(n7);
                    nList.add(n8);

                    int prox = 0;
                    for(int[] n : nList)
                    {                    
                        //check if neighbors is on board
                        if (nBoard.isCellInbound(n[0], n[1]))
                        {
                            Cell nCell = nBoard.getCells()[n[0]][n[1]];
                            //replace with flag cell
                            //if (!nCell.isExposed() && !nCell.isFlagged())
                            if (!nCell.isExposed())
                                hiddenCellList.add(nCell);
                        }
                    }
                    
                    
                    //??replace with loop to flag each hidden cell
                    if (hiddenCellList.size() == cCell.getProx())
                    //if (hiddenCellList.size() == 1)
                    {                        
                        for(Cell eCell : hiddenCellList)
                        {
                            if (!eCell.isFlagged())
                            {
                                eCell.setFlagged(true);
                                System.out.println("Flagging : " + 
                                        String.valueOf(eCell.getCellId()[0]) + "," +
                                        String.valueOf(eCell.getCellId()[1])
                                );
                                count++;
                            }
                        }
                    }
                }
            }
        }   
        return count;
    }
    
    public Cell getBestGuess(Board board)
    {
        Cell bCell = board.getCells()[0][0];
        double bGuess = 1;
        for (int x = 0; x < board.getWidth(); x++)
        {        
            for (int y = 0; y < board.getHeight(); y++ )
            {
            	
                Cell cell = board.getCells()[x][y];
                if (cell.isExposed()  == true && cell.getProx() > 0)
                {
                    int m = board.getMarkedNeighbors(x, y).size();
                    int h = board.getHiddenNeighbors(x, y, true).size();

                    double unMarkedNeighbor = h - m;
                    double unMarkedMines = cell.getProx() - m;
                    
                    double guess = (unMarkedMines / unMarkedNeighbor);
                    if (guess < bGuess && board.getCells()[x][y].isExposed())
                    {
                        bGuess = guess;
                        bCell = board.getCells()[x][y];
                    }
                }
            }
        }
        double bGuessRound = ((double)Math.round(bGuess *100)) / 100;
        System.out.println("Guess stats : " + bGuessRound);
        
        int x = bCell.getCellId()[0];
        int y = bCell.getCellId()[1];
        
        int[] n1 = new int[]{x-1,y-1};
        int[] n2 = new int[]{x,y-1};
        int[] n3 = new int[]{x+1,y-1};
        int[] n4 = new int[]{x+1,y};
        int[] n5 = new int[]{x+1,y+1};
        int[] n6 = new int[]{x,y+1};
        int[] n7 = new int[]{x-1,y+1};
        int[] n8 = new int[]{x-1,y};

        ArrayList<int[]> nList = new ArrayList<>();

        nList.add(n1);
        nList.add(n2);
        nList.add(n3);
        nList.add(n4);
        nList.add(n5);
        nList.add(n6);
        nList.add(n7);
        nList.add(n8);
        
        for(int[] n : nList)
        {  if (board.isCellInbound(n[0], n[1]))
            {
                Cell gCell = board.getCells()[n[0]][n[1]]; 
                if (!gCell.isExposed() && !gCell.isFlagged())
                    return gCell;
            }
        }       
        return null;        
    }
    
    public static int checkNeighbors(int width, int height, Board nBoard)
    {
       int count = 0;
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
            	
                Cell cCell = nBoard.getCells()[x][y];
                if (cCell.isExposed())
                {
                    //build neighbors
                    int[] n1 = new int[]{x-1,y-1};
                    int[] n2 = new int[]{x,y-1};
                    int[] n3 = new int[]{x+1,y-1};
                    int[] n4 = new int[]{x+1,y};
                    int[] n5 = new int[]{x+1,y+1};
                    int[] n6 = new int[]{x,y+1};
                    int[] n7 = new int[]{x-1,y+1};
                    int[] n8 = new int[]{x-1,y};

                    ArrayList<int[]> nList = new ArrayList<>();
                    ArrayList<Cell> markedCellList = new ArrayList<>();

                    nList.add(n1);
                    nList.add(n2);
                    nList.add(n3);
                    nList.add(n4);
                    nList.add(n5);
                    nList.add(n6);
                    nList.add(n7);
                    nList.add(n8);

                    int prox = 0;
                    for(int[] n : nList)
                    {                    
                        //check if neighbors is on board
                        if (nBoard.isCellInbound(n[0], n[1]))
                        {
                            Cell nCell = nBoard.getCells()[n[0]][n[1]];
                            if (nCell.isFlagged())
                                markedCellList.add(nCell);
                        }
                    }
                    
                    if (markedCellList.size() == cCell.getProx() && cCell.getProx() > 0)
                    {
                        for(int[] n : nList)
                        {                    
                            //check if neighbors is on board
                            if (nBoard.isCellInbound(n[0], n[1]))
                            {
                                Cell nCell = nBoard.getCells()[n[0]][n[1]];
                                if (!nCell.isFlagged() && !nCell.isExposed())
                                {
                                    System.out.println("Exposing : " + 
                                String.valueOf(nCell.getCellId()[0]) + "," +
                                String.valueOf(nCell.getCellId()[1])
                        );
                                    nBoard.exposeCell(n[0], n[1]);
                                    count++;
                                    
                                }
                            }
                        }
                    }
                }
            }
        
        }
        
        return count;
    }
    
}
