public class StudentPlayer extends Player {

    int round = 0;
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }
    @Override
    public int step(Board board) {
        int max_res_column = board.getValidSteps().get(0);
        int max_res = Integer.MIN_VALUE;
        for (Integer column : board.getValidSteps()) {
            Board cloneBoard = new Board(board);
            cloneBoard.step(2,column);
            int temp_res = miniMax(cloneBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if(temp_res > max_res) {
                max_res = temp_res;
                max_res_column = column;
            }
        }
        round++;
        return max_res_column;
    }
    public int windowCalculator(Board board,int start_c, int start_r, int mode){
        int score = 1;
        int[][] state = board.getState();
        int player = 0; int free = 0; int enemy = 0;
        int limit_c = start_c; int limit_r = start_c;
        int r = start_r; int c = start_c;

        if(mode == 0){
            for(int i = start_c; i < start_c + 4; i++){
                if(state[start_r][i] == playerIndex)
                    player++;
                if(state[start_r][i] == 0)
                    free++;
                if(state[start_r][i] == playerIndex-1)
                    enemy++;}
            return score;

        } else if (mode == 1) {
            for(int i = start_r; i < start_r + 4; i++){
                if(state[i][start_c] == 1) player++;
                if(state[i][start_c] == 0) free++;
                if(state[i][start_c] == playerIndex-1) enemy++;
            }
        } else if (mode == 2) {
            for(int i = start_c; i < limit_c + 4; i++){
                if(state[r][c] == playerIndex)
                    player++;
                if(state[r][c] == 0)
                    free++;
                if(state[r][c] == playerIndex-1)
                    enemy++;
                c++; r--;
            }
        } else {
            for(int i = start_c; i < limit_r + 4; i++){
                if(state[r][c] == playerIndex)
                    player++;
                if(state[r][c] == 0)
                    free++;
                if(state[r][c] == playerIndex-1)
                    enemy++;
                c++; r++;
            }
        }

        //if(free == 4)
          //  score = -1;
        //else
            if(player == 4)
            score = 10000;
        else if(player == 3 && free == 1)
            score = 1000;
        else if(player == 2 && free == 2)
            score = 100;
        else if(enemy == 3 && free == 1)
            score = -400;

        if(start_c == 3 && score!=-1){

            score = score*250;
        }

        return score;

    }


    public int pointCalculator(Board board, boolean currentPlayer) {

        if(round == 2)
            round = 2;
        //4 long window
        int window_lenght = 4;

        int[][] state = board.getState();
        int rows = boardSize[0] - 1 ;
        int columns = boardSize[1] - 1;

        int point = 0;
        for(int row = rows; row > 0; row--){
            int iter_start = 0;
            int iter_end = iter_start + window_lenght - 1;
            for(int column = 0; column <= 3; column++){
                if(windowCalculator(board, column, row, 0)==-1){
                    column+=3;
                }
                else{
                    point += windowCalculator(board, column, row, 0);
                    iter_start++;
                    iter_end++;
                }
            }
        }
        for(int column = 0; columns > column; column++){
            int iter_start = rows;
            int iter_end = iter_start - window_lenght + 1;
            for(int row = 2; row > 0; row--){
                if(windowCalculator(board, column, row, 1)==-1){
                    break;
                }
                else{
                    int temp_point = windowCalculator(board, column, row, 1);
                    point += temp_point;
                    iter_start--;
                    iter_end--;
                }
            }
        }

        for(int row = rows; row >= 3; row--){
            int iter_start = rows;
            int iter_end = iter_start + window_lenght - 1;
            for(int column = 0; column < 4; column++) {
                if (windowCalculator(board, column, row, 2) == -1) {

                } else {
                    int temp_point = windowCalculator(board, column, row, 2);
                    point += temp_point;

                }
            }
        }
        for(int row = 2; row > 0; row--) {
            for (int column = 0; column < 4; column++) {
                if(windowCalculator(board, column, row, 3) == -1){
                }
                else {
                    int temp_point = windowCalculator(board, column, row, 3);
                    point += temp_point;
                }
            }
        }
        return point;
    }

    public int scoreEndGame(Board board){
        if(board.getWinner() == 2){
            return Integer.MAX_VALUE;
        }
        else if(board.getWinner() == 1){
            return  Integer.MIN_VALUE;
        }

        return 0;
    }

    public int miniMax(Board board, int depht, int alpha, int beta, boolean currentPlayer) {
        if (depht == 0 || board.getValidSteps().isEmpty() || board.gameEnded()) {
            if (board.gameEnded()) {
                return scoreEndGame(board);
            } else {
                int result = pointCalculator(board, currentPlayer);
                return result;
            }
        }

        if (currentPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Integer column : board.getValidSteps()) {
                Board childBoard = new Board(board);
                childBoard.step(playerIndex, column);
                int eval = miniMax(childBoard ,depht - 1, alpha, beta, false);
                if (maxEval < eval) {
                    maxEval = eval;
                }
                if (alpha < eval)
                    alpha = eval;

                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Integer column : board.getValidSteps()) {
                Board childBoard = new Board(board);
                childBoard.step(1, column);
                int eval = miniMax(childBoard, depht - 1, alpha, beta, true);
                if (minEval > eval) {
                    minEval = eval;}

                if(beta < eval ){
                    beta = eval;
                }
                if (beta <= alpha)
                    break;

            }
            return minEval;
        }
    }
}
