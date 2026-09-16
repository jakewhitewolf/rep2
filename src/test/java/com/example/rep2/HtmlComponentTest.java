package com.example.rep2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlComponentTest {

    @Test
    void textNodeRenderTest() {
        TextNode textNode = new TextNode("Hello");

        assertEquals("Hello", textNode.renderHTML());
    }

    @Test
    void imageNodeRenderTest() {
        ImageNode imageNode = new ImageNode("image.png");

        assertEquals(
                "<img src=\"image.png\">",
                imageNode.renderHTML()
        );
    }

    @Test
    void containerRenderTest() {
        HtmlContainer div = new HtmlContainer("div");
        div.add(new TextNode("Hello"));

        assertEquals(
                "<div>Hello</div>",
                div.renderHTML()
        );
    }

    @Test
    void nestedContainerRenderTest() {
        HtmlContainer body = new HtmlContainer("body");
        HtmlContainer div = new HtmlContainer("div");

        div.add(new TextNode("Hello"));
        body.add(div);

        assertEquals(
                "<body><div>Hello</div></body>",
                body.renderHTML()
        );
    }

    @Test
    void cssClassTest() {
        HtmlContainer div = new HtmlContainer("div");

        div.addCSSClass("container");

        assertEquals(
                "<div class=\"container\"></div>",
                div.renderHTML()
        );
    }

    @Test
    void tableRowRenderTest() {
        TableRowNode row = new TableRowNode(
                "Иван | 20 | Москва"
        );

        assertEquals(
                "<tr><td>Иван</td><td>20</td><td>Москва</td></tr>",
                row.renderHTML()
        );
    }

    @Test
    void emptyTagExceptionTest() {
        assertThrows(
                CompositeException.class,
                () -> new HtmlContainer("")
        );
    }

    @Test
    void addChildToLeafExceptionTest() {
        TextNode textNode = new TextNode("Hello");

        assertThrows(
                CompositeException.class,
                () -> textNode.add(new TextNode("World"))
        );
    }
}