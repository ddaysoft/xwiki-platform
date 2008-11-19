package com.xpn.xwiki.wysiwyg.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * This {@link ImageBundle} is used for all the button icons. Using an image bundle allows all of these images to be
 * packed into a single image, which saves a lot of HTTP requests, drastically improving startup time.
 */
public interface Images extends ImageBundle
{
    Images INSTANCE = (Images) GWT.create(Images.class);

    /**
     * This image was added just to prevent IE6 from crashing. It's a stupid fix but nevertheless it allows us to test
     * the editor on IE6 till we find a better one.
     * 
     * @return An enchanted image to be used against IE6's black magic.
     */
    AbstractImagePrototype fixIE();

    @Resource("attachment.gif")
    AbstractImagePrototype attachment();

    @Resource("backcolor.gif")
    AbstractImagePrototype backColor();

    @Resource("bold.gif")
    AbstractImagePrototype bold();

    @Resource("charmap.gif")
    AbstractImagePrototype charmap();

    // @Resource("copy.gif")
    // AbstractImagePrototype copy();

    @Resource("close.gif")
    AbstractImagePrototype close();

    // @Resource("cut.gif")
    // AbstractImagePrototype cut();

    @Resource("table_delete_col.gif")
    AbstractImagePrototype deleteCol();

    @Resource("table_delete_row.gif")
    AbstractImagePrototype deleteRow();

    @Resource("forecolor.gif")
    AbstractImagePrototype foreColor();

    @Resource("hr.gif")
    AbstractImagePrototype hr();

    @Resource("image.gif")
    AbstractImagePrototype image();

    @Resource("indent.gif")
    AbstractImagePrototype indent();

    @Resource("table_delete.gif")
    AbstractImagePrototype deleteTable();

    @Resource("table_insert_col_after.gif")
    AbstractImagePrototype insertColAfter();

    @Resource("table_insert_col_before.gif")
    AbstractImagePrototype insertColBefore();

    @Resource("table_insert_row_after.gif")
    AbstractImagePrototype insertRowAfter();

    @Resource("table_insert_row_before.gif")
    AbstractImagePrototype insertRowBefore();

    @Resource("table.gif")
    AbstractImagePrototype insertTable();

    @Resource("italic.gif")
    AbstractImagePrototype italic();

    @Resource("justifycenter.gif")
    AbstractImagePrototype justifyCenter();

    @Resource("justifyfull.gif")
    AbstractImagePrototype justifyFull();

    @Resource("justifyleft.gif")
    AbstractImagePrototype justifyLeft();

    @Resource("justifyright.gif")
    AbstractImagePrototype justifyRight();

    @Resource("link.gif")
    AbstractImagePrototype link();

    @Resource("macro.gif")
    AbstractImagePrototype macro();

    @Resource("ol.gif")
    AbstractImagePrototype ol();

    @Resource("outdent.gif")
    AbstractImagePrototype outdent();

    // @Resource("paste.gif")
    // AbstractImagePrototype paste();

    @Resource("redo.gif")
    AbstractImagePrototype redo();

    @Resource("removeformat.gif")
    AbstractImagePrototype removeFormat();

    @Resource("strikethrough.gif")
    AbstractImagePrototype strikeThrough();

    @Resource("subscript.gif")
    AbstractImagePrototype subscript();

    @Resource("superscript.gif")
    AbstractImagePrototype superscript();

    @Resource("sync.gif")
    AbstractImagePrototype sync();

    @Resource("tt.gif")
    AbstractImagePrototype teletype();

    @Resource("ul.gif")
    AbstractImagePrototype ul();

    @Resource("underline.gif")
    AbstractImagePrototype underline();

    @Resource("undo.gif")
    AbstractImagePrototype undo();

    @Resource("unlink.gif")
    AbstractImagePrototype unlink();
}
