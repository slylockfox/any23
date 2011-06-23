/*
 * Copyright 2008-2010 Digital Enterprise Research Institute (DERI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deri.any23.extractor.microdata;

import org.deri.any23.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describes a possible value for a <b>Microdata item property</b>.
 *
 * @author Michele Mostarda (mostarda@fbk.eu)
 */
public class ItemPropValue {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Supported types.
     */
    public enum Type {
        Plain,
        Link,
        Date,
        Nested
    }

    /**
     * Internal content value.
     */
    private final Object content;

    /**
     * Content type.
     */
    private final Type type;

    /**
     * Constructor.
     *
     * @param content content object.
     * @param type content type.
     */
    public ItemPropValue(Object content, Type type) {
        if(content == null) {
            throw new NullPointerException("content cannot be null.");
        }
        if(content instanceof String && ((String) content).trim().length() == 0) {
            throw new IllegalArgumentException("Invalid content '" + content + "'");
        }
        if(type == null) {
            throw new NullPointerException("type cannot be null.");
        }
        if(type == Type.Nested && ! (content instanceof ItemScope) ) {
            throw new IllegalArgumentException(
                    "content must be an " + ItemScope.class + " when type is " + Type.Nested
            );
        }
        this.content = content;
        this.type = type;
    }

    /**
     * @return the content object.
     */
    public Object getContent() {
        return content;
    }

    /**
     * @return the content type.
     */
    public Type getType() {
        return type;
    }

   /**
     * @return <code>true</code> if type is plain text.
     */
    public boolean isPlain() {
        return type == Type.Plain;
    }

    /**
     * @return <code>true</code> if type is a link.
     */
    public boolean isLink() {
        return type == Type.Link;
    }

    /**
     * @return <code>true</code> if type is a date.
     */
    public boolean isDate() {
        return type == Type.Date;
    }

    /**
     * @return <code>true</code> if type is a nested {@link ItemScope}.
     */
    public boolean isNested() {
        return type == Type.Nested;
    }

    /**
     * @return <code>true</code> if type is an integer.
     */
    public boolean isInteger() {
        if(type != Type.Plain) return false;
         try {
             Integer.parseInt((String) content);
             return true;
         } catch (Exception e) {
             return false;
         }
     }

    /**
     * @return <code>true</code> if type is a float.
     */
     public boolean isFloat() {
         if(type != Type.Plain) return false;
         try {
             Float.parseFloat((String) content);
             return true;
         } catch (Exception e) {
             return false;
         }
     }

    /**
     * @return <code>true</code> if type is a number.
     */
     public boolean isNumber() {
         return isInteger() || isFloat();
     }

    /**
     * @return the content value as integer, or raises an exception.
     * @throws NumberFormatException if the content is not an integer.
     */
     public int getAsInteger() {
         return Integer.parseInt((String) content);
     }

    /**
     * @return the content value as float, or raises an exception.
     * @throws NumberFormatException if the content is not an float.
     */
     public float getAsFloat() {
         return Float.parseFloat((String) content);
     }


    /**
     * @return the content as {@link Date}
     *         if <code>type == Type.DateTime</code>,
     *         <code>null</code> otherwise.
     * @throws java.text.ParseException if the content is not a parsable date.
     */
    public Date getAsDate() throws ParseException {
        return sdf.parse((String) content);
    }

    /**
     * @return the content value as URL, or raises an exception.
     * @throws MalformedURLException if the content is not a valid URL.
     */
    public URL getAsLink() {
        try {
            return new URL((String) content);
        } catch (MalformedURLException murle) {
            throw new IllegalStateException("Error while parsing URI.", murle);
        }
    }

    /**
     * @return the content value as {@link ItemScope}.
     * @throws ClassCastException if the content is not a valid nested item.
     */
    public ItemScope getAsNested() {
        return (ItemScope) content;
    }

    public String toJSON() {
        return String.format(
                "{ \"content\" : %s, \"type\" : \"%s\" }",
                content instanceof String
                        ?
                    "\"" + StringUtils.escapeAsJSONString( (String) content) + "\""
                        :
                    content,
                type
        );
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return content.hashCode() * type.hashCode() * 2;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(obj instanceof ItemPropValue) {
            final ItemPropValue other = (ItemPropValue) obj;
            return content.equals(other.content) && type.equals(other.type);
        }
        return false;
    }

}
