package com.example.rep2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import java.util.HashMap;
import java.util.Map;

public class MainController {

    @FXML
    private TreeView<String> structureTree;

    @FXML
    private TextField tagField;

    @FXML
    private TextField textField;



    @FXML
    private TextField rowField;

    @FXML
    private TextField cssClassField;

    @FXML
    private TextArea htmlPreview;

    @FXML
    private Label statusLabel;

    private final Map<TreeItem<String>, HtmlComponent> components = new HashMap<>();

    private HtmlContainer documentRoot;

    private final Properties properties = new Properties();

    @FXML
    public void initialize() {
        loadProperties();
        initializeDocument();
    }

    private void initializeDocument() {
        components.clear();

        documentRoot = new HtmlContainer(
                properties.getProperty("root.tag")
        );

        HtmlContainer body = new HtmlContainer(
                properties.getProperty("body.tag")
        );

        documentRoot.add(body);

        TreeItem<String> htmlItem = new TreeItem<>("html");
        TreeItem<String> bodyItem = new TreeItem<>("body");

        htmlItem.getChildren().add(bodyItem);

        htmlItem.setExpanded(true);
        bodyItem.setExpanded(true);

        components.put(htmlItem, documentRoot);
        components.put(bodyItem, body);

        structureTree.setRoot(htmlItem);
        structureTree.getSelectionModel().select(bodyItem);

        htmlPreview.clear();
        statusLabel.setText(
                properties.getProperty("default.status")
        );
    }

    @FXML
    private void onAddContainer() {
        String tag = tagField.getText().trim();

        if (tag.isEmpty()) {
            showError("Введите название HTML-тега");
            return;
        }

        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        try {
            HtmlComponent selectedComponent = components.get(selectedItem);
            HtmlContainer container = new HtmlContainer(tag);

            selectedComponent.add(container);

            TreeItem<String> newItem = new TreeItem<>(tag);
            selectedItem.getChildren().add(newItem);
            selectedItem.setExpanded(true);

            components.put(newItem, container);

            tagField.clear();
            statusLabel.setText("Добавлен контейнер <" + tag + ">");
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAddText() {
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            showError("Введите текст");
            return;
        }

        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        try {
            HtmlComponent selectedComponent = components.get(selectedItem);
            TextNode textNode = new TextNode(text);

            selectedComponent.add(textNode);

            String displayText = text;

            if (displayText.length() > 25) {
                displayText = displayText.substring(0, 25) + "...";
            }

            TreeItem<String> newItem =
                    new TreeItem<>("Текст: " + displayText);

            selectedItem.getChildren().add(newItem);
            selectedItem.setExpanded(true);

            components.put(newItem, textNode);

            textField.clear();
            statusLabel.setText("Добавлен текстовый узел");
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAddImage() {
        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выберите изображение");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Изображения",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.gif",
                        "*.webp"
                ),
                new FileChooser.ExtensionFilter(
                        "Все файлы",
                        "*.*"
                )
        );

        File file = fileChooser.showOpenDialog(
                structureTree.getScene().getWindow()
        );

        if (file == null) {
            return;
        }

        try {
            HtmlComponent selectedComponent = components.get(selectedItem);

            String path = file.toURI().toString();

            ImageNode imageNode = new ImageNode(path);

            selectedComponent.add(imageNode);

            TreeItem<String> newItem =
                    new TreeItem<>("Изображение: " + file.getName());

            selectedItem.getChildren().add(newItem);
            selectedItem.setExpanded(true);

            components.put(newItem, imageNode);

            statusLabel.setText(
                    "Добавлено изображение: " + file.getName()
            );

        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAddTableRow() {
        String content = rowField.getText().trim();

        if (content.isEmpty()) {
            showError("Введите содержимое строки таблицы");
            return;
        }

        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        try {
            HtmlComponent selectedComponent = components.get(selectedItem);
            TableRowNode rowNode = new TableRowNode(content);

            selectedComponent.add(rowNode);

            TreeItem<String> newItem =
                    new TreeItem<>("Строка таблицы: " + content);

            selectedItem.getChildren().add(newItem);
            selectedItem.setExpanded(true);

            components.put(newItem, rowNode);

            rowField.clear();
            statusLabel.setText("Добавлена строка таблицы");
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAddCssClass() {
        String cssClass = cssClassField.getText().trim();

        if (cssClass.isEmpty()) {
            showError("Введите CSS-класс");
            return;
        }

        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        try {
            HtmlComponent component = components.get(selectedItem);

            component.addCSSClass(cssClass);

            cssClassField.clear();
            statusLabel.setText(
                    "CSS-класс \"" + cssClass + "\" добавлен"
            );
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        TreeItem<String> selectedItem = getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        if (selectedItem == structureTree.getRoot()) {
            showError("Корневой элемент <html> удалить нельзя");
            return;
        }

        TreeItem<String> parentItem = selectedItem.getParent();

        if (parentItem == null) {
            return;
        }

        try {
            HtmlComponent parentComponent = components.get(parentItem);
            HtmlComponent selectedComponent = components.get(selectedItem);

            parentComponent.remove(selectedComponent);

            parentItem.getChildren().remove(selectedItem);
            removeFromMap(selectedItem);

            structureTree.getSelectionModel().select(parentItem);

            statusLabel.setText("Элемент удалён");
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onGenerate() {
        try {
            htmlPreview.setText(documentRoot.renderHTML());
            statusLabel.setText("HTML успешно сгенерирован");
        } catch (CompositeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        initializeDocument();
        statusLabel.setText("Документ очищен");
    }

    private TreeItem<String> getSelectedItem() {
        TreeItem<String> selectedItem =
                structureTree.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("Выберите элемент в структуре документа");
        }

        return selectedItem;
    }

    private void removeFromMap(TreeItem<String> item) {
        for (TreeItem<String> child : item.getChildren()) {
            removeFromMap(child);
        }

        components.remove(item);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Невозможно выполнить действие");
        alert.setContentText(message);
        alert.showAndWait();

        statusLabel.setText("Ошибка: " + message);
    }

    private void loadProperties() {
        try (InputStream input = getClass().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new CompositeException("Ресурсный файл config.properties не найден");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new CompositeException("Ошибка загрузки ресурсного файла");
        }
    }
}