/*
 * Copyright 2018 Longri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui.style;

import org.tinymediamanager.core.AbstractModelObject;

/**
 * Created by Longri on 05.05.18.
 */
public abstract class AbstractStyle extends AbstractModelObject {

     StyleType type;
    int fontSize;
    String fontFamily;

    public AbstractStyle() {
    }

    /**
     * Sets the look and feel.
     *
     * @throws Exception the exception
     */
    public abstract void setLookAndFeel() throws Exception;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int newValue) {
        int oldValue = fontSize;
        fontSize = newValue;
        firePropertyChange("fontSize", oldValue, newValue);
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String newValue) {
        String oldValue = this.fontFamily;
        this.fontFamily = newValue;
        firePropertyChange("fontFamily", oldValue, newValue);
    }
}
