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
package com.integratedapps.breloque.commons.impl.data.marshall.plugins;

import com.integratedapps.breloque.commons.api.constants.Namespaces;
import com.integratedapps.breloque.commons.api.data.MarshallException;
import com.integratedapps.breloque.commons.impl.data.spi.MarshallManagerPlugin;
import com.integratedapps.breloque.commons.utils.StringUtils;
import com.integratedapps.breloque.commons.utils.XmlUtils;
import java.lang.reflect.Field;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class XmlMarshallManagerPlugin implements MarshallManagerPlugin {

    @Override
    public String getMimeType(
            ) {

        return "text/xml";
    }

    @Override
    public String marshall(
            final Object entity) throws MarshallException {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object marshallToNative(
            final Object entity) throws MarshallException {

        final Class clazz = entity.getClass();

        try {
            final Document document = XmlUtils.createDocument(Namespaces.ENTITIES, "entity");
            final Element rootElement = document.getDocumentElement();

            final Element entityElement = document.createElementNS(
                    Namespaces.ENTITIES, StringUtils.decapitalize(clazz.getSimpleName()));

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                final Element fieldElement = document.createElementNS(Namespaces.ENTITIES, field.getName());
                final Class<?> fieldClazz = field.getType();

                if (fieldClazz.equals(String.class)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Date.class)) {
                    fieldElement.setTextContent("" + XmlUtils.toXml((Date) field.get(entity)));
                } else if (fieldClazz.equals(Character.class) || fieldClazz.equals(Character.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Byte.class) || fieldClazz.equals(Byte.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Short.class) || fieldClazz.equals(Short.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Integer.class) || fieldClazz.equals(Integer.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Long.class) || fieldClazz.equals(Long.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Float.class) || fieldClazz.equals(Float.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Double.class) || fieldClazz.equals(Double.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                } else if (fieldClazz.equals(Boolean.class) || fieldClazz.equals(Boolean.TYPE)) {
                    fieldElement.setTextContent("" + field.get(entity));
                }

                entityElement.appendChild(fieldElement);
            }

            rootElement.appendChild(entityElement);

            return rootElement;
        } catch (Exception e) {
            throw new MarshallException(
                    "Failed to marshall the entity of class '" + clazz.getName() + "'.", e);
        }
    }

    @Override
    public <T> T unmarshall(
            final String entity,
            final Class<T> clazz) throws MarshallException {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T unmarshallFromNative(
            final Object entity,
            final Class<T> clazz) throws MarshallException {

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
