public class Generator{


        /**
 * generates a 4 by 4 identity Matrix
 * @return identity matrix
 */
public static Matrix identity(){
        int[][] out = new int[4][4];
        for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                        if (x == y) {
                                out[x][y] = 1;
                        }
                }
        }

        return new Matrix(out);
}


public static Matrix scale(int sx, int sy, int sz){
        int[][] out = { {sx, 0, 0, 0},
                        {0, sy, 0, 0},
                        {0, 0, sz, 0},
                        {0, 0, 0,  1}};

        return new Matrix(out);
}

public static Matrix translate(int dx, int dy, int dz){
        int[][] out = { {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0},
                        {dx, dy, dz,  1}};
        return new Matrix(out);
}

public static Matrix rotate(char axis, int theta){
        switch (axis) {
        case 'x':
                int[][] out = { {1, 0, 0, 0},
                                {0, 1, 0, 0},
                                {0, 0, 1, 0},
                                {0, 0, 0,  1}};
                return new Matrix(out);
        }

        return new Matrix();
}


}