import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Player {

    private int x = 0;
    private int y = 0;
    private StackPane node;

    public Player(String characterImage) {
        String characterImageName = characterImage + ".png";

        node = new StackPane();
        Image image = new Image(characterImageName);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameGrid.TILE_SIZE * 0.9);
        imageView.setFitHeight(GameGrid.TILE_SIZE * 0.9);
        node.getChildren().add(imageView);
    }

    public StackPane getNode() {
        return node;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
}