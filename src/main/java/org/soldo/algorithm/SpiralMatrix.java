package org.soldo.algorithm;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {

    public static void main(String[] args) {
        int[][] matrix = new int[3][];
        int k = 1;
        for (int i = 0; i < 3; i++) {
            matrix[i] = new int[3];
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = k++;
            }
        }

        System.out.println(matrix.length);
    }

    /**
     * clock
     *
     * @param matrix
     * @return
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List ans = new ArrayList();
        int row = matrix.length;
        if (row <= 0)
            return ans;
        int column = matrix[0].length;
        boolean[][] pass = new boolean[row][column];
        int r = 0, c = 0, di = 0;
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};
        for (int i = 0; i < row * column; i++){
            ans.add(matrix[r][c]);
            pass[r][c] = true;
            int rr = r + dr[di];
            int cc = c + dc[di];
            if(0 <= rr && rr < row && 0 <= cc && cc < column && !pass[rr][cc]){
                r = rr;
                c = cc;
            }else {
                di = (di + 1) % 4;
                r += dr[di];
                c += dc[di];
            }
        }
        return ans;
    }
}
