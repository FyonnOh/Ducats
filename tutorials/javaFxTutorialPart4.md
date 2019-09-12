# JavaFX Tutorial 4 – FXML 

While we have produced a fully functional prototype, there are a few major problems with our application.

1. The process of visually enhancing the GUI is long and painful:
   * Does the `TextField` need to be 330px or 325px wide? 
   * How much padding is enough padding to look good?

   Every small change requires us to rebuild and run the application.  

1. Components are heavily dependent on each other:
   Why does `duke.Main` need to know that `duke.gui.DialogBox` needs a `Label`? 
   What happens if we change the `Label` to a custom `ColoredLabel` in the future?  
    
    We need to minimize the amount of information each control needs to know about another.
    Otherwise, making changes in the future will break existing features.

1. The code is untidy and long:
   Why is all the code in one place?

   The `duke.Main` class attempts to do it all. 
   Code for visual tweaks, listeners and even utility methods are all in one file.
   This makes it difficult to find and make changes to existing code.

FXML is a XML-based language that allows us to define our user interface. Properties of JavaFX objects can be defined in the FXML file. For example:  
```xml
 <TextField fx:id="userInput" layoutY="558.0" onAction="#handleUserInput" prefHeight="41.0" prefWidth="324.0" AnchorPane.bottomAnchor="1.0" />
```

The FXML snippet define a TextField similar to the one that we programmatically defined previous in Tutorial 2. Notice how concise FXML is compared to the plain Java version.

Let's return to duke.Duke and convert it to use FXML instead.

# Rebuilding the Scene using FXML

Scene Builder is a tool developed by Oracle and currently maintained by Gluon. It is a What-You-See-Is-What-You-Get GUI creation tool. [Download](https://gluonhq.com/products/scene-builder/#download) the appropriate version for your OS and install it.

Create the following files in `src/main/resources/view`:

**duke.gui.MainWindow.fxml**
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ScrollPane?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>

<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="600.0" prefWidth="400.0" xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1" fx:controller="duke.gui.MainWindow">
   <children>
      <TextField fx:id="userInput" layoutY="558.0" onAction="#handleUserInput" prefHeight="41.0" prefWidth="324.0" AnchorPane.bottomAnchor="1.0" />
      <Button fx:id="sendButton" layoutX="324.0" layoutY="558.0" mnemonicParsing="false" onAction="#handleUserInput" prefHeight="41.0" prefWidth="76.0" text="Send" />
      <ScrollPane fx:id="scrollPane" hbarPolicy="NEVER" hvalue="1.0" prefHeight="557.0" prefWidth="400.0" vvalue="1.0">
         <content>
            <VBox fx:id="dialogContainer" prefHeight="552.0" prefWidth="388.0" />
         </content>
      </ScrollPane>
   </children>
</AnchorPane>
```

**duke.gui.DialogBox.fxml**
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.HBox?>

<fx:root alignment="TOP_RIGHT" maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308" prefWidth="400.0" type="javafx.scene.layout.HBox" xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label fx:id="dialog" text="Label" wrapText="true" />
      <ImageView fx:id="displayPicture" fitHeight="99.0" fitWidth="99.0" pickOnBounds="true" preserveRatio="true" />
   </children>
   <padding>
      <Insets bottom="15.0" left="5.0" right="5.0" top="15.0" />
   </padding>
</fx:root>
```

1. Let’s explore the provided FXML files in Scene Builder. 
    
    Running the tool brings up the main screen.
    Select `Open Project` > `src/main/resources/view/duke.gui.MainWindow.fxml`. Inspect each control and its properties.

   ![SceneBuilder opening duke.gui.MainWindow.fxml](assets/SceneBuilder.png)

1. On the right accordion pane, you can modify the properties of the control that you have selected. Try changing the various settings and see what they do!
 
1. On the left accordion, you can see that we have set the controller class to `duke.gui.MainWindow`. 
We will get to that later.
 
   ![Controller for duke.gui.MainWindow](assets/MainWindowController.png)

1. Let’s repeat the process for `duke.gui.DialogBox`.
   The main difference here is that duke.gui.DialogBox checks `Use fx:root construct` and _does not define a controller class_. 

   ![Settings for duke.gui.DialogBox](assets/DialogBoxController.png)

## Using Controllers

As part of the effort to separate the code handling duke.Duke's logic and UI, let's _refactor_ the UI-related code to its own class.
We call these UI classes _controllers_. 

Let's implement the `duke.gui.MainWindow` controller class that we specified in `duke.gui.MainWindow.fxml`.

**duke.gui.MainWindow.java**
```java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for duke.gui.MainWindow. Provides the layout for the other controls.
 */
public class duke.gui.MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private duke.Duke duke;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setDuke(duke.Duke d) {
        duke = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing duke.Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = duke.getResponse(input);
        dialogContainer.getChildren().addAll(
                duke.gui.DialogBox.getUserDialog(input, userImage),
                duke.gui.DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }
}
```

The `@FXML` annotation marks a `private` or `protected` member and makes it accessible to FXML despite its modifier.
Without the annotation, we will have to make everything `public` and expose our UI to unwanted changes.

The `FXMLLoader` will map the a control with a `fx:id` defined in FXML to a variable with the same name in its controller.
Notice how in `duke.gui.MainWindow`, we can invoke `TextField#clear()` on `userInput` and access its content just as we did in the previous example.
Similarly, methods like private methods like `handleUserInput` can be used in FXML when annotated by `@FXML`. 

## Using FXML in our application

Let's create a new `duke.Main` class as the bridge between the existing logic in `duke.Duke` and the UI in `duke.gui.MainWindow`.

**duke.Main.java**
```java
@Override
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for duke.Duke using FXML.
 */
public class duke.Main extends Application {

    private duke.Duke duke = new duke.Duke();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(duke.Main.class.getResource("/view/duke.gui.MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<duke.gui.MainWindow>getController().setDuke(duke);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Again, we can interact with the `AnchorPane` defined in the FXML as we would have if we created the `AnchorPane` ourselves.

For our custom `duke.gui.DialogBox`, we did not define a controller so let's create a controller for it.

**duke.gui.DialogBox.java**
```java
import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * An example of a custom control using FXML.
 * This control represents a dialog box consisting of an ImageView to represent the speaker's face and a label
 * containing text from the speaker.
 */
public class duke.gui.DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private duke.gui.DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(duke.gui.MainWindow.class.getResource("/view/duke.gui.DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static duke.gui.DialogBox getUserDialog(String text, Image img) {
        return new duke.gui.DialogBox(text, img);
    }

    public static duke.gui.DialogBox getDukeDialog(String text, Image img) {
        var db = new duke.gui.DialogBox(text, img);
        db.flip();
        return db;
    }
}
```

When we create a new instance of `duke.gui.DialogBox`, we set both the controller and root Node to `duke.gui.DialogBox`. 
From this point onwards we can interact with `duke.gui.DialogBox` as we have in the previous tutorials.

The last change that we have to make is to point our `duke.gui.Launcher` class in the right direction:
In `duke.gui.Launcher.java`
```java
//...    
Application.launch(duke.Main.class, args);
//...
```
[todo]: # (Discussion on the fx:root pattern.)

## Exercises

1. Convert `duke.gui.MainWindow` to use the `fx:root` construct.
1. Extend `duke.gui.MainWindow` to have a `Stage` as a root Node.
1. Customize the appearance of the application further with CSS.

--------------------------------------------------------------------------------
**Authors:**
* Initial Version: Jeffry Lum