/*
 * Copyright 2011-2021 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.selenium.component.model;

import java.util.Objects;
import java.util.StringJoiner;

public class SelectItem {

    private int index;
    private String label;
    private String value;
    private boolean selected;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectItem)) {
            return false;
        }
        SelectItem that = (SelectItem) o;
        return getIndex() == that.getIndex() &&
                    isSelected() == that.isSelected() &&
                    Objects.equals(getLabel(), that.getLabel()) &&
                    Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getLabel(), getValue(), isSelected());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SelectItem.class.getSimpleName() + "[", "]")
                    .add("index=" + index)
                    .add("label='" + label + "'")
                    .add("value='" + value + "'")
                    .add("selected=" + selected)
                    .toString();
    }
}
