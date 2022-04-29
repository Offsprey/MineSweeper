/*
Shane Neal : SpreSoft
 */


import java.util.ArrayList;



/**
 *
 * @author Spre
 */
public class Board {
    private int height;
    private int width;
    private int mines;
    private Cell[][] cells;
    private int gameStatus;

    public Board(int width, int height, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        
        buildBoard();  
        gameStatus = 0;
    }

    public Board(int height, int width, int mines, int seedX, int seedY) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        boolean seeded = false;
        while (!seeded)
        {
            buildBoard();
            Cell cCell = cells[seedX][seedY];
            //Make sure board is correctly built
            if (cCell.getProx() == 0 && !cCell.isMine())
                seeded = true;
        }
        gameStatus = 0;
    }
    
    private void buildBoard()
    {
    	//cells = new Cell[height][width];
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                int[] cellId = {x,y}; 
                cells[x][y] = new Cell(cellId);
            }
        }
        
        java.util.Random nRand = new java.util.Random(System.currentTimeMillis());
        
        int i = 0;
        while (i < mines)
        {
            //get random cell
            int[] randCell = {nRand.nextInt(width),nRand.nextInt(height)};
            if (!cells[randCell[0]][randCell[1]].isMine())            
            {
                cells[randCell[0]][randCell[1]].setMine(true);
                i++;
            }
        }        
        
        //build proximity
        for (int y = 0; y < height; y++ )
        {
            for (int x = 0; x < width; x++)
            {
                //build neighbors
                Cell cCell = cells[x][y];
                ArrayList<int[]> nList = Cell.getProxCoords(x, y);
                
                int prox = 0;
                for(int[] n : nList)
                {                    
                    //check if neighbors is on board
                    if (this.isCellInbound(n[0], n[1]))
                    {
                        Cell nCell = cells[n[0]][n[1]];
                        if (nCell.isMine())
                            prox++;
                    }
                }
                cCell.setProx(prox);
                
            }
        }
        
    }
    
    public String displayBoard()
    {
        String field = "";
        String dig1 = "    |  ";
        String dig2 = "    |  ";
        String sep = "";
        for (int x = 0; x < width; x++ )
        {
            dig1 += String.valueOf(Math.round(Math.floor(x / 10))) + " ";
            dig2 += String.valueOf(x % 10) + " ";
            sep += "--";
        }
        //field = "   |  0 1 2 3 4 5 6 7 8 9\n";
        field += dig1 + "\n" + dig2 + "\n    | " + sep + "\n";
        
        
        for (int y = 0; y < height; y++ )
        {
            field += y;
            if (y < 10)
                field += " ";
            field += "  |  ";
            for (int x = 0; x < width; x++)
            {
                Cell cCell = this.getCell(x, y);
                if (cCell.isExposed())
                {
                    if (cCell.isMine())
                    {
                        if (cCell.isFlagged())
                            field += "X ";
                        else
                            field += "# ";
                    }
                    else if(cCell.getProx() == 0)
                        field += ". ";
                    else
                        field += String.valueOf(cCell.getProx()) + " ";
                }
                else if (cCell.isFlagged())
                    field += "X ";
                else
                    field += "~ ";
                    
            }
            field += "\n";
        }
        
        field += "Mines : " + String.valueOf(this.getFlaggedCells()) + "/" + mines + "\n";
        
        return field;
    }
    
    public void markMine(int x, int y)
    {
        Cell cCell = this.getCell(x, y);
        cCell.toggleFlagged();
    }
    
    public String exposeCell(int x, int y)
    {
        Cell cCell = this.getCell(x, y);
        if (cCell.isExposed())
            return "";
        else
        {
            cCell.setExposed(true);
            if (cCell.isMine())
            {
                exposeBoard();
                gameStatus = 2;
            }
            //if cell has no marked neighbors, sweep to next cell
            else if (cCell.getProx() == 0)
            {
                exposeNeighbors(x,y);                
            }
        }
        return "";
            
    }
    
    public boolean gameOver()
    {
        int test = 0;
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                Cell cell = this.getCell(x, y);
                if (!cell.isExposed() && !cell.isFlagged())
                {
                    return false;
                }
            }
        }

        displayBoard();
        if (gameStatus != 2)
            gameStatus = 1;
        return true;
    }
    
    public ArrayList<Cell> getMarkedNeighbors(int x, int y)
    {
        ArrayList<Cell> markedCellList = new ArrayList<>();
        Cell cCell = this.getCell(x, y);
        if (cCell.isExposed())
        {            
            ArrayList<int[]> nList = Cell.getProxCoords(x, y);

            for(int[] n : nList)
            {                    
                //check if neighbors is on board
                if (this.isCellInbound(n[0], n[1]))
                {
                    Cell nCell = this.getCell(n[0], n[1]);
                    if (nCell.isFlagged())
                        markedCellList.add(nCell);
                }
            }
        }        
        return markedCellList;
    }
    
    public ArrayList<Cell> getHiddenNeighbors(int x, int y, boolean includeMarked)
    {
        ArrayList<Cell> hiddenCellList = new ArrayList<>();
        //if (cCell.isExposed())
        if (true)
        {
            ArrayList<int[]> nList = Cell.getProxCoords(x, y);

            for(int[] n : nList)
            {                    
                //check if neighbors is on board
                if (this.isCellInbound(n[0], n[1]))
                {
                    Cell nCell = this.getCell(n[0], n[1]);
                    if (!nCell.isExposed() && ((includeMarked && nCell.isFlagged()) || !nCell.isFlagged()))
                        hiddenCellList.add(nCell);
                }
            }
        }        
        return hiddenCellList;
    }
    
    
    public void exposeNeighbors(int x, int y)
    {
    	ArrayList<int[]> nList = Cell.getProxCoords(x, y);
        
        for(int[] n : nList)
        {                    
            //check if neighbors is on board
            if (this.isCellInbound(n[0], n[1]))
            {
                this.exposeCell(n[0], n[1]);
            }
        }        
    }

    public void exposeBoard()
    {
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            { 
            	this.getCell(x, y).setExposed(true);
            }
        }
    }
    
    public Cell getBestGuess()
    {
        Cell bCell = this.getCell(0, 0);
        double bGuess = 1;
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                Cell cell = this.getCell(x, y);
                //
                if (cell.isExposed()  == true && cell.getProx() > 0)
                {
                    int m = this.getMarkedNeighbors(x, y).size();
                    int h = this.getHiddenNeighbors(x, y, true).size();

                    double unMarkedNeighbor = h - m;
                    double unMarkedMines = cell.getProx() - m;
                    
                    double guess = (unMarkedMines / unMarkedNeighbor);
                    if (guess < bGuess && this.getCell(x, y).isExposed())
                    {
                        bGuess = guess;
                        bCell = this.getCell(x, y);
                    }
                }
            }
        }
        double bGuessRound = ((double)Math.round(bGuess *100)) / 100;
        System.out.println("Guess stats : " + bGuessRound);
        
        int x = bCell.getCellId()[0];
        int y = bCell.getCellId()[1];
        
        ArrayList<int[]> nList = Cell.getProxCoords(x, y);
        
        for(int[] n : nList)
        {  if (this.isCellInbound(n[0], n[1]))
            {
                Cell gCell = this.getCell(n[0], n[1]);
                if (!gCell.isExposed() && !gCell.isFlagged())
                    return gCell;
            }
        }       
        return null;        
    }
    
    public ArrayList<Cell> getHiddenCells()
    {
    	
    	ArrayList<Cell> hCells = new ArrayList<>();
    	
    	for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                Cell cell = this.getCell(x, y);
                if(!cell.isExposed() && !cell.isFlagged())
                {
                	hCells.add(cell);
                }
                
            }
        }
    	
    	return hCells;
    }
    
    public Cell getIsolatedCell()
    {
    	for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                Cell cell = this.getCell(x, y);
                if (!cell.isFlagged() && !cell.isExposed()) {
                	ArrayList<int[]> nList = Cell.getProxCoords(x, y);
	                
	                boolean isIsolated = true;
                
	                for(int[] n : nList)
	                {  if (this.isCellInbound(n[0], n[1]))
	                    {
	                        Cell gCell = this.getCell(n[0], n[1]);
	                        if (gCell.isExposed())
	                        	isIsolated = false;
	                    }
	                }   
	                if (isIsolated)
	                	return cell;
                }
            }
        }    	
    	return null;
    }
    
//Bad logic fails at edges of board
//    public Cell getBestGuess()
//    {
//        Cell bCell = this.getCells()[0][0];
//        int bGuess = 0;
//        for (int x = 0; x < width; x++)
//        {        
//            for (int y = 0; y < height; y++ )
//            {
//                Cell cell = this.getCells()[x][y];
//                //
//                if (cell.isExposed()  == true && cell.getProx() > 0)
//                {
//                    int m = this.getMarkedNeighbors(x, y).size();
//                    int h = this.getHiddenNeighbors(x, y, true).size();
//
//                    
//                    int guess = (h - m - cell.getProx());
//                    if (guess > bGuess && this.getCells()[x][y].isExposed())
//                    {
//                        bGuess = guess;
//                        bCell = this.getCells()[x][y];
//                    }
//                }
//            }
//        }
//        
//        int x = bCell.getCellId()[0];
//        int y = bCell.getCellId()[1];
//        
//        int[] n1 = new int[]{x-1,y-1};
//        int[] n2 = new int[]{x,y-1};
//        int[] n3 = new int[]{x+1,y-1};
//        int[] n4 = new int[]{x+1,y};
//        int[] n5 = new int[]{x+1,y+1};
//        int[] n6 = new int[]{x,y+1};
//        int[] n7 = new int[]{x-1,y+1};
//        int[] n8 = new int[]{x-1,y};
//
//        ArrayList<int[]> nList = new ArrayList<>();
//
//        nList.add(n1);
//        nList.add(n2);
//        nList.add(n3);
//        nList.add(n4);
//        nList.add(n5);
//        nList.add(n6);
//        nList.add(n7);
//        nList.add(n8);
//        
//        for(int[] n : nList)
//        {  if (n[0] >= 0 && n[0] < width && n[1] >= 0 && n[1] < height)
//            {
//                Cell gCell = cells[n[0]][n[1]]; 
//                if (!gCell.isExposed() && !gCell.isFlagged())
//                    return gCell;
//            }
//        }       
//        return null;        
//    }
    
    public int getFlaggedCells()
    {
        
        int flaggedCells = 0;
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            { 
                if (this.getCell(x, y).isFlagged())
                    flaggedCells++;
            }
        }
        return flaggedCells;
    }
    
    public boolean isCellInbound(int x, int y)
    {
		return (x >= 0 && x < width && y >= 0 && y < height);

    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }    

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
    
    public Cell getCell(int x ,int y)
    {
    	return cells[x][y];
    }

    public void setCells(Cell[][] cells) 
    {
        this.cells = cells;
    }
    
    @Override
    public String toString()
    {
        String output = "";
        
        for (int x = 0; x < width; x++)
        {        
            for (int y = 0; y < height; y++ )
            {
                Cell cell = this.getCell(x, y);
                int codeValue = 0;
                if (cell.isMine())
                {
                    codeValue += 10;
                }
                codeValue += cell.getProx();
                output += String.valueOf(codeValue) + ",";                
            }
            
            output += "\n";
        }
        return output;
    }
    
    
}
