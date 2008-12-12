/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.internal.parser.wikimodel.xhtml;

import java.util.Stack;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiReferenceParser;
import org.wikimodel.wem.xhtml.handler.CommentHandler;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack;
import org.wikimodel.wem.xwiki.XWikiReferenceParser;
import org.xwiki.rendering.internal.parser.wikimodel.XDOMGeneratorListener;
import org.xwiki.rendering.listener.Image;
import org.xwiki.rendering.parser.ImageParser;
import org.xwiki.rendering.parser.LinkParser;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.parser.Syntax;
import org.xwiki.rendering.parser.SyntaxType;
import org.xwiki.rendering.renderer.PrintRendererFactory;
import org.xwiki.rendering.renderer.XHTMLRenderer;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;

/**
 * Handle Link and Macro definitions in comments (we store links in a comment since otherwise there are situations
 * where it's not possible to reconstruct the original reference from the rendered HTML value and for macros it
 * wouldn't be possible at all to reconstruct the macro).
 *  
 * @version $Id$
 * @since 1.7M1
 */
public class XWikiCommentHandler extends CommentHandler
{
    private Parser parser;
    
    private LinkParser linkParser;
    
    private ImageParser imageParser;
    
    private PrintRendererFactory printRendererFactory;
    
    private WikiReferenceParser referenceParser;

    /**
     * We're using a stack so that we can have nested comment handling. For example when we have a link to an image
     * we need nested comment support.
     */
    private Stack<String> commentContentStack = new Stack<String>();
    
    public XWikiCommentHandler(Parser parser, LinkParser linkParser, ImageParser imageParser, PrintRendererFactory printRendererFactory)
    {
        this.parser = parser;
        this.linkParser = linkParser;
        this.printRendererFactory = printRendererFactory;
        this.referenceParser = new XWikiReferenceParser();
        this.imageParser = imageParser;
    }
    
    @Override
    public void onComment(String content, TagStack stack)
    {
        // If the comment starts with "startwikilink" then we need to gather all XHTML tags inside
        // the A tag, till we get a "stopwikilink" comment.
        // Same for "startimage" and "stopimage".
        if (content.startsWith("startwikilink:")) {
            handleLinkCommentStart(content, stack);
        } else if (content.startsWith("stopwikilink")) {
            handleLinkCommentStop(content, stack);
        } else if (content.startsWith("startimage:")) {
            handleImageCommentStart(content, stack);
        } else if (content.startsWith("stopimage")) {
            handleImageCommentStop(content, stack);
        } else {
            super.onComment(content, stack);
        }
    }
    
    private void handleLinkCommentStart(String content, TagStack stack)
    {
        XDOMGeneratorListener listener = new XDOMGeneratorListener(this.parser, this.linkParser, this.imageParser);
        stack.setStackParameter("xdomGeneratorListener", listener);
        stack.setStackParameter("isInLink", true);
        this.commentContentStack.push(content.substring("startwikilink:".length()));
    }

    private void handleLinkCommentStop(String content, TagStack stack)
    {
        DefaultWikiPrinter printer = new DefaultWikiPrinter();            
        XHTMLRenderer renderer = 
            (XHTMLRenderer) this.printRendererFactory.createRenderer(new Syntax(SyntaxType.XHTML, "1.0"), printer);
        XDOMGeneratorListener listener = (XDOMGeneratorListener) stack.getStackParameter("xdomGeneratorListener");
        listener.getXDOM().traverse(renderer);

        boolean isFreeStandingLink = (Boolean) stack.getStackParameter("isFreeStandingLink");
        WikiParameters params = (WikiParameters) stack.getStackParameter("linkParameters");
        String linkComment = this.commentContentStack.pop();
        if (isFreeStandingLink) {
            stack.getScannerContext().onReference(linkComment);
        } else {
            WikiReference wikiReference = this.referenceParser.parse(
                (printer.toString().length() > 0 ? printer.toString() + ">>" : "") + linkComment
                + (params.getSize() > 0 ? "||" + params.toString() : ""));
            stack.getScannerContext().onReference(wikiReference);
        }
        
        stack.setStackParameter("xdomGeneratorListener", null);
        stack.setStackParameter("isInLink", false);
        stack.setStackParameter("isFreeStandingLink", false);
        stack.setStackParameter("linkParameters", WikiParameters.EMPTY);
    }

    private void handleImageCommentStart(String content, TagStack stack)
    {
        stack.setStackParameter("isInImage", true);
        this.commentContentStack.push(content.substring("startimage:".length()));  
    }
    
    private void handleImageCommentStop(String content, TagStack stack)
    {
        boolean isFreeStandingImage = (Boolean) stack.getStackParameter("isFreeStandingImage");
        WikiParameters params = (WikiParameters) stack.getStackParameter("imageParameters");
        String imageComment = this.commentContentStack.pop();
        Image image = this.imageParser.parse(imageComment);
        
        if (isFreeStandingImage) {
            stack.getScannerContext().onReference("image:" + imageComment);
        } else {
            // Remove the ALT attribute if the content has the same value as the original image location
            // This is because the XHTML renderer automatically adds an ALT attribute since it is mandatory
            // in the XHTML specifications.
            WikiParameter altParameter = params.getParameter("alt"); 
            if (altParameter != null && altParameter.getValue().equals(image.getName())) {
                params = params.remove("alt");
            }
            
            WikiReference wikiReference = this.referenceParser.parse("image:" + imageComment
                + (params.getSize() > 0 ? "||" + params.toString() : ""));
            stack.getScannerContext().onReference(wikiReference);
        }
        
        stack.setStackParameter("isInImage", false);
        stack.setStackParameter("isFreeStandingImage", false);
        stack.setStackParameter("imageParameters", WikiParameters.EMPTY);
    }
}
