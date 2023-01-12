package rainfallanalyser.cp2406assignment_alpha;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * CP2406 Assignment - Soumyadeep Sarkar
 * Alpha Version
 * Created on the basis of the supplied starter code for the assignment.
 * The drawPicture() class was changed for this version of the project.
 * It Requires the user to specify the file path for the analysed rainfall data.
 *
 * Link for the project repo: https://github.com/Sarkar-Soumyadeep-202002/CP2406_Assignment.git
 */
public class RainfallVisualiser extends Application {

    /**
     * Draws a picture. The parameters width and height give the size
     * of the drawing area, in pixels.
     */
    public void drawPicture(GraphicsContext g, int width, int height) {

        // Create the x-axis and y-axis.
        int border_width = 20;
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeLine(border_width, border_width, border_width, height - border_width);
        g.strokeLine(border_width, height - border_width, width - border_width, height - border_width);

        TextIO.getln(); // Remove the first line

        ArrayList<Double> allMonthlyTotals = new ArrayList<>(); // Create an ArrayList to store the total rainfall of every month.

        double maxMonthlyTotal = Double.NEGATIVE_INFINITY;
        int firstYear = 2050;
        int lastYear = 0;
        // Read the file first and create an array of the monthly totals.
        while (!TextIO.eof()) {
            String[] line = TextIO.getln().trim().strip().split(","); // Create an array to store the values in each row of the csv file which is analysed as strings.
            double monthlyTotal = Double.parseDouble(line[2]); // Retrieve the total monthly rainfall from the array.
            allMonthlyTotals.add(monthlyTotal); // Append the total monthly rainfall to the ArrayList.
            if (monthlyTotal > maxMonthlyTotal)
                maxMonthlyTotal = monthlyTotal;

            int year = Integer.parseInt(line[0]);
            if (year < firstYear)
                firstYear = year;
            else if (year > lastYear)
                lastYear = year;
        }

        double xAxisLength = width - 2 * border_width;
        double yAxisLength = height - 2 * border_width;
        double currentXPos = border_width; // Determine the current position on the x-axis to draw the next bar.
        double barWidth = xAxisLength / allMonthlyTotals.size(); // Calculate the width of each bar in the graph.

        g.setFill(Color.DEEPSKYBLUE);
        g.setLineWidth(0.8);

        for (Double monthlyTotal : allMonthlyTotals) {
            // Get the height of the column relative to the maximum height.
            double columnHeight = (monthlyTotal / maxMonthlyTotal) * yAxisLength;

            // Draw the rectangle representing the rainfall and draw a black outline.
            g.fillRect(currentXPos, height - border_width - columnHeight, barWidth, columnHeight);
            g.strokeRect(currentXPos, height - border_width - columnHeight, barWidth, columnHeight);

            currentXPos += barWidth; // Calculate the x-coordinate for drawing the next bar on the graph.
        }

        // Add a title and axis names.
        g.setFill(Color.BLACK);
        g.setFont(Font.font(24));
        g.fillText("Rainfall: " + firstYear + " to " + lastYear, width/2.5, border_width);

        g.setFont(Font.font(15));
        g.fillText("Months", width/2.0, height-5);

        g.rotate(-90);
        g.fillText("Rainfall (millimeters)",-height/1.6, border_width-5);
    }

    /** Create the canvas for drawing the picture,
     * Create the border of the graph and
     * Add the title to the graph.
     **/
    //------ Implementation details: DO NOT EDIT THIS ------
    public void start(Stage stage) {
        int width = 200 * 6 + 40;
        int height = 500;
        Canvas canvas = new Canvas(width, height); // Create the canvas to draw the picture.
        drawPicture(canvas.getGraphicsContext2D(), width, height); // Draw the graph.
        BorderPane root = new BorderPane(canvas); // Create the border of the graph.
        root.setStyle("-fx-border-width: 5px; -fx-border-color: #444");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser"); // Add the title to the graph.
        stage.show();
        stage.setResizable(false); // Prevent the graph from being resized.
    }
    //------ End of Implementation details ------


    public static void main(String[] args) {
        System.out.print("Enter path: ");
        var path = TextIO.getln();

        // File paths used for testing.
        // var path = "src/main/resources/rainfalldata_analysed/MountSheridanStationCNS_analysed.csv";
        // var path = "src/main/resources/rainfalldata_analysed/IDCJAC0009_031205_1800_Data_analysed.csv";
        TextIO.readFile(path);
        launch();
    }

}
