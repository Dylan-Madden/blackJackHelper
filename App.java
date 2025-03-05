import java.util.ArrayList;
public class App {
    public static void main(String[] args)  {
        BlackJack blackJack = new BlackJack();
        Music player = new Music();


        String filePath = "src/unitThreeSounds/Minecraft.wav";

        player.play(filePath);

        Music  [][] Array2d =  {{new Music(),new Music()},{new Music(),new Music()}};
        int  [][] Array2d2 =  {{1,2},{3,4}};
        for (int i = 0; i < Array2d.length; i++) {
            for (int j = 0; j < Array2d[i].length; j++) {
                System.out.print(Array2d[i][j]);
                System.out.print(Array2d2[i][j]);
            }}
        for (Music[] row : Array2d) {
            for (Music element : row) {
                System.out.print(element);
            }
        }
        blackJack.gameMethod();
        blackJack.method();
        Game[] array = new Game[10];
        System.out.println(array);



    }
}
