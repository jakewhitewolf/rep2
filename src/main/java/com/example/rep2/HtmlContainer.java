package com.example.rep2;

import java.util.ArrayList;
import java.util.List;

public class HtmlContainer extends HtmlComponent {

    private final String tag;
    private final List<HtmlComponent> children = new ArrayList<>();

    public HtmlContainer(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new CompositeException("Имя HTML-тега не может быть пустым");
        }

        this.tag = tag;
    }

    @Override
    public void add(HtmlComponent component) {
        if (component == null) {
            throw new CompositeException("Нельзя добавить пустой элемент");
        }

        children.add(component);
    }

    @Override
    public void remove(HtmlComponent component) {
        if (!children.remove(component)) {
            throw new CompositeException("Элемент не найден в контейнере");
        }
    }

    @Override
    public String renderHTML() {
        StringBuilder result = new StringBuilder();

        result.append("<").append(tag);

        if (cssClass != null && !cssClass.isBlank()) {
            result.append(" class=\"").append(cssClass).append("\"");
        }

        result.append(">");

        for (HtmlComponent component : children) {
            result.append(component.renderHTML());
        }

        result.append("</").append(tag).append(">");

        return result.toString();
    }

    public List<HtmlComponent> getChildren() {
        return children;
    }

    public String getTag() {
        return tag;
    }
}