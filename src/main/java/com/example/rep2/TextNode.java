package com.example.rep2;

public class TextNode extends HtmlComponent {

    private final String text;

    public TextNode(String text) {
        if (text == null || text.isBlank()) {
            throw new CompositeException("Текстовый узел не может быть пустым");
        }

        this.text = text;
    }

    @Override
    public String renderHTML() {
        if (cssClass != null && !cssClass.isBlank()) {
            return "<span class=\"" + cssClass + "\">" + text + "</span>";
        }

        return text;
    }

    public String getText() {
        return text;
    }
}