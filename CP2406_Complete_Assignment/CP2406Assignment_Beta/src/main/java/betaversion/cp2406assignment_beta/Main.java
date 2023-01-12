package betaversion.cp2406assignment_beta;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * CP2406 Assignment - Soumyadeep Sarkar
 * Beta version of Rainfall Analyser
 * Main Class to be run which uses the Rainfall Visualiser/Analyser classes.
 * Creates an interactive app using JavaFX.
 *
 * Link for the project repo: https://github.com/Sarkar-Soumyadeep-202002/CP2406_Assignment.git
 */
public class Main extends Application {

    private static RainfallData rainfallData = new RainfallData(); // Create an object of the RainfallData class.
    private static final RainfallAnalyser rainfallAnalyser = new RainfallAnalyser(); // Create an object of the RainfallAnalyser class.

    private final Stage homeStage = new Stage();
    private final Stage visualiserStage = new Stage();
    private final Stage helpStage = new Stage();
    private final Label statusBar = new Label();

    /**
     * Main method that starts the application
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Main JavaFX method. Overrides the original method of the JavaFX package.
     * Builds the three main stages of the app.
     */
    @Override
    public void start(Stage stage) {

        // Build the stages to be used in the program.
        buildHomeStage();
        buildVisualiserStage();
        buildHelpStage();
        statusBar.setText("Please load a rainfall csv to be analysed"); // Display the instructions for the user.

        // Set the stage to the Home Scene and show it.
        homeStage.show();
    }

    /**
     * Builds the stage used for the Home Stage
     */
    private void buildHomeStage() {
        // Set up all the labels and put them in a VBox
        Label message = new Label("Welcome to the Rainfall Visualiser");
        message.setFont(new Font(20));
        statusBar.setAlignment(Pos.CENTER); // Align the status Bar to the center.
        VBox labelBar = new VBox(message, statusBar); // Display the message on the status bar.
        labelBar.setAlignment(Pos.CENTER);

        // Set up all the buttons and put them in a HBox.
        Button startButton = new Button("Start Visualiser");
        Button loadButton = new Button("Load Rainfall Data");
        Button quitButton = new Button("Quit");

        // Add tooltips to each of the main buttons
        Tooltip startTooltip = new Tooltip(), loadTooltip = new Tooltip(), quitTooltip = new Tooltip();
        startTooltip.setText("Starts the Rainfall Visualiser");
        Tooltip.install(startButton, startTooltip);
        loadTooltip.setText("Load the Rainfall Data from a file on your computer");
        Tooltip.install(loadButton, loadTooltip);
        quitTooltip.setText("Exits the Rainfall Visualiser");
        Tooltip.install(quitButton, quitTooltip);

        // Assign the actions to each of the main buttons.
        startButton.setOnAction(e -> {
            homeStage.hide();
            visualiserStage.show();
        });
        loadButton.setOnAction(e -> rainfallData = loadRainfallData());
        quitButton.setOnAction(e -> Platform.exit());

        HBox buttonBar = new HBox(50, startButton, loadButton, quitButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPrefHeight(50);

        MenuButton menuButton = buildMenuButton(); // Create a Menu button.
        MenuBar menuBar = buildMenuBar(); // Create the menu bar.

        // Set up the home root and scene.
        BorderPane homeRoot = new BorderPane();
        homeRoot.setCenter(labelBar);
        homeRoot.setBottom(buttonBar);
        homeRoot.setRight(menuButton);
        homeRoot.setTop(menuBar);
        homeRoot.setStyle("-fx-border-width: 2px; -fx-border-color: #444");

        // Build the home scene and assign it to the home stage.
        Scene homeScene = new Scene(homeRoot, 600, 400);
        homeStage.setScene(homeScene);
        homeStage.setTitle("CP2406 Assignment - Soumyadeep Sarkar");
        homeStage.centerOnScreen();
        homeStage.setResizable(false);

    } // end buildHomeStage()

    /**
     * Creates the Stage for the Rainfall Visualiser.
     * Get the StackedBarChart from the Rainfall Visualiser class.
     */
    private void buildVisualiserStage() {
        BorderPane visualiserRoot = new BorderPane(RainfallVisualiser.getRainfallBarChart(rainfallData));
        Button returnButton = new Button("Close Visualiser");

        MenuButton menuButton = buildMenuButton();
        menuButton.setPopupSide(Side.TOP);

        HBox visualiserHBox = new HBox(100, returnButton, menuButton);
        visualiserHBox.setAlignment(Pos.CENTER);
        visualiserHBox.setPrefHeight(30);

        MenuBar menuBar = buildMenuBar(); // Create the Menu button.

        visualiserRoot.setTop(menuBar);
        visualiserRoot.setBottom(visualiserHBox);
        visualiserRoot.setStyle("-fx-border-width: 2px; -fx-border-color: GRAY"); // Define the style of the border.
        Scene visualiserScene = new Scene(visualiserRoot, 1200, 600);

        visualiserStage.setScene(visualiserScene);
        visualiserStage.setTitle("Rainfall Visualiser");
        visualiserStage.centerOnScreen();
        visualiserStage.setResizable(true);

        // Perform actions on the return button.
        returnButton.setOnAction(actionEvent -> {
            visualiserStage.hide();
            homeStage.show();
        });
    }

    /**
     * Builds the help stage. Used to give the user basic instructions
     * on running the app.
     */
    private void buildHelpStage() {
        TabPane helpPane = new TabPane(); // Create an object for the help stage.
        helpPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        helpPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        helpPane.setStyle("-fx-border-width: 2px; -fx-border-color: GRAY"); // Set the style of the border of the help stage.

        // Write the labels for the different pages under the help stage and display them.
        Label generalHelp = new Label("""
                Welcome to the Rainfall visualiser app.
                
                This app will analyse a Comma Separated Value (CSV) file and
                give the recorded rainfall data as a bar chart on the
                visualiser screen.
                
                This app was designed for the subject CP2406 at JCU by
                Soumyadeep Sarkar.
                
                It makes use of the Commons CSV package from Apache.
                
                Please select a specific tab if you need a more detailed
                explanation.""");
        Label loadingHelp = new Label("""
                There are three ways to load a file into the Rainfall Visualiser.

                The first is to use the load option from the top of the app.
                This will open up a file explorer window and allows the user
                to enter a new file to be analysed and then represented with
                a graph on the visualiser. This option is available from both
                the home screen and the visualiser screen.

                The second is by clicking the button on the home screen. This
                also opens up the file explorer to load in a new file.

                The third is from the drop down list on the left of the home
                screen. This is a list of previously loaded and saved files
                and are stored in the project itself. Files can only be added
                to this list by choosing the save option in the app itself.""");
        Label savingHelp = new Label("""
                To save a file, the user needs to select the save file at the
                top of the app screen. The app needs to have a file loaded into
                the app before it can save any rainfall data. This saved data is
                stored in the project and can only be loaded in through the drop
                down list on the right on the home screen.

                This feature was included to prevent the user from having to
                manually find a file each time they wanted to view the graph
                in the visualiser.""");
        Label visualiserHelp = new Label("""
                The visualiser represents the currently loaded rainfall data
                as a bar chart. The user can hover their cursor over the bar
                of an entry and get its current value. This value includes the
                date, whether it is a minimum, maximum or total value in
                millimeters. A new file can be loaded into the chart from the
                visualiser screen by making use of the menu bar at the top
                of the screen. The bar chart will update itself on this screen.""");

        // Create the different tabs for the help stage.
        Tab general = new Tab("General", generalHelp);
        Tab load = new Tab("Loading", loadingHelp);
        Tab save = new Tab("Saving", savingHelp);
        Tab visualiser = new Tab("Visualiser", visualiserHelp);

        helpPane.getTabs().addAll(general, load, save, visualiser); // Add the different tabs of the help stage to the action bar when the help button is clicked.
        Button close = new Button("Close Help"); // Create the close button.
        close.setOnAction(e -> helpStage.hide()); // Perform an action on the close button.
        VBox helpBox = new VBox(helpPane, close);
        helpBox.setAlignment(Pos.CENTER);

        // Create a scene to display the help stage.
        Scene helpScene = new Scene(helpBox, 400, 350);
        helpStage.setScene(helpScene); // Display the help stage.
        helpStage.setTitle("Help for Rainfall Visualiser");
        helpStage.setResizable(false);
        helpStage.initStyle(StageStyle.UTILITY); // Define the style for the help stage.

        // Closes the help screen when focus is lost from the home stage.
        helpStage.focusedProperty().addListener( (observableValue,oldVal,newVal) -> {
            if (!newVal) {
                helpStage.hide();
            }
        });
    }

    /**
     * Build the menu button for the home stage.
     * It is used to load in previously saved and analysed Rainfall data files.
     */
    private MenuButton buildMenuButton() {
        MenuButton menuButton = new MenuButton();
        menuButton.setText("Saved Rainfall Data");
        menuButton.setAlignment(Pos.CENTER);

        File f = new File("src/main/resources/betaversion/cp2406assignment_beta/analysedrainfalldata"); // Access the analysedrainfalldata directory which stores the files which have been saved and analysed.
        if (Objects.requireNonNull(f.list()).length == 0) { // Check if the selected folder has no files which have been analysed.
            MenuItem noData = new MenuItem("No saved analysed rainfall data");
            menuButton.getItems().add(noData); // Add an item to the Menu.
        }
        else {
            for (String filename : Objects.requireNonNull(f.list())) {
                MenuItem choice = new MenuItem(filename); // Create a new menu item with the current filename.
                choice.setOnAction(e -> {
                    String path = f.getAbsolutePath() + "/" + filename; // For Linux systems, add "/" along with the filename and for windows use either "\\" or "/" along with filename for the path.
                    try {
                        rainfallData = rainfallAnalyser.getAnalysedRainfallData(path); // Analyse the file selected
                        buildVisualiserStage();
                        statusBar.setText(filename + " successfully loaded");
                    } catch (IOException ex) {
                        // Because these files are created by the program, this error shouldn't occur.
                        statusBar.setText(filename + " failed to load.\n " + ex.getMessage());
                    }
                });
                menuButton.getItems().add(choice); // Add the filename to the Menu.
            }
        }
        return menuButton;
    }

    /**
     * Build the menu bar for the Home stage and the Visualiser Stage.
     * Acts as the only way to change the Rainfall data set while on
     * the visualiser stage. Can also be used to load the help stage
     * or close the app on any stage.
     */
    private MenuBar buildMenuBar() {
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu); // Add the file menu and the help menu to the Action Bar.

        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> loadRainfallData()); // Click the open button to select a file from your computer.

        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> { // Click the save button to save the selected file to the list of analysed files.
            String filename = rainfallAnalyser.saveRainfallData(rainfallData);
            buildHomeStage();
            buildVisualiserStage();
            if (filename == null) // Display an appropriate message if no file is selected.
                statusBar.setText("No file loaded to save");
            else
                statusBar.setText(filename + " successfully saved");
        });

        MenuItem close = new MenuItem("Close");
        close.setOnAction(e -> Platform.exit()); // Click the close button to quit the rainfall visualiser.

        fileMenu.getItems().addAll(open, save, close); // Create the dropdown menu for the File menu.

        MenuItem help = new MenuItem("Help");
        help.setOnAction(e -> helpStage.show()); // Click the help button to open the help tab.
        helpMenu.getItems().add(help); // Add the help button to the help tab.

        return menuBar;
    }

    /**
     * Opens up a file explorer menu and lets the user load in a csv rainfall file
     * for analysis.
     * Responsible for identifying errors thrown by the RainfallAnalyser class.
     * Returns the analysed data set to be represented by the bar chart on the
     * visualiser stage. Updates the visualiser stage each time it is run.
     */
    private RainfallData loadRainfallData() {
        FileChooser chooser = new FileChooser(); // Create an object to select a file from your system.
        File file = chooser.showOpenDialog(homeStage); // Display the local directories of the user at the same location of the screen as the home stage.
        // Check that the file chooser successfully loaded a file.
        if (file == null) { // Check if a file has been selected.
            statusBar.setText("No file loaded");
            return null;
        }
        String path = file.getAbsolutePath();

        try {
            rainfallData = rainfallAnalyser.analyseRainfallData(path);
            statusBar.setText(rainfallData.getFilename() + " successfully loaded");
            buildVisualiserStage();
        } catch (Exception err) {
            statusBar.setText("Failed to load " + file.getName() + "\n" +
                    err.getMessage());
        }
        return rainfallData;
    }

}
