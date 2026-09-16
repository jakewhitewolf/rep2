package com.example.rep2;

public class ImageNode extends HtmlComponent {

    private final String src;

    public ImageNode(String src) {
        if (src == null || src.isBlank()) {
            throw new CompositeException("Путь к изображению не может быть пустым");
        }

        this.src = src;
    }

    @Override
    public String renderHTML() {
        StringBuilder result = new StringBuilder();

        result.append("<img src=\"").append(src).append("\"");

        if (cssClass != null && !cssClass.isBlank()) {
            result.append(" class=\"").append(cssClass).append("\"");
        }

        result.append(">");

        return result.toString();
    }

    public String getSrc() {
        return src;
    }
}