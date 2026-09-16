package com.example.rep2;

public abstract class HtmlComponent {

    protected String cssClass;

    public abstract String renderHTML();

    public void addCSSClass(String cssClass) {
        if (cssClass == null || cssClass.isBlank()) {
            throw new CompositeException("CSS-класс не может быть пустым");
        }

        this.cssClass = cssClass;
    }

    public void add(HtmlComponent component) {
        throw new CompositeException("В этот элемент нельзя добавлять дочерние элементы");
    }

    public void remove(HtmlComponent component) {
        throw new CompositeException("У этого элемента нет дочерних элементов");
    }
}