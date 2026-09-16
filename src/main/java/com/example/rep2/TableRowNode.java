package com.example.rep2;

public class TableRowNode extends HtmlComponent {

    private final String content;

    public TableRowNode(String content) {
        if (content == null || content.isBlank()) {
            throw new CompositeException("Строка таблицы не может быть пустой");
        }

        this.content = content;
    }

    @Override
    public String renderHTML() {
        StringBuilder result = new StringBuilder();

        result.append("<tr");

        if (cssClass != null && !cssClass.isBlank()) {
            result.append(" class=\"").append(cssClass).append("\"");
        }

        result.append(">");

        String[] cells = content.split("\\|");

        for (String cell : cells) {
            result.append("<td>")
                    .append(cell.trim())
                    .append("</td>");
        }

        result.append("</tr>");

        return result.toString();
    }

    public String getContent() {
        return content;
    }
}