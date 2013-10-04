/*
 *  Copyright (C) 2013 integratedApps
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 *  BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.integratedapps.breloque.commons.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class XmlUtils {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(XmlUtils.class.getName());

    public static Document createDocument(
            final String namespace,
            final String name) throws Exception {

        return DocumentBuilderFactory.
                newInstance().
                newDocumentBuilder().
                getDOMImplementation().
                createDocument(namespace, name, null);
    }

    public static String toXml(
            final Date date) throws Exception {

        if (date == null) {
            return null;
        }

        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar).toXMLFormat();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private XmlUtils(
            ) {

        // Does nothing.
    }

}
