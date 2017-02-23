package tetris;

public class Items {
    static final boolean[][][] Shape = {
            // I
            {
                { false, true, false, false },
                { false, true, false, false },
                { false, true, false, false },
                { false, true, false, false }
            },
            // L
            { 
                { false, true, false, false },
                { false, true, false, false },
                { false, true, true, false },
                { false, false, false, false }
           },
            // J
            {
               { false, false, true, false },
               { false, false, true, false },
               { false, true, true, false },
               { false, false, false, false }
            },
            // T
            {
                { false, false, false, false },
                { false, true, true, true },
                { false, false, true, false },
                { false, false, false, false }
            },
            // O
            {
                { false, false, false, false },
                { false, true, true, false },
                { false, true, true, false },
                { false, false, false, false }
            },
            // S
            {
                { false, false, false, false },
                { false, false, true, true },
                { false, true, true, false },
                { false, false, false, false }
            },
            // Z
            {
                { false, false, false, false },
                { false, true, true, false },
                { false, false, true, true },
                { false, false, false, false }
            } };
}
