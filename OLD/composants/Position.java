package composants;

public class Position {

    public static final int WIDTH = 7;  // Largeur du plateau
    public static final int HEIGHT = 6; // Hauteur du plateau
    public static final int WORSTSCORE = -45; // Score le plus bas (Le plus bas c'est -42 donc un entier inferieur à -42 fera l'affaire)

    private int[][] board;        // 0 si la cellule est vide, 1 pour le premier joueur et 2 pour le second joueur.
    private int[] height;         // Nombre de jetons par colonne
    private int moves;            // Nombre de coups joués depuis le début du jeu.

    /**
     * Constructeur par défaut, construit une position vide.
     */
    public Position() {
        board = new int[WIDTH][HEIGHT];
        height = new int[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            height[i] = 0;
            for (int j = 0; j < HEIGHT; j++) {
                board[i][j] = 0;
            }
        }
        moves = 0;
    }

    public static Position copy(Position p) {
        int[][] board_ = new int[WIDTH][HEIGHT];
        int[] height_ = new int[WIDTH];
        Position pos = new Position();
        for (int i = 0; i < WIDTH; i++) {
            height_[i] = p.height[i];
            for (int j = 0; j < HEIGHT; j++) {
                board_[i][j] = p.board[i][j];
            }
        }
        pos.board = board_;
        pos.height = height_;
        pos.moves = p.nbMoves();
        return pos;
    }

    /**
     * Indique si une colonne est jouable.
     * @param col Indice de la colonne à jouer (basé sur 0).
     * @return true si la colonne est jouable, false si la colonne est déjà pleine.
     */
    public boolean canPlay(int col) {
        return height[col] < HEIGHT;
    }

    /**
     * Joue dans une colonne jouable.
     * Ne doit pas être appelé sur une colonne non jouable ou une colonne permettant un alignement.
     * @param col Indice de la colonne à jouer (basé sur 0).
     */
    public void play(int col) {
        board[col][height[col]] = 1 + moves % 2;
        height[col]++;
        moves++;
    }

    /**
     * Joue une séquence de colonnes jouées successivement, principalement utilisée pour initialiser un plateau.
     * @param seq Séquence de chiffres correspondant à l'indice de la colonne jouée (basé sur 1).
     * @return Nombre de mouvements joués.
     */
    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col)) return i; // Mouvement invalide
            play(col);
        }
        return seq.length();
    }

    /**
     * Indique si le joueur actuel gagne en jouant une colonne donnée.
     * Ne doit jamais être appelé sur une colonne non jouable.
     * @param col Indice de la colonne jouable (basé sur 0).
     * @return true si le joueur actuel réalise un alignement en jouant la colonne correspondante.
     */
    public boolean isWinningMove(int col) {
        int currentPlayer = 1 + moves % 2;
        // Vérifie les alignements verticaux
        if (height[col] >= 3
            && board[col][height[col] - 1] == currentPlayer
            && board[col][height[col] - 2] == currentPlayer
            && board[col][height[col] - 3] == currentPlayer)
            return true;

        for (int dy = -1; dy <= 1; dy++) {
            int nb = 0;
            for (int dx = -1; dx <= 1; dx += 2)
                for (int x = col + dx, y = height[col] + dx * dy; x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && board[x][y] == currentPlayer; nb++) {
                    x += dx;
                    y += dx * dy;
                }
            if (nb >= 3) return true;
        }
        return false;
    }

    /**
     * @return Nombre de mouvements joués depuis le début du jeu.
     */
    public int nbMoves() {
        return moves;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            sb.append("|");
            for (int x = 0; x < WIDTH; x++) {
                if (board[x][y] == 0) {
                    sb.append("   ");
                } else if (board[x][y] == 1) {
                    sb.append(" X ");
                } else {
                    sb.append(" O ");
                }
            }
            sb.append("|\n");
        }
        sb.append("+");
        for (int x = 0; x < WIDTH; x++) {
            sb.append("---");
        }
        sb.append("+\n");
        return sb.toString();
    }
}
