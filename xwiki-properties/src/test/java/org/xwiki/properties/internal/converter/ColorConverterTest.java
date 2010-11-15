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
package org.xwiki.properties.internal.converter;

import java.awt.Color;

import org.xwiki.properties.converter.ConversionException;
import org.xwiki.properties.converter.Converter;
import org.xwiki.test.AbstractXWikiComponentTestCase;

/**
 * Validate {@link ColorConverter} component.
 * 
 * @version $Id$
 */
public class ColorConverterTest extends AbstractXWikiComponentTestCase
{
    private Converter colorConverter;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.colorConverter = getComponentManager().lookup(Converter.class, Color.class.getName());
    }

    public void testConvertRGB()
    {
        assertEquals(Color.WHITE, this.colorConverter.convert(Color.class, "255 , 255 , 255"));
    }

    public void testConvertHTML()
    {
        assertEquals(Color.WHITE, this.colorConverter.convert(Color.class, "#ffffff"));
        assertEquals(Color.WHITE, this.colorConverter.convert(Color.class, "#FFFFFF"));
    }

    public void testConvertInvalid()
    {
        try {
            assertEquals(Color.WHITE, this.colorConverter.convert(Color.class, "wrongformat"));
            fail("Should have thrown a ConversionException exception");
        } catch (ConversionException expected) {
            // expected
        }
    }
}